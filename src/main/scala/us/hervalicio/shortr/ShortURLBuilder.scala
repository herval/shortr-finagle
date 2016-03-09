package us.hervalicio.shortr

import us.hervalicio.shortr.id.Id
import us.hervalicio.shortr.validator.ShortURL

/**
  * Handles converting Ids to short URLs and vice-versa
  *
  * Created by herval on 3/9/16.
  */
class ShortURLBuilder(baseUrl: String) {
  private val pattern = s"${baseUrl}/(\\w+).*".r

  def urlFor(id: Id): String = s"${baseUrl}/${id.readableString}"

  object extractShortUrl {
    def unapply(url: String): Option[ShortURL] = {
      url match {
        case pattern(id) => Some(ShortURL(Id.fromReadableString(id)))
        case _ => None
      }
    }
  }

}
