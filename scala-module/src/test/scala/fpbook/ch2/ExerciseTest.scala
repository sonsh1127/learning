package fpbook.ch2

import org.junit.Assert._
import org.junit.{Assert, Test}

class ExerciseTest {

  @Test
  def fibo() {
    val f = Exercise.fibo(3)
    assertEquals(3, f)
  }

  @Test
  def isSorted() {
    val arr = Array(1, 7, 9)
    val naturalInt = (i: Int, j: Int) => i < j
    assertTrue(Exercise.isSorted(arr, naturalInt))
    assertFalse(Exercise.isSorted(Array(1, 10, 2), naturalInt))
  }

  @Test
  def curry() {
    val add = (x: Int, y: Int) => x + y
    val curried = Exercise.curry(add)
    assertEquals(5, curried(2)(3))
  }

  @Test
  def uncurry() {
    val curried = (x: Int) => (y: Int) => x + y
    val uncurried = Exercise.uncurry(curried)
    println(uncurried.getClass)
  }

  @Test
  def compose() {
    val addTwo = (x: Int) => x + 2
    val mulTwo = (x: Int) => x * 2
    val addTwoAndMulTwo = Exercise.compose(mulTwo, addTwo)
    val mulTwoAndAddTwo = Exercise.compose(addTwo, mulTwo)

    assertEquals(14, addTwoAndMulTwo(5))
    assertEquals(12, mulTwoAndAddTwo(5))
  }

}
