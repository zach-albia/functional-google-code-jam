package com.github.zachalbia.fgcj.runner

import java.nio.file.Paths

import com.github.zachalbia.fgcj.solutions.{Problem, Sink}
import fs2.{Stream, Task, io, text}

trait Runner {
  private final val ChunkSize = 4096
  protected val problem: Problem[Task]

  def main(args: Array[String]): Unit =
    pureMain(args).unsafeRun()

  private def pureMain(args: Array[String]): Task[Unit] =
    solve(args(0), args(1))

  private def solve(in: String, out: String): Task[Unit] =
    (toUtf8Lines andThen problem.toSolution andThen writeToFile(out))(in)

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
