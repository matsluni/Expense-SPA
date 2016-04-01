lazy val noIdea = project
  .copy(id = "no-idea")
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

name := "no-idea"

libraryDependencies ++= Vector(
  Library.scalaCheck % "test"
)

initialCommands := """|import it.lueneberg.no.idea._
                      |""".stripMargin
