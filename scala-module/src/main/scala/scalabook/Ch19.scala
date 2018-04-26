package scalabook

class Ch19 {

  def main(args: Array[String]): Unit = {

    val list = List("", "bsfsf")
    workWithList(list)
    val arr: Array[String] = Array("", "bsfsf", "/sdfsf")
    val list2: List[String] = List("", "sfsfs")
    val list22: List[AnyRef] = list

    val queue: Queue[Orange] = Queue(new Orange)
    val queue2: Queue[Orange] = queue.enqueue(new Orange2)

    // covariant
    val queue4: Queue[AnyRef] = queue.enqueue("sfsfsfsffsfsf")
    val array: Array[Fruit] = Array(new Orange, new Apple)
    array(1) = new Apple
    var anyOutput = new OutputChannel[AnyRef] {
      override def write(x: AnyRef): Unit = {
      }
    }
    workWith(anyOutput)
  }

  def workWith(outputChannel: OutputChannel[AnyRef]) = {
    println(outputChannel)
  }
  def workWithArray(arr: Array[AnyRef]) =
    println(arr)
  def workWithQueue(queue: Queue[AnyRef]) =
    println(queue)
  def workWithList(list: List[AnyRef]) = println(list)
}

abstract class Cat[-T, +U] {
  def meow[W](volume: T, listener: Cat[U, T])
}

trait Queue[T] {
  def head: T

  def tail: Queue[T]

  def enqueue[U >: T](x: U): Queue[U]
}

class Fruit {
}

class Apple extends Fruit {
}

class Orange extends Fruit {
}

class Orange2 extends Orange {
}

object Queue {
  def apply[T](xs: T*) = new QueueImpl[T](xs.toList, Nil)

  class QueueImpl[T](
                       private val leading: List[T],
                       private val trailing: List[T]
                     ) extends Queue[T] {
    private def mirror =
      if (leading.isEmpty) new QueueImpl[T](trailing.reverse, Nil)
      else this

    def head = mirror.leading.head

    def tail = {
      val q = mirror
      new QueueImpl(q.leading.tail, q.trailing)
    }
    def enqueue[U >: T](t: U) = new QueueImpl(Nil, Nil)
  }

}

class Nins1[+T](val t: T) {
  //var temp = t

}

class OptimizedQueue[+T] private(
                                  private[this] var leading: List[T],
                                  private[this] var trailing: List[T]
                                ) {
  private def mirror() = {
    if (leading.isEmpty) {
      while (!trailing.isEmpty) {
        leading = trailing.head :: leading
        trailing = trailing.tail
      }
    }
  }

  def head: T = {
    mirror()
    leading.head
  }

  def tail: OptimizedQueue[T] = {
    mirror()
    new OptimizedQueue[T](leading.tail, trailing)
  }

  def enqueue[U >: T](x: U) = new OptimizedQueue[U](leading, x :: trailing)

}

// upper bound


//contravariance
object Helpers {
  def orderedMergeSort[T <: Ordered[T]](xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] = {
      (xs, ys) match {
        case (Nil, _) => ys
        case (_, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (x < y) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)

      }
    }

    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(orderedMergeSort(ys), orderedMergeSort(zs))
    }
  }
}

trait OutputChannel[-T] {
  def write(x: T)
}

class Publication(val title: String)

class Book(title: String) extends Publication(title)

object Library {
  val books: Set[Book] =
    Set(
      new Book("blah blah"),
      new Book("Jpa")
    )

  def printBookList(info: Book => AnyRef) {
    books.foreach(b => println(info(b)))
  }
}

object Customer extends App {
  def getTitle(p: Publication): String = {
    p.title
  }
  Library.printBookList(getTitle)
}


class Person(val fistName: String, val lastName: String) extends Ordered[Person] {
  override def compare(that: Person) = {
    val lastNameComp = lastName.compareToIgnoreCase(that.lastName)
    if (lastNameComp != 0) lastNameComp
    else fistName.compareTo(that.fistName)
  }
}








