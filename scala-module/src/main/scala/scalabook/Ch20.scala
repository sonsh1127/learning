package scalabook

object Ch20 {
  def main(args: Array[String]): Unit = {
    func("helloWorld")
  }
  def func(greetings: String) {
    println(greetings)
  }
}

trait Abstract {
  type T
  def transform(x: T): T
  val initial: T
  var current: T
}

class Concrete extends Abstract {
  type T = String
  override def transform(x: String): String = x + x
  override val initial: String = "hi"
  override var current: String = initial
}
trait AbstractTime {
  val hour: Int
  val minute: Int
}
trait AbstractTime2 {
  def hour: Int
  def hour_=(x: Int)
  def minute: Int
  def minute_=(x: Int)
}

trait RationalTrait {
  val numerArg: Int
  val denomArg: Int
  require(denomArg != 0)

  private val g = gcd(numerArg, denomArg)

  val numer = numerArg / g
  val denom = denomArg / g
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  override def toString = s"RationalTrait($numerArg, $denomArg, $g, $numer, $denom)"
}

object OccursErrorDueToInitializeTiming {
  def main(args: Array[String]): Unit = {
    earlyInit
    val aa = new LazyRationalTrait {
      val denomArg: Int = 12
      val numerArg: Int = 14
    }
    println(aa)

    val a = new RationalTrait {
      val denomArg: Int = 12
      val numerArg: Int = 14
    }
    println(a.denomArg)
  }

  private def earlyInit() = {
    val rational = new {
      val numerArg = 12
      val denomArg = 14
    } with RationalTrait

    println(rational)
    println("early init success")
  }
}
object twoThird extends {
  val numerArg = 2
  val denomArg = 3
} with RationalTrait

class RationalClass(n: Int, d: Int) extends {
  val numerArg  = n
  val denomArg = d
} with RationalTrait

class ShellRationalTrait extends RationalTrait{
  val numerArg = 1
  override val denomArg: Int = 3
}

object Demo {
  val x = { println("initializing x"); "done"}
}

object LazyDemo {
  lazy val x = { println("initializing LazyDemo.x"); "done"}
}
object DemoRunner extends App {
  Demo
  LazyDemo
  println("LazyDemo ref")
  LazyDemo.x
  LazyDemo.x

}
trait LazyRationalTrait {
  val numerArg: Int
  val denomArg: Int

  private lazy val g = {
    require(denomArg != 0)
    gcd(numerArg, denomArg)
  }
  lazy val numer = numerArg / g
  lazy val denom = denomArg / g
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  override def toString = s"RationalTrait($numerArg, $denomArg, $g, $numer, $denom)"
}

class Food
class Grass extends Food

abstract class Animal {
  type SuitableFood <: Food
  def eat(food: SuitableFood)
}

class Cow extends Animal {
  override type SuitableFood = Grass
  override def eat(food: SuitableFood): Unit = {
  }
}

class Pasture {
  var animals: List[Animal{type SuitableFood = Grass}] = Nil
}

object Color extends Enumeration {
  val Red = Value
  val Green = Value
  val Blue = Value("B")
}

object EnumTest extends App {
  Color.values.foreach(println(_))
}


