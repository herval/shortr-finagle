package us.hervalicio.shortr.service

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import us.hervalicio.shortr.storage.ShortURLStorage
import us.hervalicio.shortr.validator.{LongURL, Validator}

/**
  * Given a long URL, retrieve the short version
  *
  * Created by herval on 3/9/16.
  */
class ShortenerService(val validator: Validator, val urls: ShortURLStorage) extends Service[Request, Response] {

  override def apply(request: Request): Future[Response] = {
    Option(request.getParam("url")).map(normalizeAndStore).getOrElse(
      Future.value(
        ResponseBuilder.missingParam()
      )
    )
  }

  def normalizeAndStore(url: String): Future[Response] = {
    validator.normalize(url).flatMap {
      case LongURL(u) => urls.findOrCreate(u).map(ResponseBuilder.success)
      case _ => Future.value(ResponseBuilder.invalidParam()) // no point on shortening short urls?
    }
  }

}
