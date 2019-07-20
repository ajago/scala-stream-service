package org.alexisjago.videostream.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import org.alexisjago.videostream.stream.StreamService
import org.alexisjago.videostream.web.StreamWebService.ResponseCountBody
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol.jsonFormat1
import spray.json.DefaultJsonProtocol._

object StreamWebService{
  case class ResponseCountBody(numberOfStreams: Int)
  case class ResponseNewStream(id: Int)

  implicit val itemFormat = jsonFormat1(ResponseCountBody)
  implicit val itemFormat2 = jsonFormat1(ResponseNewStream)
}

trait StreamWebService extends SprayJsonSupport with DefaultJsonProtocol {

  private val uri_root = "stream"
  val streamService: StreamService

  val route =
    get {
      pathPrefix(uri_root / LongNumber) { id =>
        complete(ResponseCountBody(streamService.getStreams(id)))
      }
    } ~
      post {
        pathPrefix(uri_root / LongNumber) { id =>
          complete(???)
        }
      } ~
      delete {
        pathPrefix(uri_root/ LongNumber) { id =>
          complete(???)
        }
      }
}
