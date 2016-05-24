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

import diode.data.Pot
import diode.react.ModelProxy
import it.lueneberg.Api.Expense
import it.lueneberg.ExpensesSPAMain.Loc
import it.lueneberg.RefreshExpenses
import it.lueneberg.service.ExpenseService._
import japgolly.scalajs.react.{ Callback, _ }
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._

object ExpensesList {

  case class Props(router: RouterCtl[Loc], proxy: ModelProxy[Pot[Seq[Expense]]])

  private class Backend($: BackendScope[Props, Unit]) {

    def mounted(props: Props): Callback = {
      Callback.lazily(props.proxy.dispatch(RefreshExpenses()))
    }

    def render(p: Props) =
      p.proxy.value.map { expenses =>
        <.div(
          <.h2("Expenses"),
          <.table(^.className := "table table-striped")(
            <.thead(
              <.tr(
                <.th(^.className := "hidden", "#"),
                <.th("Category"),
                <.th("Amount"),
                <.th("Description"),
                <.th("Date")
              )
            ),
            <.tbody(
              for (exp <- expenses) yield {
                <.tr(
                  <.td(^.className := "hidden", s"${exp.id}"),
                  <.td(s"${exp.category}"),
                  <.td(s"${exp.amount}"),
                  <.td(s"${exp.description}"),
                  <.td(s"${getDateString(toDate(exp.date))}")
                )
              }
            )
          )
        )
      }.getOrElse(<.div("No expenses"))
  }

  private val component = ReactComponentB[Props]("ExpensesList")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props)) // what to do, when the component gets mountet
    .build

  def apply(ctl: RouterCtl[Loc], proxy: ModelProxy[Pot[Seq[Expense]]]): ReactElement =
    component(Props(ctl, proxy))

}
