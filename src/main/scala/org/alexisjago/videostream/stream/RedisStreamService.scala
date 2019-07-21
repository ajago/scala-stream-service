package org.alexisjago.videostream.stream

import com.redis._
import com.redis.serialization.Parse.Implicits._

class RedisStreamService extends StreamService {

  private val keyPrefix = "stream_"
  private val client = new RedisClient("localhost", 6379)

  override def getStreams(userId: Long): Int = {
    client.lrange[Long](s"${keyPrefix}${userId}", 0, -1) match {
      case None => 0
      case _ => 0
    }
  }

  override def startStream(userId: Long): Int = ???

  override def stopStream(userId: Long, streamId: Long): Unit = ???
}
