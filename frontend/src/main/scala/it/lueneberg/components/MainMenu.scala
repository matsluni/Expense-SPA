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

import it.lueneberg.ExpensesSPAMain.{ AboutLoc, ExpenseListLoc, ExpenseLoc, Loc }
import it.lueneberg.utils.{ BootstrapStyles, GlobalStyles }
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object MainMenu {

  private def bss: BootstrapStyles = GlobalStyles.bootstrapStyles
  case class Props(router: RouterCtl[Loc], currentLoc: Loc)
  private case class MenuItem(idx: Int, label: (Props) => ReactNode, location: Loc)

  private val menuItems: Props => Seq[MenuItem] = props => {

    val privatePages = Seq(
      MenuItem(1, _ => "About", AboutLoc),
      MenuItem(2, _ => "Add", ExpenseLoc),
      MenuItem(3, _ => "List", ExpenseListLoc)
    )
    privatePages
  }

  private class Backend($: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      <.ul(bss.navbar)(
        // build a list of menu items
        for (item <- menuItems(props)) yield {
          <.li(^.key := item.idx, (props.currentLoc == item.location) ?= (^.className := "active"),
            props.router.link(item.location)(" ", item.label(props)))
        }
      )
    }
  }

  private val component = ReactComponentB[Props]("MainMenu")
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[Loc], currentLoc: Loc): ReactElement =
    component(Props(ctl, currentLoc))
}
