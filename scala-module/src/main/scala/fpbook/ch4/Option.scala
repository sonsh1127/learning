package fpbook.ch4

sealed trait Option[+A] {
  def map[B](f: A => B): Option[B] = {
    this match {
      case Some(a) => Some(f(a))
      case None => None
    }
  }

  def getOrElse[B >: A](default: => B): B = {
    this match {
      case None => default
      case Some(v) => v
    }
  }

  def flatMap[B](f: A => Option[B]): Option[B] = {
    map(f).getOrElse(None)
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = {
    map(x => Some(x)).getOrElse(ob)
  }

  def filter(f: A => Boolean): Option[A] = {
    flatMap(x => if (f(x)) Some(x) else None)
  }

}

object Option {

  def lift[A, B](f: A => B): Option[A] => Option[B] = o => o map (f)

  def map2[A, B, C](op1: Option[A], op2: Option[B])(f: (A, B) => C): Option[C] = {
    op1.flatMap(a => op2.map(b => f(a, b)))
  }

  def sequence[A](xs: List[Option[A]]): Option[List[A]] = {
    xs match {
      case Nil => Some(Nil)
      case h :: t => h.flatMap(hv => sequence(t).map(l => hv :: l))
    }
  }

  def traverse[A, B](xs: List[A])(f: A => Option[B]): Option[List[B]] = {
    xs.foldRight(Some(Nil): Option[List[B]])((h, t) => map2(f(h), t)((_ :: _)))
  }

  def sequenceViaTraverse[A](xs: List[Option[A]]): Option[List[A]] = {
    traverse(xs)(x => x)
  }

}

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]

