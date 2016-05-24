/*
 * Copyright 2016 Matthias Lüneberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.lueneberg

import it.lueneberg.components.{ About, ExpensePage, ExpensesList, MainMenu }
import it.lueneberg.utils.GlobalStyles
import japgolly.scalajs.react.extra.router.{ BaseUrl, Router, _ }
import org.scalajs.dom.document
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object ExpensesSPAMain extends JSApp {

  // Define the locations (pages) used in this application
  sealed trait Loc
  case object AboutLoc extends Loc
  case object ExpenseLoc extends Loc
  case object ExpenseListLoc extends Loc

  // configure the router
  val routerConfig = RouterConfigDsl[Loc].buildConfig { dsl =>
    import dsl._

    val allPages =
      (
        (staticRoute("#about", AboutLoc) ~> renderR(ctl => (About(ctl))))
        | (staticRoute("#expense", ExpenseLoc) ~> renderR(ctl => ExpensePage(ctl)))
        | (staticRoute("#expenseList", ExpenseListLoc) ~> renderR(ctl => AppCircuit.connect(_.expenses)(ExpensesList(ctl, _))))
      ).notFound(redirectToPage(AboutLoc)(Redirect.Replace))

    allPages.renderWith(layout)
  }

  // base layout for all pages
  def layout(c: RouterCtl[Loc], r: Resolution[Loc]) = {
    <.div(
      // here we use plain Bootstrap class names as these are specific to the top level layout defined here
      <.nav(
        ^.className := "navbar navbar-inverse navbar-fixed-top",
        <.div(
          ^.className := "container",
          <.div(^.className := "navbar-header", <.span(^.className := "navbar-brand", "Expenses")),
          <.div(
            ^.className := "collapse navbar-collapse",
            // connect menu to model, because it needs to update when the number of open todos changes
            MainMenu(c, r.page)
          )
        )
      ),
      // currently active module is shown in this container
      <.div(^.className := "container", r.render())
    )
  }

  def main(): Unit = {
    println("Application starting")

    // create stylesheet
    GlobalStyles.addToDocument()

    //    AppCircuit.dispatch(InitialAuth)

    // create the router
    val router: Router[Loc] = Router(BaseUrl.until_#, routerConfig)
    // tell React to render the router in the document body
    ReactDOM.render(router(), dom.document.getElementById("root"))
  }

}

