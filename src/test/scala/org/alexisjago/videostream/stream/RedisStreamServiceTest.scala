package org.alexisjago.videostream.stream

import org.scalatest.{FlatSpec, Matchers}

class RedisStreamServiceTest extends FlatSpec with Matchers {

  val redisStreamService = new RedisStreamService

  "RedisStreamService" should "return 0 for a new userId" in {
    redisStreamService.getStreams(1) shouldBe 0
  }
}
