package com.github.zachalbia.fgcj.runner

import com.github.zachalbia.fgcj.solutions.{Problem, StoreCredit}
import fs2.Task

import scalaz.{-\/, \/-}

object Solutions {
  def lookFor(problem: String): ErrProb = {
    solutionsMap.get(problem) match {
      case Some(solution) => \/-(solution)
      case None => -\/(new RuntimeException(s"No solution found for [$problem]"))
    }
  }

  private val solutions = Seq(
    StoreCredit
  )

  private val solutionsMap =
    solutions.map(_.apply[Task])
      .foldRight(Map.empty[String, Problem[Task]]) { (a, map) =>
        map + (a.getClass.getSimpleName -> a) }
}
