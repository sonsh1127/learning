package currency

import currency.Money.{dollar, frank}
import org.junit.Assert._
import org.junit.Test

class MoneyTest {

  @Test
  def multiply() {
    val five = dollar(5)
    val ten = dollar(10)

    assertEquals(dollar(10), five.times(2))
    assertEquals(dollar(15), five.times(3))
  }

  @Test
  def equality() {
    assertTrue(dollar(5).equals(dollar(5)))
    assertFalse(dollar(7).equals(dollar(5)))
    assertTrue(frank(5).equals(frank(5)))
    assertFalse(frank(7).equals(frank(5)))
    assertFalse(frank(7).equals(dollar(7)))
  }

  @Test
  def simpleAddition() {
    val five = dollar(5)
    val expression = five.plus(dollar(5))
    val bank = new Bank()
    val reduced = bank.reduce(expression, "USD")
    assertEquals(dollar(10), reduced)
  }

  @Test
  def plusReturnsSum() {
    val five = dollar(5)
    val expr = five.plus(dollar(5))
    val sum = expr.asInstanceOf[Sum]
    assertEquals(five, sum.addend)
    assertEquals(five, sum.augend)
  }

  @Test
  def reduceSum() {
    val sum = new Sum(dollar(3), dollar(4))
    val bank = new Bank
    val result = bank.reduce(sum, "USD")
    assertEquals(dollar(7), result)
  }

  @Test
  def reduceMoney() {
    val bank = new Bank
    val result = bank.reduce(dollar(1), "USD")
    assertEquals(dollar(1), result)
  }

  @Test
  def reduceMoneyDifferentCurrency() {
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val money = bank.reduce(frank(2), "USD")
    assertEquals(dollar(1), money)
  }

  @Test
  def identityRate() {
    val bank = new Bank
    val rate = bank.rate("USD", "USD")
    assertEquals(1, rate)
  }

  @Test
  def mixedAddition() {
    val fiveBucks = dollar(5)
    val tenFrank = frank(10)
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val sum = fiveBucks.plus(tenFrank)
    assertEquals(dollar(10), bank.reduce(sum, "USD"))
  }

  @Test
  def sumPlusMoney() {
    val fiveBucks = dollar(5)
    val tenFrank = frank(10)
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val sum = new Sum(fiveBucks, tenFrank).plus(fiveBucks)
    val result = bank.reduce(sum, "USD")
    assertEquals(dollar(15), bank.reduce(result, "USD"))

  }

  @Test
  def sumTimes() {
    val fiveBucks = dollar(5)
    val tenFrank = frank(10)
    val bank = new Bank
    bank.addRate("CHF", "USD", 2)
    val sum = new Sum(fiveBucks, tenFrank).times(2)
    val result = bank.reduce(sum, "USD")
    assertEquals(dollar(20), bank.reduce(result, "USD"))
  }
}
