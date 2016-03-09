package us.hervalicio.shortr.validator

import java.net.URL

import com.twitter.util.Future
import us.hervalicio.shortr.ShortURLBuilder

/**
  * Service that normalizes an input URL
  *
  * Current implementation will only validate whether URLs are valid.
  * Subsequent versions can, for instance, allow for checking if a URL is a redirect
  * (so that if http://example.com and http://new.example.com point to the same place, the same URL is returned)
  *
  * Created by herval on 3/9/16.
  */
class SimpleNormalizer(builder: ShortURLBuilder) extends Validator {

  def normalize(url: String): Future[NormalizedURL] = Future {
    try {
      url match {
        case builder.extractShortUrl(short) => short
        case _ => LongURL(new URL(url)) // TODO this is slow?
      }
    } catch {
      case e: Exception => InvalidURL(url)
    }
  }

}
