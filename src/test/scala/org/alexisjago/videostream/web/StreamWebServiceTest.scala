package org.alexisjago.videostream.web

import org.alexisjago.videostream.stream.StreamService
import org.scalatest.{FlatSpec, Matchers}
import org.scalamock.scalatest.MockFactory
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.alexisjago.videostream.web.StreamWebService._


class StreamWebServiceTest extends FlatSpec with MockFactory with ScalatestRouteTest with Matchers {

  "Stream Web Service" should "get number of streams" in new Test{
    Get("/stream/1") ~> route ~> check {
      responseAs[ResponseCountBody] shouldBe ResponseCountBody(1)
    }
  }

  trait Test extends StreamWebService {
    val streamService = mock[StreamService]
  }
}
