package us.hervalicio.shortr.service

import com.twitter.finagle.http.Request
import com.twitter.util.{Await, Future}
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers.{anyLong, eq => eql}
import us.hervalicio.shortr.id.Id
import us.hervalicio.shortr.shortener.{ExpanderService, URLPair, URLRequest, URLStorageClient}
import us.hervalicio.shortr.stats.StatsClient
import us.hervalicio.shortr.validator.ShortURL

/**
  * Created by herval on 3/9/16.
  */
class ExpanderServiceTest extends FunSuite with MockitoSugar {

  val urls = mock[URLStorageClient]
  val stats = mock[StatsClient]
  val service = new ExpanderService(urls, stats)

  test("finds the original URL and computes stats") {
    val request = mock[Request]
    val id = Id(1)
    val requestedUrl = ShortURL(id)
    val url = URLPair("foo", "bar")

    when(urls.originalFor(id)).thenReturn(Future.value(Some(url)))
    when(stats.click(eql(id), anyLong())).thenReturn(Future.Done)

    val response = Await.result(
      service.apply(URLRequest(request, requestedUrl))
    )
  }

}
