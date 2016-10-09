name := """play_cassandra_sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

resolvers += "Kundera public" at "https://oss.sonatype.org/content/repositories/releases"

// need kundera entity metadeta load
unmanagedClasspath  in Runtime += file("target/scala-2.11/classes")

libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "com.impetus.kundera.client" % "kundera-cassandra-ds-driver" % "3.6", // use datasax java driver
  "com.squareup.okhttp" % "okhttp" % "2.7.2" % Test
)


routesGenerator := InjectedRoutesGenerator

PlayKeys.externalizeResources := false

