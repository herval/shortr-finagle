package us.hervalicio.shortr.service

import com.twitter.finagle.Service
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import us.hervalicio.shortr.shortener.URLStorage
import us.hervalicio.shortr.stats.StatsClient
import us.hervalicio.shortr.validator.ShortURL

/**
  * Given a short URL, expand it to the original long URL
  *
  * Created by herval on 3/9/16.
  */
class ExpanderService(urls: URLStorage, stats: StatsClient) extends Service[URLRequest, Response] {

  override def apply(request: URLRequest): Future[Response] = {
    request.requestedUrl match {
      case ShortURL(u) => urls.originalFor(u).flatMap {
        case Some(expanded) => {
          // WIP use a Client so this service keeps working if stats is down
          stats.click(u, System.currentTimeMillis()).map { _ =>
            ResponseBuilder.success(expanded)
          }
        }
        case None => Future.value(ResponseBuilder.invalidParam())
      }
      case _ => Future.value(ResponseBuilder.invalidParam())
    }
  }
}