package fpbook.ch5

import fpbook.ch5.Stream.{cons, empty}

sealed trait Stream[+A] {

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }

  def toList: List[A] = toListRecursive

  private def toListRecursive: List[A] = this match {
    case Empty => Nil
    case Cons(h, t) => h() :: t().toList
  }

  def toListTailRecursion: List[A] = {
    @annotation.tailrec
    def go(stream: Stream[A], xs: List[A]): List[A] = {
      stream match {
        case Cons(h, t) => go(t(), h() :: xs)
        case Empty => xs
      }
    }

    go(this, Nil).reverse
  }

  def take(n: Int): Stream[A] = {
    this match {
      case Cons(h, t) if (n == 1) => cons(h(), empty)
      case Cons(h, t) if (n > 1) => cons(h(), t().take(n - 1))
      case _ => empty
    }
  }

  def drop(n: Int): Stream[A] = {
    this match {
      case Cons(_, t) if (n == 0) => this
      case Cons(_, t) if (n > 0) => t().drop(n - 1)
      case _ => empty
    }
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    this match {
      case Cons(h, t) if (p(h())) => cons(h(), t().takeWhile(p))
      case _ => empty
    }
  }

  def exists(p: A => Boolean): Boolean =
    this match {
      case Cons(h, t) => p(h()) || t().exists(p)
      case _ => false
    }

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def existsViaFoldRight(p: A => Boolean): Boolean = {
    this.foldRight(false)((x, y) => p(x) || y)
  }

  def forAll(p: A => Boolean): Boolean = {
    foldRight(true)((x, y) => p(x) && y)
  }

  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = {
    foldRight[Stream[A]](empty)((a, acc) => if (p(a)) cons(a, acc) else empty)
  }

  def mapViaFoldRight[B](f: A => B): Stream[B] = {
    foldRight[Stream[B]](empty)((a, s) => cons(f(a), s))
  }

  def filterViaFoldRight(p: A => Boolean): Stream[A] = {
    foldRight[Stream[A]](empty)((a, acc) => if (p(a)) cons(a, acc) else acc)
  }

  def appendViaFoldRight[B >: A](stream: => Stream[B]): Stream[B] =
    foldRight(stream)((a, acc) => cons(a, acc))

  def flatMapViaFoldRight[B](f: A => Stream[B]): Stream[B] = {
    foldRight[Stream[B]](empty)((a, acc) => f(a).appendViaFoldRight(acc))
  }

  def headOptionViaFoldRight(): Option[A] = {
    foldRight(None: Option[A])((a, op) => Some(a))
  }

}

case object Empty extends Stream[Nothing] {

}

case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A] {

}

object Stream {

  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] = {
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))
  }
}



