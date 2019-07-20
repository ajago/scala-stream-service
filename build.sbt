name := "akka-video-stream"

version := "0.1"

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.0-M4" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.9" % Test,
  "org.scalamock" %% "scalamock" % "4.3.0" % Test
)
