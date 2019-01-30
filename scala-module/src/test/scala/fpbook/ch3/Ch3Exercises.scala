package fpbook.ch3

import org.junit.Assert._
import org.junit.{Assert, Test}

class Ch3Exercises {

  @Test
  def prob1_caseMatch() {
    val x = List(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }

    assertEquals(3, x)
  }

  @Test
  def prob2_tail() {
    val xs = List(1, 2, 3)
    assertEquals(List(2, 3), List.tail(xs))
  }

  @Test
  def prob3_setHead() {
    val xs = List(1, 2, 3)
    assertEquals(List(4, 2, 3), List.setHead(4, xs))
  }

  @Test
  def prob4_drop() {

    val xs = List(1, 2, 3)
    assertEquals(List(2, 3), List.drop(xs, 1))
    assertEquals(List(3), List.drop(xs, 2))
    assertEquals(Nil, List.drop(xs, 3))
    assertEquals(Nil, List.drop(xs, 4))
  }

  @Test
  def prob5_dropWhile() {
    val xs = List(1, 3, 5, 8)
    assertEquals(List(8), List.dropWhile(xs, (x: Int) => x % 2 == 1))
  }

  @Test
  def prob6_init() {
    val xs = List(1, 2, 3, 4)
    assertEquals(List(1, 2, 3), List.init(xs))
  }

  @Test
  def prob7_foldRight() {
    val xs = List(1.0, 2.0, 3.0, 0, 4.0)
    val double = List.product2(xs)

  }

  @Test
  def prob8_foldRightCons() {
    val res = List.foldRight(List(1, 2, 3), Nil: List[Int])((x, xs) => Cons(x, xs))
    println(res)
  }

  @Test
  def prob9_length() {
    val len = List.length(List(1, 2, 3))
    assertEquals(3, len)
  }

  @Test
  def prob10_foldLeft() {

  }

  @Test
  def prob11_sumUsingFoldLeft() {
    val sum = List.sum3(List(1, 2, 3))
    assertEquals(6, sum)
  }

  @Test
  def prob12_reverse() {
    val reversed = List.reverse(List(1, 2, 3))
    assertEquals(List(3, 2, 1), reversed)
  }

}
