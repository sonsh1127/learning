package akka.stream

import org.junit.Test

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


class FutureLearningTest {

  implicit val ec = ExecutionContext.global

  @Test
  def futureCallbackSuccess() {
    val f: Future[String] = Future {
      println(s"${Thread.currentThread().getName}")
      "hello"
    }

    f.onComplete {
      case Success(r) => println(Thread.currentThread() + " successfully ended " + r)
    }

    Thread.sleep(100)
    println("main ended")
  }

  @Test
  def futureFailed() {

    val f = Future {
      println(s"${Thread.currentThread().getName}")
      throw new RuntimeException("dsfsdf")
    }

    f.onComplete {
      case Success(r) => println(Thread.currentThread() + " successfully ended " + r)
      case Failure(t) => println(Thread.currentThread() + "failed " + t)
    }
    Thread.sleep(100)
    println("main ended")
  }

  @Test
  def sharedFuture() {
    var totalA = 0

    val text = Future {
      println("make future")
      "na" * 16 + "BATMAN!!!"
    }

    text foreach { txt =>
      totalA += txt.count(_ == 'a')
    }

    text foreach { txt =>
      totalA += txt.count(_ == 'A')
    }


    Thread.sleep(1000)
    println("main ended")

  }

}
