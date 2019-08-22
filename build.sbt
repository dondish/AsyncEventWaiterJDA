name := "AsyncEventWaiterJDA"
organization := "org.dondish"
scalaVersion := "2.13.0"
version := "1.2.1"
crossScalaVersions := List("2.13.0", "2.12.7")
resolvers += "jcenter-bintray" at "https://jcenter.bintray.com"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "net.dv8tion" % "JDA" % "3.8.3_464"
)
bintrayOrganization := Some("dondishorg")
bintrayRepository := "oss-maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
