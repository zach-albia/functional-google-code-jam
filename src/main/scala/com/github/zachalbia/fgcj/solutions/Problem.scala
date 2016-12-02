package com.github.zachalbia.fgcj.solutions

import scala.language.higherKinds

trait Problem[F[_]] {
  val toSolution: Pipe[F]
}
