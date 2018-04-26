package currency

class Sum(val augend: Expression,
          val addend: Expression) extends Expression{

  def reduce(bank: Bank, to: String) : Money = {
    val left = bank.reduce(augend, to)
    val right = bank.reduce(addend, to)
    return new Money(left.amount + right.amount,to)
  }

  override def plus(expression: Expression) = {
    new Sum(this, expression)
  }

  override def times(multiple: Int) = {
    new Sum(augend.times(multiple), addend.times(multiple))
  }
}

