package scalabook

import scala.io.Source

object Ch8 {

  def main(args: Array[String]): Unit = {

    // first class function

    var increase = (x: Int) => x + 1
    println(increase(11))


    val multiLineFunctionLiteral = (x: Int) => {
      println("Hello")
      println("world")
      x + 10
    }

    multiLineFunctionLiteral(3)

    val someNumbers = List(-11, 10, 3, 2, 7, 4)
    println(someNumbers.filter((x: Int) => x % 2 == 1))
    println(someNumbers.filter((x) => x % 2 == 1))
    println(someNumbers.filter(x => x % 2 == 1))
    // _ syntax
    println(someNumbers.filter(_ % 2 == 1))


    val f = (_: Int) + (_: Int)
    // partial apply function
    val a = sum(1, 2, _: Int)
    println(a(5))

    someNumbers.foreach(println _)
    someNumbers.foreach(println)


    println("closure test")
    // closure
    // in scala
    var more = 3

    val lamda = () => {

    }

    val closure = (x: Int) => x + more
    println(closure(2))
    println("after change free variable")
    more = 10
    println(closure(2))
    
    // tail recursion optimazation
    


  }

  def isGoodEnough(guess: Double): Boolean = ???

  def improve(guess: Double): Double = ???

  def approximate(guess: Double) : Double =
    if (isGoodEnough(guess)) guess
    else approximate(improve(guess))
  def sum(a: Int, b: Int, c: Int) = a + b + c

  def processFile(fileName: String, width: Int) = {
    // processLine is a local function

    def processLine(line: String) = {

    }

    val source = Source.fromFile(fileName)
    for (line <- source.getLines())
      processLine(line)
  }

}


