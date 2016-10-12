name := """play_cassandra_sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)

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
  "com.squareup.okhttp" % "okhttp" % "2.7.2" % Test,
  "com.mysema.querydsl" % "querydsl-jpa" % "3.7.4",
  "com.mysema.querydsl" % "querydsl-apt" % "3.7.4"
)


routesGenerator := InjectedRoutesGenerator

PlayKeys.externalizeResources := false

// add Query DSL APT process
// It can usu to run `querydsl`
lazy val querydsl = taskKey[Unit]("query-dsl")

querydsl := {
  val log = streams.value.log

  log.info("Processing annotations QueryDsl ...")

  val separator = System.getProperty("path.separator");
  val classpath = ((products in Compile).value ++ ((dependencyClasspath in Compile).value.files)) mkString separator
  val processor = "com.mysema.query.apt.jpa.JPAAnnotationProcessor"
  val modelClasses = (new File("app/models").listFiles().filter(f => f.getName.endsWith(".java"))
                        .map(f => "models." + f.getName().substring(0, f.getName().length-5))
                      )  mkString " "
  val command = s"javac -cp $classpath -proc:only -processor $processor -XprintRounds -s app -d target/scala-2.11/classes $modelClasses"

  failIfNonZeroExitStatus(command, "Failed to process annotations.", log)

  log.info("Done processing annotations.")
}

def failIfNonZeroExitStatus(command: String, message: => String, log: Logger) {
  val result = command !

  if (result != 0) {
    log.error(message)
    sys.error("Failed running command: " + command)
  }
}

