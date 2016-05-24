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

package it.lueneberg.utils

import org.scalajs.dom._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

/**
 * Minimal facade for JQuery. Use https://github.com/scala-js/scala-js-jquery or
 * https://github.com/jducoeur/jquery-facade for more complete one.
 */
@js.native
trait JQueryEventObject extends Event {
  var data: js.Any = js.native
}

@js.native
@JSName("jQuery")
object JQueryStatic extends js.Object {
  def apply(element: Element): JQuery = js.native
}

@js.native
trait JQuery extends js.Object {
  def on(events: String, selector: js.Any, data: js.Any, handler: js.Function1[JQueryEventObject, js.Any]): JQuery = js.native
  def off(events: String): JQuery = js.native
}
