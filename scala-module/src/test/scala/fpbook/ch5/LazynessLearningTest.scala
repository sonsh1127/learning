package fpbook.ch5

import org.junit.Test

class LazynessLearningTest {

  @Test
  def invoke() {
    lazyFunc({
      println("exec");
      3
    })
    println("lazyFunc")

    println("before eager")

    eagerFunc({
      println("exec");
      3
    })
    println("eagerFunc")

    lazyFuncCallEager({
      println("exec");
      3
    })
    println("lazyFuncCallEager")

    lazyFuncCallEager({
      println("exec");
      3
    })
    println("lazyFuncCallEager")

    lazyEagerLazy({
      println("exec");
      3
    })
  }

  def lazyFunc(a: => Int) {

  }

  def eagerFunc(a: Int) {

  }

  def lazyFuncCallEager(a: => Int) {
    println("before call eager func")
    eagerFunc(a)
  }

  def lazyEagerLazy(a: => Int) {
    lazyEnd(eager2(a))
  }

  def eager2(a:  Int) : Int =  {
    a
  }

  def lazyEnd(a: => Int) {

  }

}
