package com.github.zachalbia.fgcj.solutions.africa2010.qualis

import com.github.zachalbia.fgcj.runner.Runner
import fastparse.all._
import com.github.zachalbia.fgcj.solutions.{Pipe, Problem}

import scala.language.higherKinds

final class ReverseWords[F[_]] extends Problem[F] {
  import ReverseWords.Parser.words
  val toSolution: Pipe[F] = {
    _.drop(1).filter(!_.isEmpty).zipWithIndex.map { case (line, i) =>
      val reversedWords = words.parse(line).get.value.reverse.mkString(" ")
      s"Case #${i + 1}: $reversedWords"
    }
  }
}

object ReverseWords extends Runner {
  private object Parser {
    val word = P(CharIn(('a' to 'z') ++ ('A' to 'Z')).rep(min=1).!)
    val words = P(Start ~/ (word ~ " ".? ~ "\n".?).rep(1) ~ End)
  }

  protected val problem = new ReverseWords
}
