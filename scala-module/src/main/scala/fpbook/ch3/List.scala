package fpbook.ch3

sealed trait List[+A] {
}

case object Nil extends List[Nothing] {
}

case class Cons[+A](head: A, t: List[A]) extends List[A] {
}

object List {
  def length(xs: List[Int]): Int = foldRight(xs, 0)((_, y) => y + 1)

  def sum(xs: List[Int]): Int = {
    xs match {
      case Nil => 0
      case Cons(h, t) => h + sum(t)
    }
  }

  def sum2(xs: List[Int]): Int = foldRight(xs, 0)(_ + _)

  def sum3(xs: List[Int]): Int = foldLeft(xs, 0)(_ + _)

  def product(xs: List[Double]): Double =
    xs match {
      case Nil => 1
      case Cons(0, _) => 0
      case Cons(h, t) => h * product(t)
    }

  def product2(xs: List[Double]): Double = foldRight(xs, 1.0)(_ * _)

  def product3(xs: List[Double]): Double = foldLeft(xs, 1.0)(_ * _)


  def apply[A](xs: A*): List[A] =
    if (xs.isEmpty) Nil
    else Cons(xs.head, apply(xs.tail: _*))

  def head[A](xs: List[A]): List[A] = xs match {
    case Nil => sys.error("tail of empty list")
    case Cons(_, t) => t
  }

  def tail[A](xs: List[A]): List[A] = xs match {
    case Nil => sys.error("")
    case Cons(_, tail) => tail
  }

  def setHead[A](h: A, xs: List[A]) = xs match {
    case Nil => Cons(h, Nil)
    case Cons(_, t) => Cons(h, t)
  }

  def drop[A](xs: List[A], n: Int): List[A] =
    if (n == 0) {
      xs
    } else {
      xs match {
        case Nil => Nil
        case Cons(_, t) => drop(t, n - 1)
      }
    }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] =
    l match {
      case Cons(h, t) if f(h) => dropWhile(t, f)
      case _ => l
    }


  def init[A](xs: List[A]): List[A] = {
    xs match {
      case Nil => sys.error("")
      case Cons(_, Nil) => Nil
      case Cons(h, tail) => Cons(h, init(tail))
    }
  }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(h, t) => f(h, foldRight(t, z)(f))
    }

  @annotation.tailrec
  def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B =
    as match {
      case Nil => z
      case Cons(h, t) => foldLeft(t, f(z, h))(f)
    }

  def reverse[A](xs: List[A]): List[A] =
    foldLeft(xs, Nil: List[A])((xs, x) => Cons(x, xs))
}
