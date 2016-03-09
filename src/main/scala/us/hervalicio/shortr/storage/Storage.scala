package us.hervalicio.shortr.storage

import java.net.URL

import com.twitter.util.Future
import us.hervalicio.shortr.id.Id

/**
  * Key-value storage of short & long URLs
  *
  * Created by herval on 3/9/16.
  */
trait Storage {

  def originalFor(id: Id): Future[Option[ShortenedURL]]

  def findOrCreate(longUrl: URL): Future[ShortenedURL]

}
