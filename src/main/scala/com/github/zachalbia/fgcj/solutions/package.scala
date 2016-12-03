package com.github.zachalbia.fgcj

import fastparse.all._
import fs2.Stream

import scala.language.higherKinds

package object solutions {
  final type Pipe[F[_]] = Stream[F, String] => Stream[F, String]
  final type Sink[F[_]] = Stream[F, String] => F[Unit]

  object CommonParsers {
    val number: P[Int] = P(CharIn('0' to '9').rep(1).!.map(_.toInt))
    val oneNumLine: P[Int] = number ~ "\n"
  }
}
