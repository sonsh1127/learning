import java.util.concurrent.ForkJoinPool

import org.junit.Test

import scala.concurrent.forkjoin.ForkJoinPool.ManagedBlocker
import scala.concurrent.{ExecutionContext, Future}

class ExecutionContextLearningTest {

  @Test
  def globalContext() {
    val ec = ExecutionContext.global
    ec.execute(
      () => println(s"executed by ${Thread.currentThread().getName} + hello world")
    )
    Thread.sleep(100)
    println(s"${Thread.currentThread().getName} ended")
  }

  @Test
  def blocking() {
    import ExecutionContext.Implicits.global
    Future{
      ForkJoinPool.managedBlock(
        new ManagedBlocker{
          var done = false
          override def block(): Boolean = {
            true
          }
          override def isReleasable: Boolean = done
        }
      )
    }
  }
}
