package us.hervalicio.shortr.storage

import java.net.URL
import java.util.concurrent.atomic.AtomicLong

import com.twitter.util.Future
import us.hervalicio.shortr.id.{Id, IdGenerator}
import us.hervalicio.shortr.shortener.ShortURLBuilder

import scala.collection.mutable

/**
  * A stub storage, to be replaced by the real deal when it's provided
  *
  * Created by herval on 3/9/16.
  */
class InMemoryStorage(builder: ShortURLBuilder) extends ShortURLStorage with IdGenerator {

  private val currentId = new AtomicLong(1000)
  private val urls: mutable.Map[Id, ShortenedURL] = new mutable.HashMap()
  private val originalToId: mutable.Map[String, Id] = new mutable.HashMap()

  override def originalFor(id: Id): Future[Option[ShortenedURL]] = Future {
    Thread.sleep(5) // simulating some network latency
    urls.get(id)
  }

  override def findOrCreate(longUrl: URL): Future[ShortenedURL] = {
    // since longUrl -> shortUrl can't be calculated, we need two lookups to find it
    originalToId.get(longUrl.toString).flatMap(id => urls.get(id).map(Future.value)).
        getOrElse {
          nextId().map { id =>
            Thread.sleep(5) // simulating some network latency

            val newUrl = ShortenedURL(longUrl.toString, builder.urlFor(id))
            originalToId.put(longUrl.toString, id)
            urls.put(id, newUrl)
            newUrl
          }
        }
  }

  override def nextId(): Future[Id] = Future {
    Thread.sleep(5) // simulating some network latency
    Id(currentId.getAndIncrement())
  }
}
