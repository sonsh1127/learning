package scalabook

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton

object Nills {
  implicit def function2ActionListener(f: ActionEvent => Unit) =
    new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = f(e)
    }
}

object Ch21 {
  implicit def function2ActionListener1111(f: ActionEvent => Unit) =
    new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = f(e)
    }

  def main(args: Array[String]): Unit = {
    val button = new JButton
    button.addActionListener((_: ActionEvent) => println(""))
  }
}

// implicit variable usually  unique type
class PreferredPrompt(val perference: String) {
}

object Greeter1 {
  def greet(name: String)(implicit prompt: PreferredPrompt) {
    println("Welcome, " + name + ". The system is ready.")
    println(prompt.perference)
  }
}

object ImplicitParamTest {
  def main(args: Array[String]): Unit = {
    // 1. explicit parameter passing
    val bobsPrompt = new PreferredPrompt("relax> ")
    Greeter1.greet("Bob")(bobsPrompt)

    implicit val prompt = new PreferredPrompt("Yes master>")
    Greeter1.greet("nills")
    val max = maxListUpBound(List(3, 9, -11, 121))
    val maxString = maxListUpBound(List("hello", "abc", "world", "zk"))
    println(maxListUpBound2(List(3, 9, -11, 121)) + " maxListUpBound2")
    println(maxListUpBoundWithViewBound(List(3, 9, -11, 121)) + " maxListUpBoundWithViewBound")
    println(max)
    println(maxString)
  }

  def maxListUpBound[T](elements: List[T])
                       (implicit orderer: T => Ordered[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxListUpBound(rest)(orderer)
        if (orderer(x) > maxRest) x else maxRest
    }

  def maxListUpBound2[T](elements: List[T])
                        (implicit orderer: T => Ordered[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxListUpBound(rest)
        if (x > maxRest) x else maxRest
    }

  def maxListUpBoundWithViewBound[T <% Ordered[T]](elements: List[T])
  : T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxListUpBound(rest)
        if (x > maxRest) x else maxRest
    }
}