package com.github.zachalbia.fgcj.solutions.africa2010.qualis

import com.github.zachalbia.fgcj.TestingCommons._
import fs2.Stream
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

object ReverseWordsSpec extends Properties("ReverseWords") {
  val validWord = Gen.alphaLowerStr suchThat (_.nonEmpty)
  val validWords = oneToMaxOf(20, validWord)
  val caseLine = validWords.map(_.mkString(" "))
  val caseStream = oneToMaxOf(10, caseLine).map("\n" :: _).map(Stream.emits)

  property("toSolution") = forAll(caseStream) { s =>
    val solnStream = new ReverseWords[Nothing].toSolution(s)
    def reversed(line: String) = getCaseBody(line).split(' ')
    val cases = solnStream.map(reversed(_).reverse.mkString(" ")).toList
    cases == s.toList.drop(1)
  }
}
