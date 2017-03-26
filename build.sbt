name := "Overwatch"

version := "1.0"

lazy val `overwatch` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc , cache , ws , specs2 % Test,
  "net.ruippeixotog" %% "scala-scraper" % "1.2.0",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "net.liftweb" % "lift-json" % "2.0"
)

initialCommands in console += "import net.ruippeixotog.scalascraper._"

initialCommands in console += "import scalaj.http._"

initialCommands in console += "import net.liftweb.json._"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
