package us.hervalicio.shortr

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.{Response, Status}

trait ResponseType


/**
  * Created by herval on 3/9/16.
  */
object ResponseBuilder {
  val jsonMapper = new ObjectMapper()
  jsonMapper.registerModule(DefaultScalaModule)


  def notFound() = Response(Http11, Status.NotFound)

  def invalidParam() = Response(Http11, Status.UnprocessableEntity)

  def missingParam() = Response(Http11, Status.BadRequest)

  def success(data: ResponseType) = {
    val r = Response(Http11, Status.Ok)
    r.contentString = jsonMapper.writeValueAsString(data)
    r
  }

}
