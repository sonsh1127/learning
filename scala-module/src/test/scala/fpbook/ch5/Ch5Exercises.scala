package fpbook.ch5

import fpbook.ch5.Stream.{cons, constant, empty}
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
    val stream = createStreamVia_cons123
    stream.headOption
    stream.headOption
  }

  private def createStreamVia_cons123 = {
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

  private def createStreamVia_cons456 = {
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


  private def createStreamVia_cons4567 = {
    cons({
      println("evaluated 4");
      4
    }, cons({
      println("evaluated 5");
      5
    }, cons({
      println("evaluated 6");
      6
    }, cons({
      println("evaluated 7");
      7
    }, empty))))
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
  def chaining() {
    val res = createStreamVia_cons4567.mapViaFoldRight(x => {
      println(x + 10)
      x + 10
    }
    ).filterViaFoldRight(_ % 2 == 0)
  }

  @Test
  def prob1_toList() {
    assertEquals(List("a", "b", "c"), Stream("a", "b", "c").toList)
  }

  @Test
  def prob2_take() {
    val stream = createStreamVia_cons123
    val taken = stream.take(2)
    println("taken")
    val expectedList = List(1, 2)
    assertEquals(expectedList, taken.toList)
  }

  @Test
  def prob2_drop() {
    val stream = createStreamVia_cons123
    val dropped = stream.drop(2)
    println("dropped")
    val expected = List(3)
    assertEquals(expected, dropped.toList)
  }

  @Test
  def prob3_takeWhile() {
    val stream = createStreamVia_cons123
    val actual = stream.takeWhile(it => it < 3)
    println("taken")
    val expected = List(1, 2)
    assertEquals(expected, actual.toList)
  }

  @Test
  def prob4_forAll() {
    val stream = createStreamVia_cons4567
    assertFalse(stream.forAll(_ < 7))
  }

  @Test
  def prob5_takeWhileViaFoldRight() {
    val stream = createStreamVia_cons123
    stream.takeWhileViaFoldRight(it => it < 3)
  }

  @Test
  def prob6_headOptionViaFoldRight() {
    val stream = createStreamVia_cons123
    val head = stream.headOptionViaFoldRight()
    println("actual")
    val head2 = stream.headOption
    assertEquals(head2, head)
  }

  @Test
  def prob7_mapViaFoldRight() {
    val stream = createStreamVia_cons123
    val res = stream.mapViaFoldRight(a => a * 2)
    println("actual")
    assertEquals(List(2, 4, 6), res.toList)
  }

  @Test
  def prob7_filterViaFoldRight() {
    val stream = createStreamVia_cons123
    val res = stream.filterViaFoldRight(x => x % 2 == 1)
    println("actual")
    assertEquals(List(1, 3), res.toList)
  }

  @Test
  def prob7_appendViaFoldRight() {
    val stream = createStreamVia_cons123
    val actual = stream.appendViaFoldRight(createStreamVia_cons456)
    println("actual")
    assertEquals(List(1, 2, 3) ++ List(4, 5, 6), actual.toList)
  }

  @Test
  def prob7_flatMapViaFoldRight() {
    val stream = createStreamVia_cons123
    val actual = stream.flatMapViaFoldRight(x => {
      createStreamVia_cons456
    })
    println("actual")
    assertEquals(List(4, 5, 6, 4, 5, 6, 4, 5, 6), actual.toList)
  }

  // local variable does not support defni
  val ones: Stream[Int] = cons(1, ones)

  @Test
  def infinite_stream() {
    val fiveOnes = ones.take(5).toList
    val expected = List.fill(5)(1)
    assertEquals(expected, fiveOnes)
    assertTrue(ones.exists(_ % 2 != 0))
  }

  @Test
  def prob8_constant() {
    assertEquals(List(2, 2, 2), constant(2).take(3).toList)
  }

  @Test
  def prob9_from() {
    assertEquals(List(3, 4, 5), Stream.from(3).take(3).toList)
  }

  @Test
  def prob10_fibs() {
    assertEquals(List(0, 1, 1, 2, 3, 5), Stream.fib().take(6).toList)
  }

  @Test
  def prob11_unfold() {
    val cons = Stream.unfoldViaMatch(1)(x => Some((1, 1)))
    val ones = cons.take(3)
    assertEquals(List(1, 1, 1), ones.toList)
  }

  @Test
  def prob12_fibsViaUnfold() {
    val actual = Stream.fibsViaUnFold().take(6).toList
    assertEquals(List(0, 1, 1, 2, 3, 5), actual)
  }

  @Test
  def prob12_fromViaUnfold() {
    assertEquals(List(3, 4, 5), Stream.fromViaUnFold(3).take(3).toList)
  }

  @Test
  def prob12_constantViaUnfold() {
    assertEquals(List(2, 2, 2), Stream.constantViaUnFold(2).take(3).toList)
  }

  @Test
  def prob12_onesViaUnfold() {
    assertEquals(List(1, 1, 1), Stream.onesViaUnfold.take(3).toList)
  }

  @Test
  def prob13_mapViaUnFold() {
    val stream = createStreamVia_cons123
    val res = stream.mapViaUnFold(a => a * 2)
    println("actual")
    assertEquals(List(2, 4, 6), res.toList)
  }

  @Test
  def prob13_takeViaUnFold() {
    val stream = createStreamVia_cons123
    val taken = stream.takeViaUnFold(2)
    println("taken")
    val expectedList = List(1, 2)
    assertEquals(expectedList, taken.toList)
  }

  @Test
  def prob13_takeWhileViaUnFold() {
    val stream = Stream(1, 3, 5, 8, 6)
    val res = stream.takeWhileUnFold(_ % 2 == 1)
    val expected = List(1, 3, 5)
    assertEquals(expected, res.toList)
  }

  @Test
  def prob13_zipWithViaUnFold() {
    val stream = createStreamVia_cons123
    val stream2 = createStreamVia_cons4567

    val zipped = stream.zipWith(stream2)(_ + _)

    assertEquals(3, size(zipped))
    assertEquals(List(5, 7, 9), zipped.toList)
  }

  def size[A](s: Stream[A]): Int = {
    s.foldRight(0)((_, count) => count + 1)
  }

  @Test
  def prob13_zipAllViaUnFold() {
    val stream = createStreamVia_cons123
    val stream2 = createStreamVia_cons4567
    val zipped = stream.zipAll(stream2)
    assertEquals(4, size(zipped))
    val expected = List(
      (Some(1), Some(4)),
      (Some(2), Some(5)),
      (Some(3), Some(6)),
      (None, Some(7))
    )
    assertEquals(expected, zipped.toList)
  }

  @Test
  def prob14_startsWith() {
    val stream = createStreamVia_cons123
    assertTrue(stream.startsWith(Stream(1, 2)))
    assertTrue(stream.startsWith(Stream(1)))
    assertTrue(stream.startsWith(Stream(1, 2, 3)))
    assertFalse(stream.startsWith(Stream(1, 1, 3)))
  }

  @Test
  def prob15_tailsViaUnFold() {
    val stream = createStreamVia_cons123
    val actual = stream.tails
    val listStream = actual.mapViaUnFold(s => s.toList)
    assertEquals(List(List(1, 2, 3), List(2, 3), List(3), List()), listStream.toList)
  }

  @Test
  def prob16_scanRight() {
    val stream = createStreamVia_cons123
    val res = stream.scanRight(0)(_ + _).toList
    assertEquals(List(6, 5, 3, 0), res)
  }

}
