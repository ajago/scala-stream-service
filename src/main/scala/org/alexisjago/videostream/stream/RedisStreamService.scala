package org.alexisjago.videostream.stream

import com.redis._
import com.redis.serialization.Parse.Implicits._
import org.alexisjago.videostream.stream.Exceptions.MaxStreamsException

import scala.util.{Failure, Success, Try}

class RedisStreamService(keyPrefix: String = "stream_") extends StreamService {

  private val client = new RedisClient("localhost", 6379)
  private val rnd = scala.util.Random

  override def getNumberOfStreams(userId: Long): Int = {
    client.lrange[Long](s"$keyPrefix$userId", 0, -1) match {
      case None => 0
      case Some(l) => l.flatten.length
    }
  }

  override def startStream(userId: Long): Try[Long] = {

    val key = s"$keyPrefix$userId"

    val streams = client.llen(key).getOrElse(0L).toInt

    if (streams >= 3)
      Failure(MaxStreamsException())
    else {
      val streamId = rnd.nextLong(9999999L)
      client.lpush(key, streamId) match {
        case Some(_) => Success(streamId)
        case None => Failure(new RuntimeException("Redis error"))
      }
    }
  }

  override def stopStream(userId: Long, streamId: Long): Unit = {
    client.lrem(s"$keyPrefix$userId", 0, streamId)
  }
}
