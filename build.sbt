name := """play_cassandra_sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

resolvers += "Kundera public" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final", // replace by your jpa implementation,
  "com.impetus.kundera.client" % "kundera-cassandra-ds-driver" % "3.6", // use datasax java driver
  "com.squareup.okhttp" % "okhttp" % "2.7.2" % Test
)

unmanagedJars in Compile += file("extlib/extmodel.jar")

routesGenerator := InjectedRoutesGenerator

PlayKeys.externalizeResources := false

