package com.github.zachalbia.fgcj.solutions

import com.github.zachalbia.fgcj.solutions.CommonParsers._
import fastparse.all._
import fastparse.core.Parser
import fs2.Stream

import scala.language.higherKinds

final class StoreCredit[F[_]] extends Problem[F] {
  val toSolution: Pipe[F] = { lines =>
    val linesMin1 = lines.drop(1)
    val grouping = 3
    groupBy(linesMin1, grouping).map(_._1.mkString("\n"))
      .map(StoreCredit.solve).zipWithIndex
      .map {
        case (Some((a, b)), i) => s"Case #${i + 1}: $a $b"
        case _ => sys.error("Impossible!")
      }
  }

  private def groupBy(linesMin1: Stream[F, String], grouping: Int) = {
    linesMin1.sliding(grouping).zipWithIndex
      .filter(_._2 % grouping == 0)
  }
}

object StoreCredit {
  def apply[F[_]] = new StoreCredit[F]

  private def solve(`case`: String): Parser.Solution =
    Parser.solution.parse(`case`).get.value

  private object Parser {
    type FirstIndex = Int
    type SecondIndex = Int
    type Solution = Option[(FirstIndex, SecondIndex)]

    val numCases, credit, numItems = oneNumLine

    def prices(n: Int) = P( number.rep(exactly=n, sep=" ") ~ "\n".? )

    val rawCase = P(credit ~/ (for (i <- numItems; ps <- prices(i)) yield ps))

    val solution: Parser[Solution, Char, String] =
      rawCase.map{case (c, prices) => Case(c, prices)}.map(_.solve)
  }

  private case class Case(credit: Int, prices: Seq[Int]) {
    def solve: Option[(Int, Int)] = {
      val pricesI = prices.zipWithIndex.map { case (a, i) => (a, i + 1) }
      Some((for {
        a <- pricesI
        b <- pricesI if bothSumToCredit((a, b))
      } yield (a._2, b._2)).head)
    }

    private def bothSumToCredit(ab: ((Int, Int), (Int, Int))): Boolean = ab match {
      case ((x, i), (y, j)) => i != j && x + y == credit
    }
  }
}
