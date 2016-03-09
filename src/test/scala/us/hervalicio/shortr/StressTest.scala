package us.hervalicio.shortr

import java.net.{InetSocketAddress, URL, URLEncoder}
import java.util.concurrent.atomic.AtomicInteger

import com.google.common.util.concurrent.AtomicLongMap
import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Http, _}
import com.twitter.finagle.stats.SummarizingStatsReceiver
import com.twitter.util.{Await, Future, Stopwatch}

import scala.collection.JavaConverters._


// based on Finagle's example at https://github.com/twitter/finagle/blob/develop/finagle-example/src/main/scala/com/twitter/finagle/example/stress/Stress.scala
object StressTest {

  class CircularQueue[T](list: List[T]) {
    var current = 0

    def next(): T = synchronized {
      val item = list(current)
      current+=1
      if(current >= list.size) {
        current = 0
      }
      item
    }
  }

  def clock(path: String, concurrency: Int, totalRequests: Int, urls: CircularQueue[String]) = {
    val errors = new AtomicInteger(0)
    val responses = AtomicLongMap.create[Status]()

    val statsReceiver = new SummarizingStatsReceiver

    def request() = {
      val request = Request(
        Version.Http11,
        Method.Get,
        urls.next()
      )
      request.headerMap.set("Host", "localhost")
      request
    }

    val client: Service[Request, Response] = ClientBuilder()
        .codec(Http())
        .hosts(new InetSocketAddress("localhost", 8080))
        .hostConnectionCoresize(concurrency)
        .reportTo(statsReceiver)
//        .retries(0)
        .hostConnectionLimit(concurrency)
        .build()

    val completedRequests = new AtomicInteger(0)

    val requests = Future.parallel(concurrency) {
      Future.times(totalRequests / concurrency) {
        client(request()).onSuccess { response =>
          responses.incrementAndGet(response.status)
        }.handle { case e =>
          errors.incrementAndGet()
        }.ensure {
          completedRequests.incrementAndGet()
        }
      }
    }

    val elapsed = Stopwatch.start()

    Future.join(requests).ensure {
      client.close()

      val duration = elapsed()
      println("%20s\t%s".format("Status", "Count"))
      for ((status, count) <- responses.asMap.asScala) {
        println("%20s\t%d".format(status, count))
      }
      println("================")
      println("%d requests completed in %dms (%f requests per second)".format(
        completedRequests.get, duration.inMilliseconds,
        totalRequests.toFloat / duration.inMillis.toFloat * 1000))
      println("%d errors".format(errors.get))

      println("stats")
      println("=====")

      statsReceiver.print()
    }

  }

  def main(args: Array[String]) {
    println("Starting server...")
    Shortr.main(Array())

    val n = 5000

    val longUrls = (1 to n).map { n =>
      s"http://example.com/${n}"
    }

    val shortenRequests = longUrls.map { url =>
      Request.queryString(
        "/shorten",
        Map("url" -> URLEncoder.encode(url, "UTF-8"))
      )
    }.toList

    println("Stress testing shortener")
    Await.result(
      clock("/shorten", 100, n, new CircularQueue(shortenRequests)).ensure {

        // cheating a bit to get all urls in storage so we can try /expand
        val expanderUrls = longUrls.map { u => Await.result(Shortr.storage.findOrCreate(new URL(u))) }
        val expandRequests = expanderUrls.map { url =>
          Request.queryString(
            "/expand",
            Map("url" -> URLEncoder.encode(url.shortenedUrl, "UTF-8"))
          )
        }.toList

        println("Testing expander")
        clock("/expand", 100, n, new CircularQueue(expandRequests)).ensure {
          Shortr.server.close()
        }
      })
  }
}
