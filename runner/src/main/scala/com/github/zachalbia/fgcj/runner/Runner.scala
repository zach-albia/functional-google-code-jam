package com.github.zachalbia.fgcj.runner

import java.nio.file.Paths

import com.github.zachalbia.fgcj.runner.Solutions._
import com.github.zachalbia.fgcj.solutions.Sink
import fs2.{Stream, Task, io, text}

import scalaz.{-\/, \/-}

object Runner {
  final val ChunkSize = 4096

  def main(args: Array[String]): Unit =
    run(args(0), args(1), args(2)).unsafeRun()

  private def run(in: String, out: String, problem: String): Task[Unit] =
    lookFor(problem) match {
      case \/-(soln) => import soln._
        (toUtf8Lines andThen toSolution andThen writeToFile(out))(in)
      case -\/(err) => Task.now { println(err) }
    }

  private val toUtf8Lines: String => Stream[Task, String] = { path =>
    io.file.readAll[Task](Paths.get(path), ChunkSize)
      .through(text.utf8Decode)
      .through(text.lines)
  }

  private val writeToFile: String => Sink[Task] = { outputPath => solnStream =>
    solnStream.intersperse("\n")
      .through(text.utf8Encode)
      .through(io.file.writeAll(Paths.get(outputPath))).run
  }
}
