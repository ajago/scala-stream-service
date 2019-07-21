package org.alexisjago.videostream.stream

class RedisStreamService extends StreamService {
  override def getStreams(userId: Long): Int = ???

  override def startStream(userId: Long): Int = ???

  override def stopStream(userId: Long, streamId: Long): Unit = ???
}
