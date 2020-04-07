import sbt._

object Dependencies {

  val webDeps = Seq(
    "com.h2database" % "h2" % "1.4.199",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
  )

  val commonDeps = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.23",
    "com.typesafe.akka" %% "akka-stream" % "2.5.23",
    "com.typesafe.akka" %% "akka-http" % "10.1.1",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1"
  )
}
