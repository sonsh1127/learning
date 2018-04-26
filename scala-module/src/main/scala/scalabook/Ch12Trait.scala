package scalabook

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

object Ch12Trait {

  def main(args: Array[String]): Unit = {
    val frog = new Frog
    frog.philosophize()
  }
}

trait Philosophical {
  def philosophize() {
    println("I consume memory, therefore I am.")
  }
}

trait TestOne {
  def test(): Boolean
}
class Frog extends Philosophical with TestOne{
  override def toString: String = "test"
  override def test() = false
}

abstract class IntQueue {
  def get(): Int
  def put(x: Int)
}

trait Doubling extends IntQueue {
  abstract override def put(x: Int){
    super.put(x * 2)
  }
}
trait Incrementing extends IntQueue {
  abstract override def put(x: Int) = super.put(x+1)
}
trait Filtering extends IntQueue {
  abstract override def put(x: Int) = if (x >= 0) super.put(x)
}

class BasicIntQueue extends IntQueue {
  private val queue = new ArrayBuffer[Int]()
  override def get(): Int = queue.remove(0)
  override def put(x: Int): Unit = queue += x
}

object TraitMixin{
  def main(args: Array[String]): Unit = {

    val queue = new BasicIntQueue with Filtering with Incrementing

    queue.put(-1)
    queue.put(0)
    println(queue.get())
    println(queue.get())

    val queue2 = new BasicIntQueue with Filtering
    queue2.put(10)
    queue2.put(20)


  }

}

object DecomposeType extends Enumeration{
  type DecomposeType = Value
  val CAWONSUK, GEM, GARU = Value


}

object ScalaRandomTest {
  def main(args: Array[String]): Unit = {
    val random = new Random()
    for (i <- 1 to 100) {
      println(random.nextInt(3))
    }
  }
}




