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

package it.lueneberg.components

import it.lueneberg.ExpensesSPAMain.Loc
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._

object About {

  case class Props(router: RouterCtl[Loc])

  // create the React component for Dashboard
  private val component = ReactComponentB[Props]("About")
    .render_P {
      case Props(router) =>
        <.div(
          <.h2("About"),
          <.div("This is a example of a Single Page App written in Scala and ScalaJS using React.")
        )
    }.build

  def apply(router: RouterCtl[Loc]) = component(Props(router))
}
