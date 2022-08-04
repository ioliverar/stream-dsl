name := "kstream-scala-transaction"
version := "0.1"
scalaVersion := "2.12.8"
organization := "io.orb"

//Repositories
resolvers += "confluent" at "https://packages.confluent.io/maven/"

//Scala Libraries
libraryDependencies += "org.scala-lang" % "scala-library" % "2.12.8"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.12.8"
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.12.8"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

//Util libraries
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "commons-io" % "commons-io" % "2.6"
libraryDependencies += "org.apache.avro" % "avro" % "1.9.2"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.apache.commons" % "commons-dbcp2" % "2.7.0"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.28"

//Kafka Libraries
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "5.4.1-ccs"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "5.4.1-ccs"
libraryDependencies += "org.apache.kafka" % "kafka-streams-scala_2.11" % "5.4.1-ccs"
libraryDependencies += "io.confluent" % "kafka-streams-avro-serde" % "5.4.1"

//TiDB driver
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.24"

//SerDes Libraries
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.0"