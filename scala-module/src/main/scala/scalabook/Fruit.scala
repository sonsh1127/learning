package bobdelishts

abstract class Fruit(val name: String,
                     val color: String)

object Fruits {
  object Apple extends Fruit("Apple", "RED")
  object Orange extends Fruit("Orange", "orange")
  object Pair extends Fruit("pear", "yellow")

  val menu = List(Apple, Orange, Pair)
}

