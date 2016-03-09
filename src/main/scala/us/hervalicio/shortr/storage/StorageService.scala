package us.hervalicio.shortr.storage

import java.net.URL

import com.twitter.util.Future
import us.hervalicio.shortr.id.{Id, IdGenerator}
import us.hervalicio.shortr.shortener.ShortURLBuilder
import us.hervalicio.shortr.storage.memory.InMemoryKeyValueStorage

/**
  * A storage façade for accessing a key-value storage to store and retrieve URLs
  *
  * Created by herval on 3/9/16.
  */
class StorageService(builder: ShortURLBuilder, storage: InMemoryKeyValueStorage) extends Storage with IdGenerator {

  override def originalFor(id: Id): Future[Option[ShortenedURL]] = Future {
    Thread.sleep(5) // simulating some network latency
    storage.get[ShortenedURL](id.number.toString)
  }

  override def findOrCreate(longUrl: URL): Future[ShortenedURL] = {
    // since longUrl -> shortUrl can't be calculated, we need two lookups to find it
    storage.get[String](longUrl.toString).flatMap(id => storage.get[ShortenedURL](id).map(Future.value)).
        getOrElse {
          nextId().map { id =>
            val newUrl = ShortenedURL(longUrl.toString, builder.urlFor(id))
            storage.put[String](longUrl.toString, id.number.toString)
            storage.put[ShortenedURL](id.number.toString, newUrl)
            newUrl
          }
        }
  }

  override def nextId(): Future[Id] = Future {
    Id(storage.incr("sequential_id"))
  }

}
