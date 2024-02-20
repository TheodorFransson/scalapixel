ThisBuild/scalaVersion := "3.3.1"
ThisBuild/name := "ScalaPixel"

scalacOptions := Seq("-unchecked", "-deprecation")

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "org.kordamp.ikonli" % "ikonli-fluentui-pack" % "12.3.1"
libraryDependencies += "org.kordamp.ikonli" % "ikonli-swing" % "12.3.1"
libraryDependencies += "com.formdev" % "flatlaf" % "3.3"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test

Compile/doc/javacOptions ++= Seq(
  "-encoding",    "UTF-8", 
  "-charset",     "UTF-8", 
  "-docencoding", "UTF-8"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}