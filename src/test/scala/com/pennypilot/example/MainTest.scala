package com.pennypilot.example

import scala.util.Properties

class MainTest extends munit.FunSuite:
  private def launcher =
    if Properties.isWin then "scala-cli.bat" else "scala-cli"

  test("Prints expected contents when given a directory path"):
    // prepare test directory
    val tempDir = os.temp.dir()

    // create files
    val expectedFiles = Seq("Ls", "Hello").map(tempDir / _)
    expectedFiles.foreach(os.write(_, "Hello"))

    // check
    val foundFiles = os.proc(launcher, ".", "--", tempDir).call().out.trim()

    expectedFiles
      .map(_.toString)
      .foreach: file =>
        assert(foundFiles.contains(file))

  test("Prints an error when path doesn't exist"):
    val fakeDir = os.Path("/a/bogus/path")

    assert:
      os.proc(launcher, ".", "--", fakeDir)
        .call(stderr = os.Pipe)
        .err
        .text()
        .contains("Expected directory path as an input")

  test("Prints an error when path exists, but isn't a directory"):
    val realDir = os.temp.dir()
    val file    = realDir / "aFile"
    os.write(file, "Hello")

    assert:
      os.proc(launcher, ".", "--", file)
        .call(stderr = os.Pipe)
        .err
        .text()
        .contains("Expected directory path as an input")
