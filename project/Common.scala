
import sbt._
import sbt.Keys._

object Common {

  val settings = Seq(
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq("org.scalaz" %% "scalaz-core" % "7.1.3"),
    resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
  )
}