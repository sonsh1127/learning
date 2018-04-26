package scalabook

/**
  * for comprehension
  */
object Ch23 {
  def main(args: Array[String]): Unit = {
    val lara = MyPerson("Lara", false)
    val bob = MyPerson("Bob", true)
    val julie = MyPerson("Julie", false, lara, bob)
    val persons = List(lara, bob, julie)

    val res = persons.filter(p => !p.isMail)
      .flatMap(p => p.children.map(child => (p.name, child.name)))
    println(res)

    val res2 = for (p <- persons; if !p.isMail; c <- p.children) yield
      (p.name, c.name)
    println(res2)

    val newRes = for (p <- persons; if p.name startsWith ("K")) yield p
    println(newRes)

    for (x <- List(1, 2); y <- List("One", "Two"))
      println((x, y))

    val withFilter = List("a", "b", "c").withFilter(_ == "b")
    println("withFilter : " + withFilter)
  }

  case class MyPerson(name: String, isMail: Boolean, children: MyPerson*)
  type Path = List[(Int, Int)]
  def inCheck(q: (Int, Int), queen: (Int, Int)): Boolean
  = q._1 == queen._1 || q._2 == queen._2 || (q._1 - queen._1).abs == (q._2 - queen._2)

  def isSafe(queen: (Int, Int), queens: Path) = queens.forall(q => !inCheck(q, queen))

  def nQueens(n: Int): List[Path] = {
    def placeQueens(k: Int): List[Path] = {
      if (k == 0) {
        List(List())
      } else {
        for {
          queens <- placeQueens(k - 1)
          column <- 1 to n
          queen = (k, column)
          if isSafe(queen, queens)
        } yield queen :: queens
      }
    }

    placeQueens(n)
  }

}
