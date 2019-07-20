package org.alexisjago.videostream.stream

trait StreamService {
  def getStreams(id: Long): Int
  def startStream(id: Long)
  def stopStream(id: Long)
}
