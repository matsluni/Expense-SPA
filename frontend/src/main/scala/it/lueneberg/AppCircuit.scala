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

import diode.{ ActionHandler, Circuit, ModelRW }
import diode._
import diode.data._
import diode.data.{ Empty, Pot, PotAction }
import diode.react.ReactConnector
import diode.util.RunAfterJS
import it.lueneberg.Api.Expense
import it.lueneberg.service.ExpenseService

import scala.concurrent.duration._
import scalajs.concurrent.JSExecutionContext.Implicits.queue

// Define the root of our application model
case class RootModel(expenses: Pot[Seq[Expense]])

case class RefreshExpenses(potResult: Pot[Seq[Expense]] = Empty) extends PotAction[Seq[Expense], RefreshExpenses] {
  def next(newResult: Pot[Seq[Expense]]) = RefreshExpenses(newResult)
}

class ExpensesHandler[M](modelRW: ModelRW[M, Pot[Seq[Expense]]]) extends ActionHandler(modelRW) {
  val updateEffect = RefreshExpenses().effect(ExpenseService.getExpenses())(exp => exp.valueOr(e => List[Expense]()))
  override def handle = {
    case action: RefreshExpenses => {
      action.handleWith(this, updateEffect)(PotAction.handler())
    }
  }
}

/**
 * AppCircuit provides the actual instance of the `RootModel` and all the action
 * handlers we need. Everything else comes from the `Circuit`
 */
object AppCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {

  // define initial value for the application model
  def initialModel = RootModel(Empty)

  // Application circuit
  override protected val actionHandler = combineHandlers(
    new ExpensesHandler(zoomRW(_.expenses)((m, v) => m.copy(expenses = v)))
  )

}
