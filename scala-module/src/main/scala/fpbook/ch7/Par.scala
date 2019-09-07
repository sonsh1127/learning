package fpbook.ch7

import java.util.concurrent.{ExecutorService, Future, TimeUnit}

object Par {

  type Par[A] = ExecutorService => Future[A]

  private case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false

    override def isCancelled: Boolean = false

    override def isDone: Boolean = true

    override def get(timeout: Long, unit: TimeUnit): A = get
  }

  def unit[A](a: A): Par[A] = {
    _: ExecutorService => UnitFuture(a)
  }

  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = {
    (es: ExecutorService) => UnitFuture(f(a(es).get(), b(es).get))
  }


  def map2_enhanced[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    es => {
      val (af, bf) = (a(es), b(es))
      Map2Future(af, bf, f)
    }

  def fork[A](a: => Par[A]): Par[A] = (es: ExecutorService) => a(es)

  def lazyUnit[A](a: => A) = fork(unit(a))

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  def asyncF[A, B](f: A => B): A => Par[B] = {
    a => lazyUnit(f(a))
  }

  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map(parList)(a => a.sorted)

  def map[A, B](par: Par[A])(f: A => B): Par[B] = {
    map2(par, unit(()))((a, _) => f(a))
  }

  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = fork {
    val fbs = ps.map(asyncF(f))
    sequence(fbs)
  }

  def sequence[A](ps: List[Par[A]]): Par[List[A]] = {
    ps.foldRight(unit(List[A]()))((acc, pa) => map2(acc, pa)((a, b) => a :: b))
  }

  def parFilter[A](l: List[A])(f: A => Boolean): Par[List[A]] = {
    val pars: List[Par[List[A]]] =
      l map (asyncF((a: A) => if (f(a)) List(a) else List()))
    map(sequence(pars))(_.flatten)
  }


  case class Map2Future[A, B, C](a: Future[A], b: Future[B], function: (A, B) => C) extends Future[C] {
    @volatile var cache: Option[C] = None

    override def isCancelled: Boolean = a.isCancelled || b.isCancelled

    override def isDone: Boolean = ???

    override def cancel(mayInterruptIfRunning: Boolean): Boolean = ???

    override def get(): C = compute(Long.MaxValue)

    override def get(timeout: Long, unit: TimeUnit): C = compute(TimeUnit.NANOSECONDS.convert(timeout, unit))

    private def compute(timeoutInNanos: Long): C = cache match {
      case Some(v) => v
      case None =>
        val start = System.nanoTime()
        val v1 = a.get(timeoutInNanos, TimeUnit.NANOSECONDS)
        val end = System.nanoTime()
        val v2 = b.get(timeoutInNanos - (end - start), TimeUnit.NANOSECONDS)

        val r = function(v1, v2)
        cache = Some(r)
        r
    }
  }

  def asyncF2[A, B](f: A => B): A => Par[B] = {
    a => lazyUnit(f(a))
  }

  def sequence2[A](ps: List[Par[A]]): Par[List[A]] = {
    ps.foldRight(Par.unit(List[A]()))((e, acc) => Par.map2(e, acc)((a: A, b) => a :: b))
  }

  // todo parFilter2 와 parFilter 는 무슨 의미적 차이가 있는 것일까?
  def parFilter2[A](as: List[A])(p: A => Boolean): Par[List[A]] = {
    val res: Par[List[List[A]]] = parMap(as)(x => if(p(x)) List(x) else List())
    map(res)(res => res.flatten)
  }

}
