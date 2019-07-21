package org.alexisjago.videostream.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import org.alexisjago.videostream.stream.Exceptions.MaxStreamsException
import org.alexisjago.videostream.stream.StreamService
import org.alexisjago.videostream.web.StreamWebService.{ResponseCountBody, ResponseNewStream}
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol.{jsonFormat1, _}

import scala.util.{Failure, Success}

object StreamWebService {

  case class ResponseCountBody(numberOfStreams: Int)

  case class ResponseNewStream(id: Long)

  implicit val itemFormat = jsonFormat1(ResponseCountBody)
  implicit val itemFormat2 = jsonFormat1(ResponseNewStream)
}

trait StreamWebService extends SprayJsonSupport with DefaultJsonProtocol {

  private val uriRoot = "user"
  private val streamEntity = "stream"
  val streamService: StreamService

  val route =
    get {
      pathPrefix(uriRoot / LongNumber / streamEntity / "size") { userId =>
        complete(ResponseCountBody(streamService.getNumberOfStreams(userId)))
      }
    } ~
      post {
        pathPrefix(uriRoot / LongNumber / streamEntity) { userId =>
          streamService.startStream(userId) match {
            case Success(streamId) => complete(ResponseNewStream(streamId))
            case Failure(MaxStreamsException()) => complete(HttpResponse(StatusCodes.Conflict))
            case Failure(_) => complete(HttpResponse(StatusCodes.InternalServerError))
          }
        }
      } ~
      delete {
        pathPrefix(uriRoot / LongNumber / streamEntity / LongNumber) { (userId, streamId) =>
          streamService.stopStream(userId, streamId)
          complete(StatusCodes.NoContent)
        }
      }
}
