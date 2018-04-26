package currency

class Money(val amount: Int, val currency: String) extends Expression {

  def plus(expression: Expression) : Expression = {
    new Sum(this, expression)
  }

  def times(multiplier: Int) = new Money(amount * multiplier, currency)

  def canEqual(other: Any): Boolean = other.isInstanceOf[Money]

  override def equals(other: Any): Boolean = other match {
    case that: Money =>
      (that canEqual this) &&
        amount == that.amount &&
        currency == that.currency
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(amount, currency)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"Money($amount, $currency)"

  override def reduce(bank: Bank, to: String): Money = {
    val rate = bank.rate(currency, to)
    new Money(amount / rate, to)
  }
}

object Money {
  def dollar(amount: Int) = new Money(amount, "USD")
  def frank(amount: Int)= new Money(amount, "CHF")
}
