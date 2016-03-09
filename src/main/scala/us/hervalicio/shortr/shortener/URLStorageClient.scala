package us.hervalicio.shortr.shortener

import java.net.URL

import com.twitter.util.Future
import us.hervalicio.shortr.id.{Id, IdGenerator}
import us.hervalicio.shortr.storage.KeyValueStorage

/**
  * A storage façade for accessing a key-value storage to store and retrieve URLs
  *
  * Created by herval on 3/9/16.
  */
class URLStorageClient(builder: ShortURLBuilder, storage: KeyValueStorage, idGenerator: IdGenerator) extends URLStorage {

  override def originalFor(id: Id): Future[Option[URLPair]] = Future {
    storage.get[URLPair](id.number.toString)
  }

  override def findOrCreate(longUrl: URL): Future[URLPair] = {
    // since longUrl -> shortUrl can't be calculated, we need two lookups to find it
    storage.get[String](longUrl.toString).flatMap(id => storage.get[URLPair](id).map(Future.value)).
        getOrElse {
          idGenerator.nextId().map { id =>
            val newUrl = URLPair(longUrl.toString, builder.urlFor(id))
            storage.put[String](longUrl.toString, id.number.toString)
            storage.put[URLPair](id.number.toString, newUrl)
            newUrl
          }
        }
  }

}
