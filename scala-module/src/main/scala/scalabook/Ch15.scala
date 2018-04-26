package scalabook

object Ch15 {
  def main(args: Array[String]): Unit = {
    val v = Var("x")
    val op = BinOp("+", Number(1), v)
    println(op)
    val other = op.copy(operator = "-")
    println(other)

    // pattern match

    val unary = UnOp("+", v)
    val second: List[Int] => Int = {
      case x :: y :: _ => y
    }
    second(List(1,2,3))
    //second(List())

    val patialFunc: PartialFunction[List[Int], Int] = {
      case x :: y :: _ => y
    }

    println(patialFunc.isDefinedAt(List()))
    println(patialFunc.isDefinedAt(List(1,2,3)))

    val op2 = BinOp("-", Var("a"), BinOp("-", Var("b"), Var("c")))

    val exprFormatter = new ExprFormatter

  }

  def simplify(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", exp)) => exp
    case BinOp("+", exp, Number(0)) => exp
    case BinOp("*", exp, Number(1)) => exp
    case _ => expr
  }

  def simplifyAll(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", exp)) => simplifyAll(exp)
    case BinOp("+", exp, Number(0)) => simplifyAll(exp)
    case BinOp("*", exp, Number(1)) => simplifyAll(exp)
    case UnOp(op, expr) => UnOp(op, simplifyAll(expr))
    case BinOp(op, l, r) => BinOp(op, simplifyAll(l), simplifyAll(r))
    case _ => expr
  }
}

abstract class Expr

case class Var(name: String) extends Expr

case class Number(num: Double) extends Expr

case class UnOp(operator: String, expr: Expr) extends Expr

case class BinOp(operator: String, left: Expr, right: Expr) extends Expr


class ExprFormatter {

  private val operatorGroups = Array(
    Set("|", "||"),
    Set("&", "&&"),
    Set("^"),
    Set("==", "!="),
    Set("<", "<=", ">", ">="),
    Set("+", "-"),
    Set("*", "%")
  )

  private val precedence = {
    val assoc = for {
      i <- 0 until operatorGroups.length
      op <- operatorGroups(i)
    } yield op -> i

    assoc.toMap
  }

  private val unaryOperatorPrecedence = operatorGroups.length
  private val fractionPrecedence = -1



}