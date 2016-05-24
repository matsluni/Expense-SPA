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

package it.lueneberg.expense.api

import akka.http.scaladsl.server.Directives._
import it.lueneberg.expense.api.util.TwirlSupport

trait StaticRoute {
  val staticEndpoints =
    get {
      (pathSingleSlash | path("index.html")) {
        import TwirlSupport._
        complete(html.index.render())
      } ~
        encodeResponse {
          path("frontend-launcher.js")(getFromResource("frontend-launcher.js")) ~
            path("frontend-opt.js")(getFromResource("frontend-opt.js")) ~
            path("frontend-fastopt.js")(getFromResource("frontend-fastopt.js")) ~
            path("frontend-jsdeps.js")(getFromResource("frontend-jsdeps.js")) ~
            path("frontend-jsdeps.min.js")(getFromResource("frontend-jsdeps.min.js"))
        }
    } ~
      encodeResponse {
        // for static assets like css and webjars stuff
        getFromResourceDirectory("web")
      }
}
