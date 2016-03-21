package us.hervalicio.shortr.shortener

import us.hervalicio.shortr.ResponseType

/**
  * Created by herval on 3/9/16.
  */
case class URLPair(originalUrl: String, shortenedUrl: String) extends ResponseType
