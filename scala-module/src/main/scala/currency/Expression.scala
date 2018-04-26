package currency

trait Expression {
  def reduce(bank: Bank, to: String): Money
  def plus(expression: Expression): Expression
  def times(multiple: Int): Expression
}
