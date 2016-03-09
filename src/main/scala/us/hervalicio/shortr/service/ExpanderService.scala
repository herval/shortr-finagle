package us.hervalicio.shortr.service

import com.twitter.finagle.Service
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import us.hervalicio.shortr.shortener.URLStorage
import us.hervalicio.shortr.validator.ShortURL

/**
  * Given a short URL, expand it to the original long URL
  *
  * Created by herval on 3/9/16.
  */
class ExpanderService(val urls: URLStorage) extends Service[URLRequest, Response] {

  override def apply(request: URLRequest): Future[Response] = {
    request.requestedUrl match {
      case ShortURL(u) => urls.originalFor(u).map {
        case Some(shortened) => ResponseBuilder.success(shortened)
        case None => ResponseBuilder.invalidParam()
      }
      case _ => Future.value(ResponseBuilder.invalidParam())
    }
  }
}