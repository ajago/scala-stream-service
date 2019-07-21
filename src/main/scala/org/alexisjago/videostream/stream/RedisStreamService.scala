package org.alexisjago.videostream.stream

import com.redis._
import com.redis.serialization.Parse.Implicits._
import org.alexisjago.videostream.stream.Exceptions.MaxStreamsException

import scala.util.{Failure, Success, Try}

class RedisStreamService(keyPrefix: String = "stream_") extends StreamService {

  private val redisHost = "localhost"
  private val redisPort = 6379
  
  private val rnd = scala.util.Random

  override def getNumberOfStreams(userId: Long): Int = {
    val client = new RedisClient(redisHost, redisPort)

    client.lrange[Long](s"$keyPrefix$userId", 0, -1) match {
      case None => 0
      case Some(l) => l.flatten.length
    }
  }

  override def startStream(userId: Long): Try[Long] = {

    val client = new RedisClient(redisHost, redisPort)
    val key = s"$keyPrefix$userId"

    client.watch(key)
    val streams = client.llen(key).getOrElse(0L).toInt

    // Use below line to manually test concurrency functionality
//    Thread.sleep((5000))

    if (streams >= 3) {
      client.unwatch()
      Failure(MaxStreamsException())
    }
    else {
      val streamId = rnd.nextLong(9999999L)
      client.pipeline { p =>
        p.lpush(key, streamId)
      }.map(_.head).collect {
        case Some(_) => Success(streamId)
        case _ => Failure(new RuntimeException("Redis error"))
      }.getOrElse(Failure(new MaxStreamsException())) //watched key has been altered.
    }
  }

  override def stopStream(userId: Long, streamId: Long): Unit = {
    val client = new RedisClient(redisHost, redisPort)
    client.lrem(s"$keyPrefix$userId", 0, streamId)
  }
}
