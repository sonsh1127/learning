package learning

abstract class DefValTest {

  def receive(x: Int)

  val r: Int
}

class Sub extends DefValTest {
  override val r: Int = 3
  override def receive(x: Int): Unit = println(x)
}
