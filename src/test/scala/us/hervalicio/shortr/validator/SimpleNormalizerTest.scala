package us.hervalicio.shortr.validator

import java.net.URL

import com.twitter.util.Await
import org.scalatest.FunSuite
import us.hervalicio.shortr.id.Id
import us.hervalicio.shortr.shortener.ShortURLBuilder

/**
  * Created by herval on 3/9/16.
  */
class SimpleNormalizerTest extends FunSuite {

  val normalizer = new SimpleNormalizer(new ShortURLBuilder("http://shr.tr"))

  test("A valid url passes validation") {
    val expected = LongURL(new URL("http://twitter.com"))
    assert(
      Await.result(normalizer.normalize("http://twitter.com")) == expected
    )
  }

  test("An invalid URL fails") {
    assert(
      Await.result(normalizer.normalize("httpwhoops")) == InvalidURL("httpwhoops")
    )
  }

  test("A short url passes validation") {
    val expected = ShortURL(Id(123))
    assert(
      Await.result(normalizer.normalize("http://shr.tr/3f")) == expected
    )
  }

}
