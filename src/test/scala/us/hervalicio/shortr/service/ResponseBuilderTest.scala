package us.hervalicio.shortr.service

import org.scalatest.FunSuite
import us.hervalicio.shortr.shortener.ShortenedURL

/**
  * Created by herval on 3/9/16.
  */
class ResponseBuilderTest extends FunSuite {

  test("Json generation") {
    assert(ResponseBuilder.success(ShortenedURL("http://twitter.com", "http://tw.tr/123")).contentString.length > 2)
  }

}
