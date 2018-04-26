package scalabook

object ListApiLearningTest extends App{

  val oneTwo = List(1, 2)
  val threeFour = List(3, 4)

  val oneToFour = oneTwo ::: threeFour
  print("list oneTwo was not mutated " + oneTwo)

  //specail cons operator
  //method name start with colon right operator's method will be invoked (oneTwo.::(1))
  val oneTwoThree = 1 :: oneTwo
  println(oneTwoThree)

  // in scala, empty list is called Nil

  val oneToFour2 = 1 :: 2 :: 3 :: 4 :: Nil
  //
  val thrill = "Will" :: "fill" :: "until" :: "nills" :: Nil
  println(thrill)
  println(thrill(2))
  println(thrill.count(s => s.length == 4))
  // return drop
  println(thrill.drop(2))
  // return dropRight

  print(thrill.dropRight(2))
  print(thrill.exists(s => s == "until"))
  print(thrill.filter(s => s.length == 4))
  print(thrill.forall(s => s.endsWith("l")))
  print("HEAD: " +thrill.head)
  print("LAST: " +thrill.last)
  print(thrill.tail)
  print(thrill)
  thrill.foreach(print)
  print(thrill.map(s => s+ "}"))

  def toCamelCase(str: String) = {
  }
}
