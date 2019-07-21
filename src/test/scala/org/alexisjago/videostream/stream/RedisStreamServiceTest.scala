package org.alexisjago.videostream.stream

import com.redis.RedisClient
import org.scalatest.{FlatSpec, Matchers}
import com.redis.serialization.Parse.Implicits._

class RedisStreamServiceTest extends FlatSpec with Matchers {

  val client = new RedisClient("localhost", 6379)
  val rnd = scala.util.Random

  "RedisStreamService" should "return 0 for a new userId" in new Test {
    redisStreamService.getStreams(1) shouldBe 0
  }

  it should "create a new stream" in new Test {
    val streamId = redisStreamService.startStream(1)
    val streams = client.lrange[Long](s"$keyPrefix$streamId", 0, -1)

    streams shouldBe Some(List(Some(streamId)))
  }

  it should "get streams for a valid user" in new Test {
    val userId = 2L
    val streamId = 5L
    val streamId2 = 10L
    client.lpush(s"${keyPrefix}$userId", streamId, streamId2)

    redisStreamService.getStreams(userId) shouldBe 2
  }

  it should "delete an existing stream" in new Test {
    val userId = 2L
    val streamId = 5L
    client.lpush(s"${keyPrefix}$userId", streamId)

    redisStreamService.stopStream(userId, streamId)
    val streams = client.lrange[Long](s"$keyPrefix$userId", 0, -1)

    streams shouldBe Some(List())
  }

  trait Test {
    val keyPrefix: String = rnd.nextLong().toString + '_'
    println(s"Using key prefix - ${keyPrefix}")
    val redisStreamService = new RedisStreamService(keyPrefix)
  }
}
