package com.github.zachalbia.fgcj

import com.github.zachalbia.fgcj.solutions.Problem
import fs2.Task

import scalaz.\/

package object runner {
  final type ErrProb = Throwable \/ Problem[Task]
}
