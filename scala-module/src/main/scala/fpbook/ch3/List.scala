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

  def append[A](xs: List[A], ys: List[A]): List[A] =
    xs match {
      case Nil => ys
      case Cons(h, t) => Cons(h, append(t, ys))
    }

  def foldLeftViaFoldRight[A, B](l: List[A], z: B)(f: (B, A) => B): B = {
    foldRight(l, (b: B) => b)((a, g) => b => g(f(b, a)))(z)
  }

  def foldRightViaFoldLeft[A, B](xs: List[A], z: B)(f: (A, B) => B): B = {
    foldLeft(reverse(xs), z)((x, y) => f(y, x))
  }

  def append_viaFold[A](xs: List[A], ys: List[A]): List[A] = {
    foldRight(xs, ys)(Cons(_, _))
  }

  def flatten[A](l: List[List[A]]): List[A] = {
    foldRight(l, Nil: List[A])(append(_, _))
  }

  def addOne(xs: List[Int]): List[Int] =
    xs match {
      case Nil => Nil
      case Cons(h, t) => Cons(h + 1, addOne(t))
    }

  def addOne2(xs: List[Int]): List[Int] =
    foldRight(xs, Nil: List[Int])((a, b) => Cons(a + 1, b))

  def mapToString(xs: List[Double]): List[String] =
    xs match {
      case Nil => Nil
      case Cons(h, t) => Cons(h.toString, mapToString(t))
    }

  def mapToString2(xs: List[Double]): List[String] =
    foldRight(xs, Nil: List[String])((h, t) => Cons(h.toString, t))

  def map[A, B](xs: List[A])(f: A => B): List[B] =
    xs match {
      case Nil => Nil
      case Cons(h, t) => Cons(f(h), map(t)(f))
    }

  def map2[A, B](xs: List[A])(f: A => B): List[B] =
    foldRight(xs, Nil: List[B])((h, t) => Cons(f(h), t))

  def filter[A](xs: List[A])(f: A => Boolean): List[A] = {
    xs match {
      case Nil => Nil
      case Cons(h, t) => if (f(h)) Cons(h, filter(t)(f)) else filter(t)(f)
    }
  }

  def filter2[A](xs: List[A])(f: A => Boolean): List[A] = {
    foldRight(xs, Nil: List[A])((h, t) => if (f(h)) Cons(h, t) else t)
  }

  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = {
    as match {
      case Nil => Nil
      case Cons(h, t) => append(f(h), flatMap(t)(f))
    }
  }

  def flatMap2[A, B](as: List[A])(f: A => List[B]): List[B] =
    foldRight(as, Nil: List[B])((h, t) => append(f(h), t))

  def flatMap3[A, B](as: List[A])(f: A => List[B]): List[B] =
    flatten(map(as)(f))

  def filterViaFlatMap[A](xs: List[A])(p: A => Boolean): List[A] =
    flatMap(xs)(a => if (p(a)) List(a) else Nil)

  def addList(xs: List[Int], ys: List[Int]): List[Int] = {
    (xs, ys) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (Cons(h1, t1), Cons(h2, t2)) => Cons(h1 + h2, addList(t1, t2))
    }
  }

  def zipWith[A, B, C](xs: List[A], ys: List[B])(f: (A, B) => C): List[C] = {
    (xs, ys) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (Cons(h1, t1), Cons(h2, t2)) => Cons(f(h1, h2), zipWith(t1, t2)(f))
    }
  }

  /**
    * in this case subsequnece mean substring
    * @param sup
    * @param sub
    * @tparam A
    * @return
    */
  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = {
    sup match {
      case Nil => sub == Nil
      case _ if startsWith(sup, sub) => true
      case Cons(_, t) => hasSubsequence(t, sub)
    }
  }

  @annotation.tailrec
  def startsWith[A](l: List[A], prefix: List[A]): Boolean = (l,prefix) match {
    case (_,Nil) => true
    case (Cons(h,t),Cons(h2,t2)) if h == h2 => startsWith(t, t2)
    case _ => false
  }

  @annotation.tailrec
  def hasSubsequence2[A](sup: List[A], sub: List[A]): Boolean = sup match {
    case Nil => sub == Nil
    case _ if startsWith(sup, sub) => true
    case Cons(h,t) => hasSubsequence2(t, sub)
  }

}
