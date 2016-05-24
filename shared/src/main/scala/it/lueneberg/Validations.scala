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

import cats.data.Validated.{ invalidNel, valid }
import cats._, cats.std.all._
import cats.data.{ NonEmptyList => NEL, _ }
import cats.data._
import it.lueneberg.Api.Expense
import java.math.{ BigDecimal => JBig }

object Validations {

  sealed trait ValidationError {
    def fieldname: String
  }
  case class MustNotBeEmpty(fieldname: String) extends ValidationError
  case class MustBeGreaterThan(fieldname: String, minValue: BigDecimal) extends ValidationError

  def notEmptyValidation(e: String, fieldname: String): Validated[NEL[ValidationError], String] = {
    if (e.isEmpty) invalidNel(MustNotBeEmpty(fieldname)) else valid(e)
  }

  def mustBeGreaterThan(field: BigDecimal, fieldname: String, minValue: BigDecimal = JBig.ZERO): Validated[NEL[ValidationError], BigDecimal] = {
    if (field > minValue) valid(field) else invalidNel(MustBeGreaterThan(fieldname, minValue))
  }

  implicit class ExpenseValidationOps(expense: Expense) {
    def validate: Validated[NEL[ValidationError], Expense] = {
      import cats.syntax.cartesian._
      val err =
        notEmptyValidation(expense.description, "description") |@|
          mustBeGreaterThan(expense.amount, "amount") |@|
          notEmptyValidation(expense.category, "category")

      err.map { (desc, am, ca) => expense }
    }
  }

}
