package concurrent

import org.junit.Test

import scala.concurrent.Future
import scala.util.{Failure, Success}

class FutureLearingTest {

  @Test
  def futureWithCallback(){
    import scala.concurrent.ExecutionContext.Implicits.global
    val future: Future[List[String]] = Future(
      getList()
    )
    Thread.sleep(300)
    // on complete to success
    future.foreach(list => {
      println(Thread.currentThread().getName + " nins")
      list.foreach(println)}
    )
  }

  @Test
  def futureWithOnComplete() {
    import scala.concurrent.ExecutionContext.Implicits.global
    val f: Future[List[String]] = Future(
      getList()
    )
    Thread.sleep(300)
    f onComplete {
      case Success(posts) =>
        println(Thread.currentThread().getName + " exec")
        for (post <- posts) println(post)
      case Failure(t) => println("An error has occurred: " + t.getMessage)
    }
  }

  @Test
  def futureOrderingIsNotGuranteed() {

    import scala.concurrent.ExecutionContext.Implicits.global
    @volatile var totalA = 0

    val text = Future {
      "na" * 16 + "BATMAN!!!"
    }

    text foreach { txt =>
      totalA += txt.count(_ == 'a')
    }

    text foreach { txt =>
      totalA += txt.count(_ == 'A')
    }

    Thread.sleep(3000)
  }

  def getList()= {
    Thread.sleep(100)
    println(Thread.currentThread().getName)
    List("apple", "banana", "carrot")
  }

}
