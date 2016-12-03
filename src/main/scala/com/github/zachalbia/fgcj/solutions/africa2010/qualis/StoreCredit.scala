package com.github.zachalbia.fgcj.solutions.africa2010.qualis

import com.github.zachalbia.fgcj.runner.Runner
import com.github.zachalbia.fgcj.solutions.CommonParsers._
import com.github.zachalbia.fgcj.solutions.{Pipe, Problem}
import fastparse.all._
import fs2.Stream

import scala.language.higherKinds

final class StoreCredit[F[_]] extends Problem[F] {
  val toSolution: Pipe[F] = { lines =>
    groupBy(lines.drop(1), 3)
      .map { _.mkString("\n") }
      .map { StoreCredit.solve }.zipWithIndex
      .map { case ((a, b), i) => s"Case #${i + 1}: $a $b" }
  }

  private def groupBy(lines: Stream[F, String], size: Int) = {
    lines.sliding(size).zipWithIndex
      .filter(_._2 % size == 0).map(_._1)
  }
}

object StoreCredit extends Runner {
  val problem = new StoreCredit

  private def solve(`case`: String) =
    Parser.toCase.parse(`case`).get.value.solve

  private object Parser {
    val credit, numItems = oneNumLine
    def prices(n: Int) = P(number.rep(exactly = n, sep = " ") ~ "\n".?)
    val rawCase = P(Start ~ credit ~ (for { n <- numItems ; ps <- prices(n) } yield ps) ~ End)
    val toCase = rawCase.map { case (c, ps) => Case(c, ps) }
  }

  private case class Case(credit: Int, prices: Seq[Int]) {
    def solve: (Int, Int) = {
      val pricesI = prices.zipWithIndex.map { case (a, i) => (a, i + 1) }
      (for {
        a <- pricesI if a._1 <= credit
        b <- pricesI if bothSumToCredit((a, b))
      } yield (a._2, b._2)).head
    }

    private def bothSumToCredit(ab: ((Int, Int), (Int, Int))): Boolean = ab match {
      case ((x, i), (y, j)) => i != j && x + y == credit
    }
  }
}
