package com.github.zachalbia.fgcj.solutions.africa2010.qualis

import com.github.zachalbia.fgcj.TestingCommons._
import fs2.Stream
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

import scala.util.Random

object StoreCreditSpec extends Properties("StoreCredit") {
  case class Case(credit: Int, prices: List[Int], solution: (Int, Int)) {
    val lines: String = s"$credit\n${prices.size}\n${prices.mkString(" ")}\n"
    val solutionIndices: (Int, Int) = {
      val indices = List(prices.indexOf(solution._1), prices.lastIndexOf(solution._2)).sorted
      (indices(0) + 1, indices(1) + 1)
    }
  }

  def genNonSolutions(credit: Int, size: Int, ns: List[Int]): Gen[List[Int]] = for {
    num   <- Gen.choose(1, 1000) suchThat { n => ns.forall(n + _ != credit) }
    genNs <- if (ns.size == size) Gen.const(ns) else genNonSolutions(credit, size, num :: ns)
  } yield genNs

  val case_ : Gen[Case] = for {
    credit <- Gen.choose(2, 500)
    first  <- Gen.choose(1, credit - 1)
    second = credit - first
    maxSize       <- Gen.choose(2, 20)
    nonSolutions  <- genNonSolutions(credit, maxSize, List(first, second))
    prices = Random.shuffle(nonSolutions)
  } yield Case(credit, prices, (first, second))

  val cases: Gen[(List[(Int, Int)], List[String])] = oneToMaxOf(20, case_).map(cs =>
    (cs.map(_.solutionIndices), cs.map(_.lines)))

  property("toSolution") = forAll(cases) { case (solutions, lines) =>
    val stream = Stream("\n") ++ Stream.emits(lines)
    try {
      val result = new StoreCredit[Nothing].toSolution(stream).map(
        getCaseBody(_).split(' ').map(_.toInt))
      val actual = result.map(pair => (pair(0), pair(1))).toList
      solutions == actual
    } catch {
      case _: scala.Throwable => true
    }
  }
}
