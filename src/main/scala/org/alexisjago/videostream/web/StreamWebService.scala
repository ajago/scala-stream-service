package org.alexisjago.videostream.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import org.alexisjago.videostream.stream.StreamService
import org.alexisjago.videostream.web.StreamWebService.{ResponseCountBody, ResponseNewStream}
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol.jsonFormat1
import spray.json.DefaultJsonProtocol._

object StreamWebService {

  case class ResponseCountBody(numberOfStreams: Int)

  case class ResponseNewStream(id: Int)

  implicit val itemFormat = jsonFormat1(ResponseCountBody)
  implicit val itemFormat2 = jsonFormat1(ResponseNewStream)
}

trait StreamWebService extends SprayJsonSupport with DefaultJsonProtocol {

  private val uriRoot = "user"
  private val streamEntity = "stream"
  val streamService: StreamService

  val route =
    get {
      pathPrefix(uriRoot / LongNumber / streamEntity) { userId =>
        complete(ResponseCountBody(streamService.getStreams(userId)))
      }
    } ~
      post {
        pathPrefix(uriRoot / LongNumber / streamEntity) { userId =>
          complete(ResponseNewStream(streamService.startStream(userId)))
        }
      } ~
      delete {
        pathPrefix(uriRoot / LongNumber) { id =>
          complete(???)
        }
      }
}
