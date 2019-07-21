package org.alexisjago.videostream.web

import akka.http.scaladsl.Http
import org.alexisjago.videostream.stream.{RedisStreamService, StreamService}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

class WebServer(val streamService: StreamService)(
                 implicit val actorSystem: ActorSystem,
                implicit val materializer: ActorMaterializer
                ) extends StreamWebService {

  def startServer(address: String, port: Int) = {
    Http().bindAndHandle(route, address, port)
  }
}

object WebServer {

  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("catalogue-service")
    implicit val materializer = ActorMaterializer()

    val streamService = new RedisStreamService()

    val server = new WebServer(streamService)
    server.startServer("localhost", 7230)
  }

}