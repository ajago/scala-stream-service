package org.alexisjago.videostream.stream

object Exceptions {
  case class MaxStreamsException() extends RuntimeException("Max streams hit")
}
