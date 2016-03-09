package us.hervalicio.shortr

import org.scalatest.FunSuite
import us.hervalicio.shortr.id.Id
import us.hervalicio.shortr.validator.ShortURL

/**
  * Created by herval on 3/9/16.
  */
class ShortURLBuilderTest extends FunSuite {

  val builder = new ShortURLBuilder("http://foo.com")

  test("extracts from a url string") {
    assert(
      builder.extractShortUrl.unapply("http://foo.com/3f?ignored=foo") == Some(ShortURL(Id(123)))
    )
  }

  test("converts an Id to a valid url") {
    assert(
      builder.urlFor(Id(123)) == "http://foo.com/3f"
    )
  }

}
