ThisBuild/scalaVersion := "3.3.0"

scalacOptions := Seq("-unchecked", "-deprecation")

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "org.kordamp.ikonli" % "ikonli-fluentui-pack" % "12.3.1"
libraryDependencies += "org.kordamp.ikonli" % "ikonli-swing" % "12.3.1"
libraryDependencies += "com.formdev" % "flatlaf" % "3.3"

Compile/doc/javacOptions ++= Seq(
  "-encoding",    "UTF-8", 
  "-charset",     "UTF-8", 
  "-docencoding", "UTF-8"
)