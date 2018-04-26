package currency


class Bank {

  private val map = scala.collection.mutable.Map[(String, String), Int]()

  def addRate(from: String, to: String, rate: Int) = {
    map.put((from, to), rate)
  }

  def rate(from: String, to: String): Int = {
    if (from == to) 1
    else map((from, to))
  }

  def reduce(expression: Expression, to: String): Money = {
    expression.reduce(this, to)
  }
}
