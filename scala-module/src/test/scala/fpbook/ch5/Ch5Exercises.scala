package fpbook.ch5

import fpbook.ch5.Stream.{cons, empty}
import org.junit.Assert._
import org.junit.{Assert, Test}

class Ch5Exercises {

  @Test
  def logicalOperatorIsALazyFunction() {
    // println not execute here
    val res = false && {
      println("!!");
      true
    }
    assertFalse(res)
  }

  @Test
  def streamViaCons() {
    val stream = createStreamViaCons
    stream.headOption
    stream.headOption
  }

  private def createStreamViaCons = {
    Cons(() => {
      println(1);
      1
    },
      () => Cons(() => {
        println(2);
        2
      },
        () => Cons(() => {
          println(3);
          3
        }, () => empty)))
  }

  @Test
  def streamVia_cons() {
    val stream = createStreamVia_cons
    stream.headOption
    stream.headOption
  }

  private def createStreamVia_cons = {
    cons({
      println("evaluated 1");
      1
    }, cons({
      println("evaluated 2");
      2
    }, cons({
      println("evaluated 3");
      3
    }, empty)))
  }

  private def createStreamVia_cons2 = {
    cons({
      println("evaluated 4");
      4
    }, cons({
      println("evaluated 5");
      5
    }, cons({
      println("evaluated 6");
      6
    }, empty)))
  }


  def if2[A](condition: Boolean, onTrue: => A, onFalse: => A) {
    if (condition) onTrue else onFalse
  }

  @Test
  def if2() {
    if2(10 % 2 == 0, () => print("TRUE"), () => println("FALSE"))
    if2(10 % 2 == 0, print("TRUE"), println("FALSE"))
  }

  def maybeTwice(b: Boolean, i: => Int) = {
    lazy val j = i
    println("before exec if ")
    if (b) j + j else 0
  }

  @Test
  def maybeTwice() {
    val res = maybeTwice(true, {
      println("hi");
      42
    })
    assertEquals(84, res)
  }

  @Test
  def prob1_toList() {
    assertEquals(List("a", "b", "c"), Stream("a", "b", "c").toList)
  }

  @Test
  def prob2_take() {
    val stream = createStreamVia_cons
    val taken = stream.take(2)
    println("taken")
    val expectedList = List(1, 2)
    assertEquals(expectedList, taken.toList)
  }

  @Test
  def prob2_drop() {
    val stream = Stream("a", "b", "c")
    val dropped = stream.drop(2)
    val expected = List("c")
    assertEquals(expected, dropped.toList)
  }

  @Test
  def prob3_takeWhile() {
    val stream = Stream(1, 3, 5, 8, 6)
    val res = stream.takeWhile(_ % 2 == 1)
    val expected = List(1, 3, 5)
    assertEquals(expected, res.toList)
  }

  @Test
  def prob4_forAll() {
    val stream = Stream(1, 2, 3, 100, 1)
    assertFalse(stream.forAll(_ < 10))
  }

  @Test
  def prob5_takeWhileViaFoldRight() {
    val stream = createStreamVia_cons
    stream.takeWhileViaFoldRight(it => it < 3)
  }

  @Test
  def prob6_headOptionViaFoldRight() {
    val stream = createStreamVia_cons
    val head = stream.headOptionViaFoldRight()
    println("actual")
    val head2 = stream.headOption
    assertEquals(head2, head)
  }

  @Test
  def prob7_mapViaFoldRight() {
    val stream = createStreamVia_cons
    val res = stream.mapViaFoldRight(a => a * 2)
    println("actual")
    assertEquals(List(2, 4, 6), res.toList)
  }

  @Test
  def prob7_filterViaFoldRight() {
    val stream = createStreamVia_cons
    val res = stream.filterViaFoldRight(x => x % 2 == 1)
    println("actual")
    assertEquals(List(1, 3), res.toList)
  }

  @Test
  def prob7_appendViaFoldRight() {
    val stream = createStreamVia_cons
    val actual = stream.appendViaFoldRight(createStreamVia_cons2)
    println("actual")
    assertEquals(List(1, 2, 3) ++ List(4, 5, 6), actual.toList)
  }

  @Test
  def prob7_flatMapViaFoldRight() {
    val stream = createStreamVia_cons
    val actual = stream.flatMapViaFoldRight(x => {
      createStreamVia_cons2
    })

    println("actual")
    assertEquals(List(4,5,6,4,5,6,4,5,6), actual.toList)
  }

}
