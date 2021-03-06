package us.hervalicio.shortr.shortener

import com.twitter.finagle.Service
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import us.hervalicio.shortr.ResponseBuilder
import us.hervalicio.shortr.validator.LongURL

/**
  * Given a long URL, retrieve the short version
  *
  * Created by herval on 3/9/16.
  */
class ShortenerService(val urls: URLStorage) extends Service[URLRequest, Response] {

  override def apply(request: URLRequest): Future[Response] = {
    request.requestedUrl match {
      case LongURL(u) => urls.findOrCreate(u).map(ResponseBuilder.success)
      case _ => Future.value(ResponseBuilder.invalidParam()) // no point on shortening short urls?
    }
  }

}
