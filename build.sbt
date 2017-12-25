name := "reactive_messenger_auth"
 
version := "1.0" 
      
lazy val `reactive_messenger_auth` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.1"
libraryDependencies += evolutions // evolutions
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      