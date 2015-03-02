
name := "business-warriors"

version := "0.1.0"

scalaVersion := "2.11.5"

logLevel := Level.Info

libraryDependencies ++= Seq(
  "org.json4s"             %% "json4s-jackson"      % "3.2.10",
  "org.scala-lang.modules" %% "scala-xml"           % "1.0.3",
  "com.typesafe.akka"      %% "akka-actor"          % "2.3.9",
  "com.typesafe.akka"      %% "akka-slf4j"          % "2.3.9",
  "ch.qos.logback"         %  "logback-classic"     % "1.1.2",
  "io.spray"               %% "spray-can"           % "1.3.2",
  "io.spray"               %% "spray-client"        % "1.3.2",
  "io.spray"               %% "spray-httpx"         % "1.3.2",
  "io.spray"               %% "spray-routing"       % "1.3.2",
  "io.dropwizard.metrics"  %  "metrics-core"        % "3.1.0",
  "io.dropwizard.metrics"  %  "metrics-jvm"         % "3.1.0",
  "com.github.scopt"       %% "scopt"               % "3.2.0",
  "com.github.marklister"  %% "product-collections" % "1.3.0",
  "org.scalatest"          %% "scalatest"           % "2.2.2" % "test",
  "com.typesafe.akka"      %% "akka-testkit"        % "2.3.9" % "test",
  "io.spray"               %% "spray-testkit"       % "1.3.2" % "test"
)

resolvers ++= Seq(
  "Scala Tools" at "http://scala-tools.org/repo-snapshots/",
  "Akka"        at "http://repo.akka.io/releases/",
  "Typesafe"    at "http://repo.typesafe.com/typesafe/releases"
)
