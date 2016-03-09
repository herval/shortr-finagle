package us.hervalicio.shortr.service

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import us.hervalicio.shortr.storage.ShortURLStorage
import us.hervalicio.shortr.validator.{ShortURL, Validator}

/**
  * Given a short URL, expand it to the original long URL
  *
  * Created by herval on 3/9/16.
  */
class ExpanderService(val validator: Validator, val urls: ShortURLStorage) extends Service[Request, Response] {

  override def apply(request: Request): Future[Response] = {
    // TODO make this a filter?
    Option(request.getParam("url")).map(normalizeAndLookup).getOrElse(
      Future.value(
        ResponseBuilder.missingParam()
      )
    )
  }

  def normalizeAndLookup(url: String): Future[Response] = {
    validator.normalize(url).flatMap {
      case ShortURL(u) => urls.originalFor(u)
      case _ => Future.value(None)
    }.map {
      case Some(u) => ResponseBuilder.success(u)
      case None => ResponseBuilder.invalidParam()
    }
  }
}