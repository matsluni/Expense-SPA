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

package it

//import cats.data.Xor
//import io.circe.{ Decoder, DecodingFailure, Encoder, Json }
import it.lueneberg.utils.JQueryStatic

//import scala.scalajs.js.{ Date => JsDate }

package object lueneberg {
  // expose jQuery under a more familiar name
  val jQuery = JQueryStatic

  //  // from io.circe#circe-java8
  //  final def decodeJsDate(): Decoder[JsDate] =
  //    Decoder.instance { c =>
  //      c.as[String].flatMap { s =>
  //        try Xor.right(new JsDate(JsDate.parse(s))) catch {
  //          case _: Exception => Xor.left(DecodingFailure("JsDate", c.history))
  //        }
  //      }
  //    }
  //
  //  final def encodeJsDate(): Encoder[JsDate] =
  //    Encoder.instance(d => Json.string(d.toISOString()))
  //
  //  implicit final val decodeDateDefault: Decoder[JsDate] =
  //    decodeJsDate()
  //
  //  implicit final val encodeDateDefault: Encoder[JsDate] =
  //    encodeJsDate()

}
