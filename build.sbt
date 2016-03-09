name := "shortr"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.33.0",
  "com.twitter" %% "finagle-redis" % "6.33.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test"
)
