package us.hervalicio.shortr

import java.net.{InetAddress, InetSocketAddress}

import com.twitter.finagle.Service
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.finagle.http.service.RoutingService
import us.hervalicio.shortr.id.TimestampBasedGenerator
import us.hervalicio.shortr.service.{ExpanderService, ParamValidator, ShortenerService}
import us.hervalicio.shortr.shortener.ShortURLBuilder
import us.hervalicio.shortr.storage.InMemoryStorage
import us.hervalicio.shortr.validator.SimpleNormalizer

/**
  * Created by herval on 3/9/16.
  */
object Shortr extends App {

  val baseUrl = "http://shr.tr"
  val builder = new ShortURLBuilder(baseUrl)
  val normalizer = new SimpleNormalizer(builder)
  val storage = new InMemoryStorage(
    new TimestampBasedGenerator(InetAddress.getLocalHost.hashCode()), builder
  ) // TODO configure a machine id

  val validate = new ParamValidator(normalizer)
  val shorten = new ShortenerService(storage)
  val expand = new ExpanderService(storage)

  val router: Service[Request, Response] = RoutingService.byPath {
    case "/shorten" => validate andThen shorten
    case "/expand" => validate andThen expand
    // TODO analytics
  }

  val server: Server = ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(8080))
      .name("shortr")
      .build(router)
}


// TODO Stress-tester