package com.github.zachalbia.fgcj.solutions.africa2010.qualis

import com.github.zachalbia.fgcj.runner.Runner
import com.github.zachalbia.fgcj.solutions.{Pipe, Problem}

import scala.language.higherKinds
import fastparse.all._
import fs2.Task

final class T9Spelling[F[_]] extends Problem[F] {
  val toSolution: Pipe[F] =
    _.drop(1).filter(_.nonEmpty).map(Parser.t9Msg.parse(_).get.value)
      .zipWithIndex
      .map { case (line, i) => s"Case #${i + 1}: $line" }
}

private case class Token(get: String) {
  def sameKeyAs(that: Token): Boolean =
    this.get.head == that.get.head
}

private[qualis] object Parser {
  val msgChar: Parser[String] = P(CharIn('a' to 'z', Seq(' ')).rep(exactly=1).!)
  val message: Parser[String] = P(Start ~ msgChar.rep(1).! ~ "\n".? ~ End)
  val t9Msg: Parser[String]   = P(message.map(t9))

  private def t9(s: String): String =
    s.map(T9Spelling.scheme(_))
      .foldLeft(Vector.empty[Token])((toks, tok) =>
        if (toks.nonEmpty && toks.last.sameKeyAs(tok)) toks :+ Token(" ") :+ tok
        else toks :+ tok
      ).map(_.get).mkString
}

object T9Spelling extends Runner {
  private[qualis] val scheme = Map(
    'a' -> Token("2"), 'b' -> Token("22"), 'c' -> Token("222"),
    'd' -> Token("3"), 'e' -> Token("33"), 'f' -> Token("333"),
    'g' -> Token("4"), 'h' -> Token("44"), 'i' -> Token("444"),
    'j' -> Token("5"), 'k' -> Token("55"), 'l' -> Token("555"),
    'm' -> Token("6"), 'n' -> Token("66"), 'o' -> Token("666"),
    'p' -> Token("7"), 'q' -> Token("77"), 'r' -> Token("777"), 's' -> Token("7777"),
    't' -> Token("8"), 'u' -> Token("88"), 'v' -> Token("888"),
    'w' -> Token("9"), 'x' -> Token("99"), 'y' -> Token("999"), 'z' -> Token("9999"),
    ' ' -> Token("0")
  )

  protected val problem: Problem[Task] = new T9Spelling[Task]
}
