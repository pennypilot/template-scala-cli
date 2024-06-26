package com.pennypilot.example

/** Example program that prints the contents of a directory (similar to ls
  * command)
  * @param args
  *   the command-line arguments of the program. If no argument supplied, prints
  *   the contents of the current working directory. Otherwise, prints the
  *   contents of the given directory.
  */
@main def example(args: String*): Unit =
  val path = args.headOption match
    case Some(p) => os.Path(p, os.pwd)
    case _       => os.pwd

  if os.isDir(path) then println(os.list(path).mkString(","))
  else System.err.println("Expected directory path as an input")
