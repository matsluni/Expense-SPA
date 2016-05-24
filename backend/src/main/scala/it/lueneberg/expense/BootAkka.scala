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

package it.lueneberg.expense

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import it.lueneberg.Api.{ Expense }
import it.lueneberg.expense.api.{ ExpenseRoute, StaticRoute }
import akka.http.scaladsl.server.Directives._

object BootAkka extends App with Config with StaticRoute with ExpenseRoute {

  implicit val system = ActorSystem("actorSystem")
  implicit val materializer = ActorMaterializer()
  implicit val executor = system.dispatcher

  val expenseDB = scala.collection.concurrent.TrieMap[Long, Expense]()

  val bindingFuture = Http().bindAndHandle(staticEndpoints ~ expenseRoute, "localhost", webPort)

}
