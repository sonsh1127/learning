package currency

import org.junit.Assert._
import org.junit.{Assert, Test}

class MoneyTest {

  @Test
  def multiply() {
    val five = Money.dollar(5)
    assertEquals(Money.dollar(10), five.times(2))
    assertEquals(Money.dollar(15), five.times(3))
  }

  @Test
  def equality() {
    assertTrue(Money.dollar(5).equals(Money.dollar(5)))
    assertFalse(Money.dollar(7).equals(Money.dollar(5)))
    assertTrue(Money.frank(5).equals(Money.frank(5)))
    assertFalse(Money.frank(7).equals(Money.frank(5)))
    assertFalse(Money.frank(7).equals(Money.dollar(7)))
  }

  @Test
  def simpleAddition() {
    val five = Money.dollar(5)
    val expression = five.plus(Money.dollar(5))
    val bank = new Bank()
    val reduced = bank.reduce(expression, "USD")
    assertEquals(Money.dollar(10), reduced)
  }

  @Test
  def plusReturnsSum() {
    val five = Money.dollar(5)
    val expr = five.plus(Money.dollar(5))
    val sum = expr.asInstanceOf[Sum]
    assertEquals(five, sum.addend)
    assertEquals(five, sum.augend)
  }

  @Test
  def reduceSum() {
    val sum = new Sum(Money.dollar(3), Money.dollar(4))
    val bank = new Bank
    val result = bank.reduce(sum, "USD")
    assertEquals(Money.dollar(7), result)
  }

  @Test
  def reduceMoney() {
    val bank = new Bank
    val result = bank.reduce(Money.dollar(1), "USD")
    assertEquals(Money.dollar(1), result)
  }

  @Test
  def reduceMoneyDifferentCurrency() {
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val money = bank.reduce(Money.frank(2), "USD")
    assertEquals(Money.dollar(1), money)
  }

  @Test
  def identityRate() {
    val bank = new Bank
    val rate = bank.rate("USD", "USD")
    assertEquals(1, rate)
  }

  @Test
  def mixedAddition() {
    val fiveBucks = Money.dollar(5)
    val tenFrank = Money.frank(10)
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val sum = fiveBucks.plus(tenFrank)
    assertEquals(Money.dollar(10), bank.reduce(sum, "USD"))
  }

  @Test
  def sumPlusMoney() {
    val fiveBucks = Money.dollar(5)
    val tenFrank = Money.frank(10)
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val sum = new Sum(fiveBucks, tenFrank).plus(fiveBucks)
    val result = bank.reduce(sum, "USD")
    assertEquals(Money.dollar(15), bank.reduce(result, "USD"))

  }

  @Test
  def sumTimes() {
    val fiveBucks = Money.dollar(5)
    val tenFrank = Money.frank(10)
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val sum = new Sum(fiveBucks, tenFrank).times(2)
    val result = bank.reduce(sum, "USD")
    assertEquals(Money.dollar(20), bank.reduce(result, "USD"))
  }
}
