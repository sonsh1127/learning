package scalabook

import java.io.{File, PrintWriter}

object Ch9 {
  def main(args: Array[String]): Unit = {
    val file = new File("./")

    withPrintWriter(file) {
      writer => writer.println("hello world")
    }
    myAssert2(5 > 3)
    val a = "hello world"
    val str = s"$a name"
    println(str)
  }

  // template method pattern
  def withPrintWriter(file: java.io.File)(op: PrintWriter => Unit) {
    val pw = new PrintWriter(file)
    try {
      op(pw)
    } finally {
      pw.close()
    }
  }

  def myAssert(predicate: () => Boolean) = {
    if (!predicate()) {
      throw new AssertionError()
    }
  }

  def myAssert2(predicate: => Boolean) = {
    if (!predicate) {
      throw new AssertionError()
    }
  }
}

object FileMatcher1 {
  private def filesHere = new File(".").listFiles()

  def filesEnding(query: String) =
    for (file <- filesHere; if (file.getName.endsWith(query)))
      yield file

  def filesContaing(query: String) =
    for (file <- filesHere; if (file.getName.contains(query)))
      yield file

  def filesRegex(query: String) =
    for (file <- filesHere; if (file.getName.matches(query)))
      yield file
}

object FileMatcher2 {
  private def filesHere = new File(".").listFiles()

  private def filesMatching(query: String, matcher: (String, String) => Boolean) =
    for (file <- filesHere; if (matcher(file.getName, query)))
      yield file

  def filesEnding(query: String) = filesMatching(query, _.endsWith(_))

  def filesContaing(query: String) = filesMatching(query, _.contains(_))

  def filesRegex(query: String) = filesMatching(query, _.matches(_))
}

object FileMatcher3 {
  private def filesHere = new File(".").listFiles()

  private def filesMatching(matcher: (String) => Boolean) =
    for (file <- filesHere; if (matcher(file.getName)))
      yield file

  def filesEnding(query: String) = filesMatching(_.endsWith(query))

  def filesContaing(query: String) = filesMatching(_.contains(query))

  def filesRegex(query: String) = filesMatching(_.matches(query))
}
