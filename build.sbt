name := "AsyncEventWaiterJDA"
organization := "org.dondish"
scalaVersion := "2.12.7"
version := "1.0.1"
resolvers += "jcenter-bintray" at "https://jcenter.bintray.com"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "net.dv8tion" % "JDA" % "3.8.1_439"
)
bintrayOrganization := Some("dondishorg")
bintrayRepository := "oss-maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
