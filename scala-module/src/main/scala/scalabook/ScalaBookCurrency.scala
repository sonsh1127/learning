package scalabook

class ScalaBookCurrency {
}

abstract class CurrencyZone {

  type Currency <: AbstractCurrency

  def make(amount: Long): Currency

  abstract class AbstractCurrency {

    val amount: Long

    def designation: String

    override def toString = s"AbstractCurrency($amount, $designation)"

    def +(that: Currency): Currency = {
      make(this.amount + that.amount)
    }

    def *(that: Double): Currency = {
      make((this.amount * that).toLong)
    }

    def from(other: CurrencyZone#AbstractCurrency): Currency =
      make(math.round(other.amount.toDouble * Converter.exchangeRate(other.designation)(this.designation)))
  }
}

object US extends CurrencyZone {
  abstract class Dollar extends AbstractCurrency {
    override def designation: String = "USD"
  }

  override type Currency = Dollar

  override def make(cents: Long) = new Dollar {
    override val amount: Long = cents
  }
  val Cent = make(1)
  val Dollar = make(100)
  val CurrencyUnit = Dollar
}

object Europe extends CurrencyZone {
  abstract class Euro extends AbstractCurrency {
    override def designation: String = "EUR"
  }

  override type Currency = Euro

  override def make(cents: Long): Euro = new Euro {
    override val amount: Long = cents
  }
  val Cent = make(1)
  val Euro = make(100)
  val CurrencyUnit = Euro
}

object Converter {
  var exchangeRate = Map(
    "USD" -> Map("USD"-> 1.0, "EUR"-> 0.7, "JPY" -> 1.2, "CHF" -> 1.2),
    "EUR" -> Map("USD"-> 1.3, "EUR"-> 1.0, "JPY" -> 1.2, "CHF" -> 1.2),
    "CHF" -> Map("USD"-> 0.8, "EUR"-> 0.8, "JPY" -> 1.2, "CHF" -> 1.0)
  )
}

object CurrencyRunner extends App {
  val japan = Europe.Euro from US.Dollar * 100
  println(japan)
}