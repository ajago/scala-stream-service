package org.alexisjago.videostream.stream

import com.redis.RedisClient
import org.scalatest.{FlatSpec, Matchers}
import com.redis.serialization.Parse.Implicits._
import org.alexisjago.videostream.stream.Exceptions.MaxStreamsException

import scala.util.{Failure, Success}

class RedisStreamServiceTest extends FlatSpec with Matchers {

  val client = new RedisClient("localhost", 6379)
  val rnd = scala.util.Random

  "RedisStreamService" should "return 0 for a new userId" in new Test {
    redisStreamService.getNumberOfStreams(1) shouldBe 0
  }

  it should "create a new stream" in new Test {
    val userId = 1
    val streamIdTry = redisStreamService.startStream(userId)
    val streams = client.lrange[Long](s"$keyPrefix$userId", 0, -1)

    streamIdTry shouldBe a [Success[_]]
    val streamId = streamIdTry.get
    streams shouldBe Some(List(Some(streamId)))
  }

  it should "get streams for a valid user" in new Test {
    val userId = 2L
    val streamId = 5L
    val streamId2 = 10L
    client.lpush(s"${keyPrefix}$userId", streamId, streamId2)

    redisStreamService.getNumberOfStreams(userId) shouldBe 2
  }

  it should "delete an existing stream" in new Test {
    val userId = 2L
    val streamId = 5L
    client.lpush(s"${keyPrefix}$userId", streamId)

    redisStreamService.stopStream(userId, streamId)
    val streams = client.lrange[Long](s"$keyPrefix$userId", 0, -1)

    streams shouldBe Some(List())
  }

  it should "not allow more than 3 streams" in new Test {
    val userId = 2L
    val streamId = 5L
    val streamId2 = 10L
    val streamId3 = 15L
    val streamId4 = 20L
    client.lpush(s"${keyPrefix}$userId", streamId, streamId2, streamId3)

    val response = redisStreamService.startStream(userId)

    response shouldBe Failure(MaxStreamsException())
  }

  trait Test {
    val keyPrefix: String = rnd.nextLong().toString + '_'
    println(s"Using key prefix - ${keyPrefix}")
    val redisStreamService = new RedisStreamService(keyPrefix)
  }
}
