package org.alexisjago.videostream.stream

import com.redis._
import com.redis.serialization.Parse.Implicits._

class RedisStreamService(keyPrefix: String = "stream_") extends StreamService {

  private val client = new RedisClient("localhost", 6379)

  override def getStreams(userId: Long): Int = {
    client.lrange[Long](s"${keyPrefix}${userId}", 0, -1) match {
      case None => 0
      case Some(l) => l.flatten.length
    }
  }

  override def startStream(userId: Long): Long =
    client.lpush(s"${keyPrefix}$userId", 1L).getOrElse(throw new RuntimeException("Redis error"))

  override def stopStream(userId: Long, streamId: Long): Unit = {
    client.lrem(s"${keyPrefix}${userId}", 0, streamId)
  }
}
