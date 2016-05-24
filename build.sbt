import com.typesafe.sbt.web.Import.WebKeys._

lazy val frontendRef = LocalProject("frontend")
val copyWebJarResources = taskKey[Unit]("Copy resources from WebJars")

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

lazy val backend = (project in(file("backend")))
  .enablePlugins(
    AutomateHeaderPlugin,
    GitVersioning,
    SbtWeb,
    SbtTwirl
  )
  .settings(Revolver.settings: _*)
  .settings(
    LessKeys.compress in Assets := true,
    libraryDependencies ++= Library.jvmDependencies.value,
    testOptions += Tests.Argument("-oDF"),
    parallelExecution in Test := false,
    logBuffered := false,
    (resourceGenerators in Compile) <+=
      (fastOptJS in Compile in frontendRef, packageScalaJSLauncher in Compile in frontendRef, packageJSDependencies in Compile in frontendRef)
        .map((f1, f2, f3) => Seq(f1.data, f2.data, f3.get.head)),

    copyWebJarResources := {
      // copy the compiled CSS
      val s = streams.value
      s.log("Copying webjar resources")
      val compiledCss = webTarget.value / "less" / "main" / "stylesheets"
      val targetDir = (classDirectory in Compile).value / "web"
      IO.createDirectory(targetDir / "stylesheets")
      IO.copyDirectory(compiledCss, targetDir / "stylesheets")
    },
    copyWebJarResources <<= copyWebJarResources dependsOn(compile in Compile, assets in Compile),
    managedResources in Compile <<= (managedResources in Compile) dependsOn copyWebJarResources
  )
  .dependsOn(sharedJVM)

lazy val frontend = (project in(file("frontend")))
  .enablePlugins(
    AutomateHeaderPlugin,
    GitVersioning,
    ScalaJSPlugin)
  .settings(
    artifactPath in fastOptJS := (resourceManaged in backend in Compile).value / ((moduleName in fastOptJS).value),
    artifactPath in fullOptJS := (resourceManaged in backend in Compile).value / ((moduleName in fullOptJS).value),
    libraryDependencies ++= Library.scalajsDependencies.value,
    jsDependencies ++= Library.jsDependencies.value,
    skip in packageJSDependencies := false,
    skip in packageMinifiedJSDependencies := false,
    persistLauncher := true,
    persistLauncher in Test := false,
    testFrameworks += new TestFramework("utest.runner.Framework"))
  .dependsOn(sharedJS)

lazy val shared = (crossProject.crossType(CrossType.Pure))
  .in(file("shared"))
  .enablePlugins(
    AutomateHeaderPlugin,
    GitVersioning
  )
  .settings(
    scalaVersion := Version.Scala,
    libraryDependencies ++= Library.sharedDependencies.value
  )

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")
lazy val sharedJS = shared.js.settings(name := "sharedJS")

name := "expense-spa"

initialCommands := """|import it.lueneberg.expense._
                      |""".stripMargin
