package us.hervalicio.shortr

import java.net.InetSocketAddress

import com.twitter.finagle.Service
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.finagle.http.service.RoutingService
import us.hervalicio.shortr.id.IdGeneratorClient
import us.hervalicio.shortr.service.{ExpanderService, ParamValidatorFilter, ShortenerService, StatsService}
import us.hervalicio.shortr.shortener.{ShortURLBuilder, URLStorageClient}
import us.hervalicio.shortr.stats.StatsClient
import us.hervalicio.shortr.storage.memory.InMemoryKeyValueStorage
import us.hervalicio.shortr.validator.SimpleNormalizer

/**
  * Created by herval on 3/9/16.
  */
object Shortr extends App {

  val baseUrl = "http://shr.tr"
  val storage = new InMemoryKeyValueStorage
  val builder = new ShortURLBuilder(baseUrl)

  val ids = new IdGeneratorClient(storage)
  val urls = new URLStorageClient(builder, storage, ids)
  val stats = new StatsClient(storage)

  val validate = new ParamValidatorFilter(new SimpleNormalizer(builder))
  val shorten = new ShortenerService(urls)
  val expand = new ExpanderService(urls, stats)
  val showStats = new StatsService(urls, stats)

  val router: Service[Request, Response] = RoutingService.byPath {
    case "/shorten" => validate andThen shorten
    case "/expand" => validate andThen expand
    case "/stats" => validate andThen showStats
  }

  val server: Server = ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(8080))
      .name("shortr")
      .build(router)
}
