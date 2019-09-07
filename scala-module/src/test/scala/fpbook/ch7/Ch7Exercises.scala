package fpbook.ch7

import java.util.concurrent.Executors

import fpbook.ch7.Par.Par
import org.junit.Test

class Ch7Exercises {

  @Test
  def prob1_signature_of_map2() {
    // the answer is that the signature of map2 is def map2[A, B, C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C]
  }


  @Test
  def prob2_representation_of_par() {
    // th
  }

  @Test
  def prob3_improve_map2_to_keep_expire_time() {
    //
  }

  @Test
  def prob4_asyncF() {


    val a = (v : Int) => v * 2

    val f = Par.asyncF2(a)


  }

  @Test
  def prob5_sequence() {

  }

  @Test
  def prob6_parFilter() {

  }

  @Test
  def prob7_map_fusion() {

  }

  @Test
  def prob8_fork_counter_example() {
    val a = Par.lazyUnit(42 + 1)
    val s = Executors.newSingleThreadExecutor()
    val res = Par.run(s)(Par.fork(a))
    println(res.get())
  }

  @Test
  def prob9_prove_all_fixed_threadpoll_can_cause_deadlock() {

  }


  @Test
  def prob10_errorhadlnig() {

  }

  @Test
  def prob11_choiceN() {

  }

  @Test
  def prob12_choiceMap() {

  }

  @Test
  def prob13_chooser() {

  }

  @Test
  def prob14_join() {

  }

  def sum(ints: IndexedSeq[Int]): Par[Int] = {
    reduce(ints)(_ + _)
  }

  def max(ints: IndexedSeq[Int]): Par[Int] = {
    reduce(ints)(Math.max)
  }

  def reduce[A, B](ints: IndexedSeq[A])(f: (A, A) => A): Par[A] = {
    if (ints.length <= 1) {
      Par.unit(ints.headOption getOrElse (throw new IllegalArgumentException("")))
    } else {
      val (l, r) = ints.splitAt(ints.length / 2)
      val pa = reduce(l)(f)
      val pb = reduce(r)(f)
      Par.map2(Par.fork(pa), Par.fork(pb))(f)
    }
  }
}
