package com.github.zachalbia.fgcj.solutions.africa2010.qualis

import com.github.zachalbia.fgcj.TestingCommons._
import fs2.Stream
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

object T9SpellingSpec extends Properties("T9Spelling") {
  val t9Map = Map(
    '2' -> Vector("a", "b", "c"),
    '3' -> Vector("d", "e", "f"),
    '4' -> Vector("g", "h", "i"),
    '5' -> Vector("j", "k", "l"),
    '6' -> Vector("m", "n", "o"),
    '7' -> Vector("p", "q", "r", "s"),
    '8' -> Vector("t", "u", "v"),
    '9' -> Vector("w", "x", "y", "z"),
    '0' -> Vector(" "),
    ' ' -> Vector("")
  )

  val char: Gen[Char]             = Gen.frequency((26, Gen.alphaLowerChar), (1, Gen.const(' ')))
  val line: Gen[String]           = oneToMaxOf(50, char).map(_.mkString)
  val rawLines: Gen[List[String]] = oneToMaxOf(100, line)

  def parseT9(t9: String): String =
    t9.split("(?<=(.))(?!\\1)")
      .map(t9Ch => t9Map(t9Ch.head)(t9Ch.length - 1))
      .mkString

  property("toSolution") = forAll(rawLines) { lines =>
    val stream = Stream("dropped line\n") ++ Stream.emits(lines)
    val t9Strings = new T9Spelling[Nothing].toSolution(stream).toList
    val actual = t9Strings.map(getCaseBody andThen parseT9)
    actual == lines
  } :| "all lines"
}
