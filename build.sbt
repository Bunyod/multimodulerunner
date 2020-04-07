import Dependencies._
import spray.revolver.AppProcess

name := "multisample"
organization := "com.multisample"
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.10"

lazy val appserver =
  (project in file("common"))
  .settings(libraryDependencies ++= commonDeps)
  .settings(mainClass in (Compile, run) := Some("testapp.ApplicationActor"))

lazy val web = (project in file("multisample"))
  .enablePlugins(PlayScala)
  .settings(libraryDependencies ++= webDeps:+ guice)
  .disablePlugins(RevolverPlugin)

lazy val root = (project in file(".")).aggregate(`appserver`, web)

runWebServer := {
    (run in Compile in `web`).evaluated
}

val runWebServer = inputKey[Unit]("Runs web-server")
