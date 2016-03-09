package us.hervalicio.shortr.validator

import java.net.URL

import us.hervalicio.shortr.id.Id

/**
  * Created by herval on 3/9/16.
  */
sealed trait NormalizedURL

case class InvalidURL(url: String) extends NormalizedURL

case class LongURL(url: URL) extends NormalizedURL

case class ShortURL(id: Id) extends NormalizedURL
