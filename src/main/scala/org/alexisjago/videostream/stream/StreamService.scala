package org.alexisjago.videostream.stream

trait StreamService {
  def getStreams(userId: Long): Int

  def startStream(userId: Long): Int

  def stopStream(userId: Long, streamId: Long)
}
