package org.alexisjago.videostream.stream

import scala.util.Try

trait StreamService {
  def getNumberOfStreams(userId: Long): Int

  def startStream(userId: Long): Try[Long]

  def stopStream(userId: Long, streamId: Long)

}
