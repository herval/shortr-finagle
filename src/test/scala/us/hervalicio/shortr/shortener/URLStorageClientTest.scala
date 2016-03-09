package us.hervalicio.shortr.shortener

import java.net.URL

import com.twitter.util.Await
import org.scalatest.FunSuite
import us.hervalicio.shortr.id.{Id, IdGeneratorClient}
import us.hervalicio.shortr.storage.memory.InMemoryKeyValueStorage

/**
  * Created by herval on 3/9/16.
  */
class URLStorageClientTest extends FunSuite {

  val storage = new InMemoryKeyValueStorage()

  val client = new URLStorageClient(
    new ShortURLBuilder("http://shr.tr"),
    storage,
    new IdGeneratorClient(storage)
  )

  test("find original URL for an existing short url") {
    val existing = Await.result(client.findOrCreate(new URL("http://foo.com")))
    val found = Await.result(client.originalFor(Id(1)))
    assert(found === Some(existing))
  }

  test("store a new URL") {
    val url = Await.result(client.findOrCreate(new URL("http://foo.com")))
    assert(url.shortenedUrl == "http://shr.tr/1")
  }

}
