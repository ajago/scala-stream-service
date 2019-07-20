package org.alexisjago.videostream.stream

trait StreamService {
  def getStreams(id: Int)
  def startStream(id: Int)
  def stopStream(id: Int)
}
