package fpbook.ch6

import fpbook.ch6.RNG._
import org.junit.Assert._
import org.junit.Test

class Ch6Exercises {

  @Test
  def abs_intMin() {
    assertFalse(Int.MinValue.abs > 0)
  }

  @Test
  def prob1_nonNegativeInt() {
    val (i, _) = nonNegativeInt(new SimpleRNG(1212L))
    assertTrue(i > 0)
  }

  @Test
  def prob2_double() {
    val (d, _) = double(new SimpleRNG(23232))
    assertTrue(d >= 0 && d < 1)
  }

  /**
    * I do not think it is necessary to solve it.
    */
  @Test
  def prob4_ints() {
    val (list, _) = ints(10)(new SimpleRNG(232321111))
    val set = list.toSet
    assertEquals(10, set.size)
  }

  @Test
  def prob5_doubleViaMap() {
    val (d, _) = doubleViaMap(new SimpleRNG(23232))
    assertTrue(d >= 0 && d < 1)
  }

  @Test
  def prob6_map2() {
    val r: Rand[Int] = _.nextInt
    val r2: Rand[Int] = _.nextInt
    val nins = map2(r, r2)(_ + _)
  }

  @Test
  def prob7_intsViaSequence() {
    val (list, _) = intsViaSequence(10)(new SimpleRNG(232321111))
    val set = list.toSet
    assertEquals(10, set.size)
  }

  @Test
  def prob8_flatMap() {
    val value: Rand[Double] = flatMap(int) {
      i => unit(i.toDouble)
    }
  }

  @Test
  def prob9_mapViaFlatMap() {
    val zero: Rand[Int] = mapViaFlatMap(int) { i => i - i }
  }

  @Test
  def prob9_map2ViaFlatMap() {
    map2ViaFlatMap(int, int)((i, j) => i + j)
  }

  @Test
  def prob10_unitState() {
    val state: State[Nothing, Int] = State.unit(3)
  }

  @Test
  def prob10_mapState() {

  }

  @Test
  def prob10_map2State() {

  }

  @Test
  def prob10_flatMapState() {

  }

  @Test
  def prob10_sequenceState() {

  }

  @Test
  def prob11_simulateStateMachine() {

  }

}
