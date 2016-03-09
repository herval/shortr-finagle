package us.hervalicio.shortr.service

import org.scalatest.FunSuite
import us.hervalicio.shortr.ResponseBuilder
import us.hervalicio.shortr.shortener.URLPair

/**
  * Created by herval on 3/9/16.
  */
class ResponseBuilderTest extends FunSuite {

  test("Json generation") {
    assert(ResponseBuilder.success(URLPair("http://twitter.com", "http://tw.tr/123")).contentString.length > 2)
  }

}
