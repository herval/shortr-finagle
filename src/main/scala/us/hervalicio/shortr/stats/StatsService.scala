package us.hervalicio.shortr.stats

import com.twitter.finagle.Service
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import us.hervalicio.shortr.ResponseBuilder
import us.hervalicio.shortr.shortener.{URLRequest, URLStorageClient}
import us.hervalicio.shortr.validator.ShortURL

/**
  * Created by herval on 3/9/16.
  */
class StatsService(storage: URLStorageClient, stats: StatsClient) extends Service[URLRequest, Response] {

  override def apply(request: URLRequest): Future[Response] = {
    request.requestedUrl match {
      case ShortURL(u) => {
        stats.clicks(u).map { clicks =>
          ResponseBuilder.success(
            ClickStats(u, clicks)
          )
        }
      }
      case _ => Future.value(ResponseBuilder.invalidParam())
    }
  }

}
