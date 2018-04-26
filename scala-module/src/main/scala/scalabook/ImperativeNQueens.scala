package scalabook

trait NQueens {

}

class ImperativeNQueens(val size: Int) extends NQueens {

  val chess = Array.ofDim[Int](size)
  val Empty = -1
  init

  def isPossible(level: Int, i: Int): Boolean = {
    for (j <- 0 until level) {
      if (chess(j) == i
        || (level - j) == Math.abs(i - chess(j))) {
        return false
      }
    }
    true
  }

  private def init() {
    for (i <- 0 until size)
      chess(i) = Empty
  }

  def printSolutions() {
    def loop(level: Int) {
      if (level == size) {
        printResult
      } else {
        for (i <- 0 until size) {
          if (isPossible(level, i)) {
            chess(level) = i
            loop(level + 1)
            chess(level) = Empty
          }
        }
      }
    }

    loop(0)
  }

  private def printResult = {
    println(chess.mkString("#", ",", "#"))
  }
}

class FunctionalNQueens extends NQueens {


}

object NQueensTest {
  def main(args: Array[String]): Unit = {
    val nQueens = new ImperativeNQueens(5)
    nQueens.printSolutions()
  }
}
