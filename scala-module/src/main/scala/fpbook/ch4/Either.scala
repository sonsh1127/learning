package fpbook.ch4

sealed trait Either[+E, +A] {
  def map[B](f: A => B): Either[E, B] = this match {
    case Right(a) => Right(f(a))
    case Left(e) => Left(e)
  }

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
    case Right(a) => f(a)
    case Left(e) => Left(e)
  }

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
    case Left(_) => b
    case Right(a) => Right(a)
  }

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C):
  Either[EE, C] = for {a <- this; b1 <- b} yield f(a, b1)


}

case class Left[+E](value: E) extends Either[E, Nothing]

case class Right[+A](value: A) extends Either[Nothing, A]

object Either {

  def sequence[E, A](xs: List[Either[E, A]]): Either[E, List[A]] = xs match {
    case Nil => Right(Nil)
    case h :: t => h.flatMap(hv => sequence(t).map(tv => hv :: tv))
  }

  def traverse[E, A, B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    es match {
      case Nil => Right(Nil)
      case h :: t => (f(h) map2 traverse(t)(f)) (_ :: _)
    }

  def traverse_1[E, A, B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    es.foldRight(Right(Nil): Either[E, List[B]])((a, b) => f(a).map2(b)(_ :: _))

  def mkName(name: String): Either[String, Name] = {
    if (name == null || name == "") {
      Left("empty string")
    } else {
      Right(new Name(name))
    }
  }

  def mkAge(age: Int): Either[String, Age] = {
    if (age < 0) {
      Left("Invalid age")
    } else {
      Right(new Age(age))
    }
  }

  def mkPerson(name: String, age: Int): Either[String, Person] = {
    mkName(name).map2(mkAge(age))(new Person(_, _))
  }


}

case class Person(name: Name, age: Age)

sealed class Name(val value: String)

sealed class Age(val value: Int)
