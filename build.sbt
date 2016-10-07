name := """play_cassandra_sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final", // replace by your jpa implementation,
  "com.squareup.okhttp" % "okhttp" % "2.7.2" % Test
)

routesGenerator := InjectedRoutesGenerator

PlayKeys.externalizeResources := false

