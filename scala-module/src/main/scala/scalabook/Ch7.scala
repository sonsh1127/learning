package scalabook

import java.io.File
import java.nio.file.{Files, Path, Paths}

object Ch7 {
  def main(args: Array[String]): Unit = {
    println("hello world")

    // nested for loop
    for (i <- 1 to 5; j <- 1 to 3)
      println(s"""i={${i} and j = ${j} """)

    // in scala if statement is expression so ternary operator is unncessary in scala
    val test = 3
    val res = if (test == 3) true else false

    // to is upper inclusive
    println("to for loop")
    for (i <- 1 to 3)
      println(i)

    // until is upper exclusive blah blah

    println("until for loop")
    // <- syntax is referred as generator

    for (i <- 1 until 3)
      println(i)

    // for loop with filtering
    println("for loop with filtering ")
    for (i <- 1 to 10 if i % 5 != 0)
      println(i)

    println("for loop with multiple filter")

    for (i <- 1 to 10
         if i % 5 != 0
         if i % 3 != 0
    )
      println(i)

    // we dont need to forc checkedException
    val currentPath: Path = Paths.get("./")
    println(currentPath.toAbsolutePath)
    val thisLines = fileLines(new java.io.File("scala-module/src/scalabook/Ch7.scala"))
    println(thisLines.size)

    // making new collection using

    val currentFile = new File("./")

    val lines = for (file <- currentFile.listFiles()) yield file.length()
    lines.foreach(println)

    val i = 10
    // try catch expression
    val half  = if (i % 2 ==0) i / 2 else throw new IllegalArgumentException

    println(half)


    // scala scope sample


    val a =10;

    {
      val a =20
      println(a)

    }
    println(a)

  }

  def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines().toList

  def makeRow(row: Int) =  {
    val rows = for (col <- 1 to 10) yield {
      val prod = (row * col).toString
      val padd = " " * (4 - prod.length)
      padd + prod
    }
    rows.mkString(" ")

  }

  def multiTable() = {
    val tables = for (row <- 1 to 10) yield makeRow(row)
    tables.mkString("\n")
  }



}


