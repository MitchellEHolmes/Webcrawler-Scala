name := "webcrawler"

version := "1.0"

scalaVersion := "2.12.4"

resolvers += "akka" at "http://repo.akka.io/snapshots"

libraryDependencies ++= {
  Seq(
    "com.typesafe.akka" %% "akka-actor"                           % "2.4.19",
    "org.jsoup"         % "jsoup"                                 % "1.8+",
    "commons-validator" % "commons-validator"                     % "1.5+"
  )
}