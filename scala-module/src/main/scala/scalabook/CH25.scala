package scalabook

import scala.collection.{IndexedSeqLike, mutable}
import scala.collection.mutable.ArrayBuffer

object CH25 extends App {
  // builder
  val buf = new ArrayBuffer[Int]
  val builder = buf mapResult (_.toArray)
  println(builder)
  println(builder.getClass)
  val array = builder.result()
  println(array.getClass)

}


abstract class Base

case object A extends Base

case object C extends Base

case object G extends Base

case object U extends Base

object Base {
  val fromInt: Int => Base = Array(A, C, G, U)
  val toInt: Base => Int = Map(A -> 0, C -> 1, G -> 2, U -> 3)
}

final class RNA1 private(val groups: Array[Int], val length: Int) extends IndexedSeq[Base] {

  import RNA1._

  def apply(idx: Int): Base = {
    if (idx < 0 || idx >= length)
      throw new IndexOutOfBoundsException
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }

}

object RNA1 {

  private val S = 2
  private val N = 32 / S
  private val M = (1 << S) - 1

  def fromSeq(buf: Seq[Base]): RNA1 = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <- 0 until buf.length)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)

    new RNA1(groups, buf.length)
  }

  def apply(bases: Base*) = fromSeq(bases)
}

object RNA1Test extends App {

  val xs = List(A, G, C, A)
  val rna = RNA1.fromSeq(xs)

  val rna1 = RNA1(A, G, C, G, C)

  println(rna1.length)

  println(rna1.last)

  // take returns instance of vector not RNA1. to solve this problem
  println(rna1.take(3))

  val rna2 = RNA2(A, U, G, G, C)
  println(rna2.take(3))
  println(rna2.filter( U != _))
}


final class RNA2 private(val groups: Array[Int], val length: Int) extends IndexedSeq[Base] with IndexedSeqLike[Base, RNA2] {

  import RNA2._

  override def newBuilder: mutable.Builder[Base, RNA2] = new ArrayBuffer[Base] mapResult fromSeq

  def apply(idx: Int): Base = {
    if (idx < 0 || idx >= length)
      throw new IndexOutOfBoundsException
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }

}

object RNA2 {

  private val S = 2
  private val N = 32 / S
  private val M = (1 << S) - 1

  def fromSeq(buf: Seq[Base]): RNA2 = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <- 0 until buf.length)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)

    new RNA2(groups, buf.length)
  }

  def apply(bases: Base*) = fromSeq(bases)

}

class RNA3 {

}


object RNA3 {

}



