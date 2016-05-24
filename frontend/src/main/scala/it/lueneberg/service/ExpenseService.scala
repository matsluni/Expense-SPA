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

package it.lueneberg.service

import cats.data.Xor
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
import io.circe.syntax._
import it.lueneberg.Api._
import org.scalajs.dom._
import org.scalajs.dom.ext.{ Ajax, AjaxException }

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{ Failure, Success }

object ExpenseService {

  val baseUrl = "/api/expenses"

  def postExpense(expense: Expense) = {
    val eventualRequest: Future[XMLHttpRequest] = Ajax.post(baseUrl, expense.asJson.toString(), headers = Map("Content-Type" -> "application/json"))
    eventualRequest.onComplete {
      case Success(s) => println(s"Success: $s")
      case Failure(t) => println(s"Failure: $t")
    }
  }

  def getExpenses(): Future[Xor[Error, Seq[Expense]]] = {
    val eventualExpenses: Future[XMLHttpRequest] = Ajax.get(baseUrl)
    eventualExpenses.map { xhr => decode[Seq[Expense]](xhr.responseText) }
  }

  def getDateString(d: js.Date) = s"${d.getFullYear()}-${toDoubleDigit(d.getMonth() + 1)}-${toDoubleDigit(d.getDate())}"
  def toDate(l: Long) = new js.Date(l.toDouble)
  def toDoubleDigit(i: Int) = (if (i < 9) s"0$i" else s"$i")
}
