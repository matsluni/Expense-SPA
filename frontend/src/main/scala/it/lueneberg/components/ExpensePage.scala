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

import cats.data.{ NonEmptyList, Validated }
import it.lueneberg.ExpensesSPAMain.Loc
import it.lueneberg.service.ExpenseService
import japgolly.scalajs.react.{ BackendScope, ReactComponentB, ReactElement }
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react._
import it.lueneberg.Api._
import it.lueneberg.utils.Bootstrap.{ Button, CommonStyle }
import it.lueneberg.Validations._
import it.lueneberg.service.ExpenseService._

import scala.scalajs.js
import scala.scalajs.js.Date

object ExpensePage {

  case class Props(router: RouterCtl[Loc])

  case class State(expense: Expense = Expense(), validationErrors: Seq[ValidationError] = Nil)

  private class Backend($: BackendScope[Props, State]) {

    def submitForm(state: State): Callback = {
      val validateExpense: Validated[NonEmptyList[ValidationError], Expense] = state.expense.validate
      if (validateExpense.isValid) {
        Callback { ExpenseService.postExpense(state.expense) } >>
          { $.setState(state.copy(validationErrors = Nil, expense = Expense())) }
      } else {
        import cats.std.all.listInstance
        $.setState(state.copy(validationErrors = validateExpense.toEither.left.get.unwrap))
      }
    }

    def updateDescription(e: ReactEventI) = {
      val text = e.target.value
      $.modState(s => s.copy(expense = s.expense.copy(description = text)))
    }

    def updateAmount(e: ReactEventI) = {
      val amount = BigDecimal(e.target.value)
      $.modState(s => s.copy(expense = s.expense.copy(amount = amount)))
    }

    def updateDate(e: ReactEventI) = {
      val date = Date.parse(e.target.value).toLong
      $.modState(s => s.copy(expense = s.expense.copy(date = date)))
    }

    def updateCategory(e: ReactEventI) = {
      val category = e.target.value
      $.modState(s => s.copy(expense = s.expense.copy(category = category)))
    }

    def render(p: Props, s: State) = {
      <.div(
        ^.className := "panel panel-default",
        <.div(
          ^.className := "panel-body",
          <.form(
            <.div(
              ^.className := s.validationErrors.find(x => x.fieldname == "category").fold("form-group")(x => "form-group, has-error"),
              <.label(^.htmlFor := "category", "category"),
              <.select(^.className := "form-control", ^.ref := "category", ^.onChange ==> updateCategory, ^.value := s.expense.category,
                <.option(^.value := "", "Select..."),
                <.option(^.value := "Food", "Food"),
                <.option(^.value := "Clothes", "Clothes"),
                <.option(^.value := "Presents", "Presents"))
            ),
            <.div(
              ^.className := s.validationErrors.find(x => x.fieldname == "amount").fold("form-group")(x => "form-group, has-error"),
              <.label(^.htmlFor := "amount", "Amount"),
              <.input(
                ^.className := "form-control col-sm-2 col-xs-12",
                ^.tpe := "number", ^.step := "0.01", ^.min := "0", ^.inputMode := "numeric",
                ^.id := "amount",
                ^.ref := "amount",
                ^.value := (if (s.expense.amount == 0) "" else s.expense.amount.toString()),
                ^.onChange ==> updateAmount
              )
            ),
            <.div(
              ^.className := s.validationErrors.find(x => x.fieldname == "description").fold("form-group")(x => "form-group, has-error"),
              <.label(^.htmlFor := "description", "description"),
              <.input(
                ^.className := "form-control col-sm-2 col-xs-12",
                ^.tpe := "text",
                ^.id := "description",
                ^.ref := "description",
                ^.value := s.expense.description,
                ^.onChange ==> updateDescription
              )
            ),
            <.div(
              ^.className := s.validationErrors.find(x => x.fieldname == "expenseDate").fold("form-group")(x => "form-group, has-error"),
              <.label(^.htmlFor := "expenseDate", "Date"),
              <.input(
                ^.className := "form-control col-sm-2 col-xs-12 input-min-width-95p",
                ^.tpe := "date",
                ^.id := "expenseDate",
                ^.ref := "expenseDate",
                ^.value := (if (s.expense.date == 0) "" else getDateString(toDate(s.expense.date))),
                ^.onChange ==> updateDate
              )
            ),
            <.div(
              ^.className := "form-group col-sm-4 col-sm-offset-4",
              Button(Button.Props(submitForm(s), CommonStyle.success), "Submit")
            )
          )
        )
      )
    }
  }

  private val component = ReactComponentB[Props]("ExpensePage")
    .initialState(State()) // initial state
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[Loc]): ReactElement =
    component(Props(ctl))

}
