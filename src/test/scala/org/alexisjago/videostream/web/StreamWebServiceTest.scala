package org.alexisjago.videostream.web

import akka.http.scaladsl.model.StatusCodes
import org.alexisjago.videostream.stream.StreamService
import org.scalatest.{FlatSpec, Matchers}
import org.scalamock.scalatest.MockFactory
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.alexisjago.videostream.stream.Exceptions.MaxStreamsException
import org.alexisjago.videostream.web.StreamWebService._

import scala.util.{Failure, Success}


class StreamWebServiceTest extends FlatSpec with MockFactory with ScalatestRouteTest with Matchers {

  "Stream Web Service" should "get number of streams" in new Test {
    (streamService.getNumberOfStreams _).expects(1).returning(2)

    Get("/user/1/stream/size") ~> route ~> check {
      responseAs[ResponseCountBody] shouldBe ResponseCountBody(2)
    }
  }

  it should "create a new stream" in new Test {
    (streamService.startStream _).expects(1).returning(Success(2))

    Post("/user/1/stream") ~> route ~> check {
      responseAs[ResponseNewStream] shouldBe ResponseNewStream(2)
    }
  }

  it should "not create a new stream if max limit hit" in new Test {
    (streamService.startStream _).expects(1).returning(Failure(MaxStreamsException()))

    Post("/user/1/stream") ~> route ~> check {
      status shouldEqual StatusCodes.Conflict
    }
  }

  it should "stop an existing stream" in new Test {
    (streamService.stopStream _).expects(1, 2)

    Delete("/user/1/stream/2") ~> route ~> check {
      status shouldEqual StatusCodes.NoContent
    }
  }

  trait Test extends StreamWebService {
    val streamService = mock[StreamService]
  }

}
