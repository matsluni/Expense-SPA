import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._


object Version {
  final val Scala             = "2.11.8"

  object JVM {
    final val Akka            = "2.4.3"
    final val AkkaHttpJson    = "1.6.0"
    final val TypesafeConfig  = "1.2.1"
    final val Slf4J           = "1.7.12"
    final val LogbackClassic  = "1.1.3"
    final val ScalaCheck      = "1.13.0"
  }

  object JS {
    final val scalaDom        = "0.9.0"
    final val scalajsReact    = "0.10.4"
    final val scalaCSS        = "0.3.1"
    final val React           = "0.14.7"
    final val diode           = "0.5.0"
    final val uTest           = "0.3.1"
  }

  object WebJar {
    final val Bootstrap       = "3.3.2"
    final val JQuery          = "1.11.1"
    final val FontAwesome     = "4.3.0-1"
  }

  object Shared {
    final val Cats            = "0.4.1"
    final val Circe           = "0.4.1"
  }
}

object Library {

  val sharedDependencies = Def.setting(Seq(
    "io.circe"                            %% "circe-core"             % Version.Shared.Circe,
    "io.circe"                            %% "circe-parser"           % Version.Shared.Circe,
    "io.circe"                            %% "circe-generic"          % Version.Shared.Circe
  ))

  val jvmDependencies = Def.setting(Seq(
    "com.typesafe.akka"                   %% "akka-http-experimental" % Version.JVM.Akka,
    "com.typesafe.akka"                   %% "akka-slf4j"             % Version.JVM.Akka,
    "de.heikoseeberger"                   %% "akka-http-circe"        % Version.JVM.AkkaHttpJson,
    "com.typesafe"                        %  "config"                 % Version.JVM.TypesafeConfig,
    "org.slf4j"                           %  "slf4j-api"              % Version.JVM.Slf4J,
    "ch.qos.logback"                      %  "logback-classic"        % Version.JVM.LogbackClassic,
    "org.webjars"                         %  "font-awesome"           % Version.WebJar.FontAwesome  % Provided,
    "org.webjars"                         %  "bootstrap"              % Version.WebJar.Bootstrap    % Provided,

    "org.scalatest"                       %% "scalatest"              % "2.2.5"                     % "test"
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
      "com.github.japgolly.scalajs-react" %%% "core"                  % Version.JS.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra"                 % Version.JS.scalajsReact,
      "com.github.japgolly.scalacss"      %%% "ext-react"             % Version.JS.scalaCSS,
      "me.chrons"                         %%% "diode"                 % Version.JS.diode,
      "me.chrons"                         %%% "diode-react"           % Version.JS.diode,
      "org.scala-js"                      %%% "scalajs-dom"           % Version.JS.scalaDom,
      "io.circe"                          %%% "circe-core"            % Version.Shared.Circe,
      "io.circe"                          %%% "circe-parser"          % Version.Shared.Circe,
      "io.circe"                          %%% "circe-generic"         % Version.Shared.Circe,
      "io.circe"                          %%% "circe-scalajs"         % Version.Shared.Circe,
      "com.lihaoyi"                       %%% "utest"                 % Version.JS.uTest            % "test"
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
    "org.webjars.bower" % "react"     % Version.JS.React          / "react-with-addons.js"  minified "react-with-addons.min.js"                                   commonJSName "React",
    "org.webjars.bower" % "react"     % Version.JS.React          / "react-dom.js"          minified "react-dom.min.js"         dependsOn "react-with-addons.js"  commonJSName "ReactDOM",
    "org.webjars"       % "jquery"    % Version.WebJar.JQuery     / "jquery.js"             minified "jquery.min.js",
    "org.webjars"       % "bootstrap" % Version.WebJar.Bootstrap  / "bootstrap.js"          minified "bootstrap.min.js"         dependsOn "jquery.js"
  ))

}
