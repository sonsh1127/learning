package fpbook.ch4

import org.junit.Assert._
import org.junit.Test

class Ch4ExerciseTest {

  @Test
  def prob1_map() {
    val some = Some(12).map(x => x.toString)
    val none = None.map(x => x.toString)
    assertEquals(None, none)
    assertEquals("12", some.getOrElse("1"))
  }

  @Test
  def prob1_flatMap() {

    val validAge = "1"
    val validId = "2"
    val invalidAge = "a"
    val invalidId = "AAA"

    val some = Try(validAge.toInt).flatMap(a => Try(validId.toInt))
    assertTrue(some.isInstanceOf[Some[Int]])

    val none = Try(invalidAge.toInt).flatMap(a => Try(validId.toInt))
    assertTrue(none.eq(None))
    val none2 = Try(validAge.toInt).flatMap(a => Try(invalidId.toInt))
    assertTrue(none2.eq(None))
  }

  @Test
  def prob1_getOrElse() {
    assertEquals(1, Some(1).getOrElse(-1))
    assertEquals(-1, None.getOrElse(-1))
  }

  @Test
  def prob1_orElse() {
    assertEquals(Some(1), Some(1).orElse(Some(2)))
    assertEquals(Some(2), None.orElse(Some(2)))
  }

  @Test
  def prob1_filter() {
    val option: Option[Int] = None
    assertEquals(Some(2), Some(2).filter(x => x % 2 == 0))
    assertEquals(None, Some(1).filter(x => x % 2 == 0))
    assertEquals(None, option.filter(x => x % 2 == 0))
  }

  @Test
  def prob2_varianceViaFlatMap() {
    val res = variance(List(1, 2, 3))
    assertTrue(res.isInstanceOf[Some[Double]])
    val res2 = variance(List())
    assertEquals(None, res2)
  }

  @Test
  def prob3_map2() {
    val res = Option.map2(Some(3), Some(4))((x, y) => x + y)
    assertEquals(Some(7), res)
    val res2 = Option.map2(Some(3), None: Option[Int])((x, y) => x + y)
    assertEquals(None, res2)
  }

  @Test
  def prob4_sequence() {
    val list1 = List(Some(1), Some(2), Some(3))
    val op = Option.sequence(list1)
    assertEquals(Some(List(1, 2, 3)), op)

    val list2 = List(Some(1), None, Some(3))
    assertEquals(None, Option.sequence(list2))
  }

  @Test
  def prob5_traverse() {
    val option = Option.traverse(List("1", "a", "3"))(x => Try(x.toInt))
    val op2 = Option.traverse(List("1", "2", "3"))(x => Try(x.toInt))
    assertEquals(None, option)
    assertEquals(Some(List(1, 2, 3)), op2)
  }

  @Test
  def prob6() {

  }

  @Test
  def prob7() {

  }

  @Test
  def prob8() {

  }

  def mean(xs: Seq[Double]): Option[Double] = {
    if (xs.isEmpty) {
      None
    } else {
      Some(xs.sum / xs.length)
    }
  }

  def variance(seq: Seq[Double]): Option[Double] = {
    mean(seq).flatMap(m => mean(seq.map(x => Math.pow(x - m, 2))))
  }

  def Try[A](a: => A): Option[A] = {
    try {
      Some(a)
    } catch {
      case _: Exception => None
    }
  }

}
