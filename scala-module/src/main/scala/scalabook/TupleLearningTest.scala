package scalabook

object TupleLearningTest extends App{
  val pair = ("hello", "world")
  // pair index start with 1 not zero

  println(pair._1)
  println(pair._2)
  println(pair)

  // SET Learning Test

  var jetSet = Set("Boeing", "AirBus")
  // jetSet reassigned
  jetSet += "Lear"
  println(jetSet.contains("Lear"))


  val mutableSet = scala.collection.mutable.Set("Hitch", "hiking")
  mutableSet += "nins"
  println(mutableSet)

  //explicit hash set

  val immutableHashSet = scala.collection.immutable.HashSet("nis", "cons")
  println(immutableHashSet + "cons2dsfs")


  // Map Learning Test
  val mutableMap = scala.collection.mutable.Map[Int, String]()
  mutableMap += (1 -> "goto island")
  mutableMap += (2 -> "find")
  mutableMap += (3 -> "dig")
  println()

  val members = List(Member("nins", List("A", "B")), Member("cons", List("c", "D")))


  val allNickNames = members.flatMap(_.nickNames)
  println(allNickNames)


}



case class Member (name: String, nickNames: List[String]) {

}





