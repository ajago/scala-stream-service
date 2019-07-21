package org.alexisjago.videostream.web

import org.alexisjago.videostream.stream.StreamService
import org.scalatest.{FlatSpec, Matchers}
import org.scalamock.scalatest.MockFactory
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.alexisjago.videostream.web.StreamWebService._


class StreamWebServiceTest extends FlatSpec with MockFactory with ScalatestRouteTest with Matchers {

  "Stream Web Service" should "get number of streams" in new Test{
    (streamService.getStreams _).expects(1).returning(2)

    Get("/stream/1") ~> route ~> check {
      responseAs[ResponseCountBody] shouldBe ResponseCountBody(2)
    }
  }

  it should "create a new stream" in new Test {
    (streamService.startStream _).expects(1).returning(2)

    Post("/stream/1") ~> route ~> check {
      responseAs[ResponseNewStream] shouldBe ResponseNewStream(2)
    }
  }

  trait Test extends StreamWebService {
    val streamService = mock[StreamService]
  }
}
