package com.github.zachalbia.fgcj

import org.scalacheck.Gen

import scala.util.matching.Regex

/**
  * Created by Zach on 2016-12-04.
  */
object TestingCommons {
  def oneToMaxOf[A](n: Int, a: Gen[A]): Gen[List[A]] = for {
    n <- Gen.choose(1, n)
    l <- Gen.listOfN(n, a)
  } yield l

  val resultRegex: Regex = """Case #\d+: (.*)""".r

  val getCaseBody: String => String =
    resultRegex.findFirstMatchIn(_).get.group(1)
}
