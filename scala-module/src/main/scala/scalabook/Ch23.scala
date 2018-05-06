package scalabook

/**
  * for comprehension
  */
object Ch23 {
  /*def main(args: Array[String]): Unit = {
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
*/

  val books =
    List (
      MyBook(
        "Structure",
        "Susman",
        "Nills"
      ),
      MyBook(
        "Principles of Compiler Design",
        "Aho Alfred",
        "Ullman"
      ),
      MyBook(
        "Programming in Modular",
        "Gosling"
      )
    )

  def main(args: Array[String]): Unit = {
    forBasic

    //nQueensTest


    val goslingBooks = for (b <- books; if b.authors.exists(_.startsWith("Gosling"))) yield b
    val goslingBooks2 = for (b <- books withFilter (b => b.authors.exists(_.startsWith("Gosling")))) yield b
    goslingBooks.foreach(println _)


    query

    // for comprehesion conversion
    // all for expression can be represented using three high order function like map, flatMap, withFilter


  }

  private def query = {
    for (b1 <- books; b2 <- books if (b1 != b2);
         a1 <- b1.authors; a2 <- b2.authors if (a1 == a2)) yield a1
  }

  private def forBasic = {
    val lara = MyPerson("Lara", false)
    val bob = MyPerson("Bob", true)
    val julie = MyPerson("Julie", false, lara, bob)
    val persons = List(lara, bob, julie)

    persons.filter(!_.isMail).flatMap(p => p.children.map(children => (p.name, children.name)))
      .foreach(println _)

    // more easy to understand version
    val pair = for (p <- persons; if !p.isMail; c <- p.children) yield (p.name, c.name)
    pair.foreach(println _)

    val names = for (p <- persons; n = p.name; if (n.startsWith("L"))) yield n
    names.foreach(println _)
  }

  private def nQueensTest = {
    val solutions = nQueens(8)
    printSolutions(solutions)
  }

  case class MyPerson(name: String, isMail: Boolean, children: MyPerson*)


  def isSafe(queens: List[(Int, Int)], queen: (Int, Int)): Boolean = {
    queens.forall(q => queen._2 != q._2 && (queen._1 - q._1).abs != (queen._2 - q._2))

  }

  def nQueens(n: Int): List[List[(Int, Int)]] = {
    def placeQueens(k: Int): List[List[(Int, Int)]] = {
      if (k == 0)
        List(List())
      else
        for {
          queens <- placeQueens(k - 1)
          column <- 1 to n
          queen = (k, column)
          if isSafe(queens, queen)
        } yield queen :: queens
    }

    placeQueens(n)
  }

  def printSolutions(tables: List[List[(Int, Int)]]) {

    def printSolution(table: List[(Int, Int)]) {
      val len = table.head._1
      println(s"len = $len")
      println("_" * (len * 2))

      for {
        dim <- table.reverse
        col <- 1 to len
        pipe = if (col == 1) "|" else ""
        cell = if (col == dim._2) "Q|" else "_|"
        nl = if (col == len) "\n" else ""
      } print(pipe + cell + nl)

    }

    for (table <- tables) printSolution(table)
  }

  case class MyBook(title: String, authors: String*)


  class Nills extends Traversable[String] {
    override def foreach[U](f: String => U): Unit = ???
  }



}
