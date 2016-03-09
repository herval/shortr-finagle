package us.hervalicio.shortr.service

import java.net.URLDecoder

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future
import us.hervalicio.shortr.validator.{InvalidURL, Validator}

/**
  * Validates if our url param is present and valid
  *
  * Created by herval on 3/9/16.
  */
class ParamValidator(validator: Validator) extends Filter[Request, Response, URLRequest, Response] {

  override def apply(request: Request, service: Service[URLRequest, Response]): Future[Response] = {
    decodedParam(request) match {
      case Some(url) => validator.normalize(url).flatMap {
        case InvalidURL(_) => Future.value(ResponseBuilder.invalidParam())
        case normalized => service(URLRequest(request, normalized))
      }
      case None => Future.value(ResponseBuilder.missingParam())
    }
  }

  def decodedParam(request: Request): Option[String] = {
    Option(request.getParam("url")).map(u => URLDecoder.decode(u, "UTF-8"))
  }
}
