package us.hervalicio.shortr.service

import com.twitter.finagle.http.{Request, RequestProxy}
import us.hervalicio.shortr.validator.NormalizedURL

/**
  * Created by herval on 3/9/16.
  */
case class URLRequest(request: Request, requestedUrl: NormalizedURL) extends RequestProxy
