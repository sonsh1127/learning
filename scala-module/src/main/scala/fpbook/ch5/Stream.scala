package fpbook.ch5

import fpbook.ch5.Stream.{cons, empty, unfoldViaMatch}

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
      case Cons(h, t)
        if (p(h())) =>
        cons(h(), t().takeWhile(p))
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
      case Cons(h, t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
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
    foldRight(None: Option[A])((a, _) => Some(a))
  }

  def mapViaUnFold[B](f: A => B): Stream[B] = {
    unfoldViaMatch(this)(s =>
      s match {
        case Cons(h, t) => Some((f(h()), t()))
        case _ => None
      }
    )
  }

  def takeViaUnFold(n: Int): Stream[A] = {
    // 인자에 묶어서 전달하는 방법
    unfoldViaMatch((this, n)) {
      case (Cons(h, t), 1) => Some((h(), (empty, 0)))
      case (Cons(h, t), n) if n > 1 => Some((h(), (t(), n - 1)))
      case _ => None
    }
  }

  def takeWhileUnFold(f: A => Boolean): Stream[A] = {
    unfoldViaMatch(this) {
      case Cons(h, t) if (f(h())) => Some((h(), t()))
      case _ => None
    }
  }

  def zipWith[B, C](s2: Stream[B])(f: (A, B) => C): Stream[C] = {
    unfoldViaMatch((this, s2)) {
      case (Cons(h1, t1), Cons(h2, t2)) => Some(f(h1(), h2()), (t1(), t2()))
      case _ => None
    }
  }

  def zip[B](s2: Stream[B]): Stream[(A,B)] = zipWith(s2)((_, _))

  def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] =
    unfoldViaMatch((this, s2)) {
      case (Cons(h1, t1), Cons(h2, t2)) => Some((Some(h1()), Some(h2())), (t1(), t2()))
      case (Cons(h1, t1), Empty) => Some((Some(h1()), None), (t1(), Empty))
      case (Empty, Cons(h2, t2)) => Some((None, Some(h2())), (Empty, t2()))
      case _ => None
    }

  def startsWith[A](s: Stream[A]): Boolean = {
    zipAll(s)
      .takeWhile(op => op._2 != None)
      .forAll{case (x, y) => x == y}
  }

  def tails: Stream[Stream[A]] = {
    // 내 꼬리는 전체 스트림의 꼬리가 맞다
    val lastItems = unfoldViaMatch(this){
      case Cons(h, t) => Some(t(), t())
      case _ => None
    }

    cons(this, lastItems)
  }

  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] =
    foldRight(cons(z, empty))((a, acc) => {
      acc match {
        case Cons(h, t) => cons(f(a, h()), acc)
      }
    })
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

  def constant[A](a: A): Stream[A] = {
    cons(a, constant(a))
  }

  def from(n: Int): Stream[Int] = {
    cons(n, from(n + 1))
  }

  def fib(): Stream[Int] = {
    def go(a: Int, b: Int): Stream[Int] = {
      cons(a, go(b, a + b))
    }

    go(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    f(z).map(tuple => cons(tuple._1, unfold(tuple._2)(f))).getOrElse(empty)
  }

  def unfoldViaMatch[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    f(z) match {
      case Some((a, s)) => cons(a, unfoldViaMatch(s)(f))
      case None => empty
    }
  }

  def fibsViaUnFold(): Stream[Int] = {
    unfoldViaMatch((0, 1))(op => Some((op._1, (op._2, op._1 + op._2))))
  }

  def fromViaUnFold(n: Int): Stream[Int] = {
    unfoldViaMatch(n)(a => Some(a, a + 1))
  }

  def constantViaUnFold(n: Int): Stream[Int] = {
    unfoldViaMatch(n)(n => Some(n, n))
  }

  val onesViaUnfold = unfold(1)(_ => Some((1, 1)))

}



