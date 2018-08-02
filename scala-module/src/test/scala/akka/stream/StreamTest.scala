package akka.stream

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import org.junit.Test

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class StreamTest {

  @Test
  def quickStart() {
    val source: Source[Int, NotUsed] = Source(1 to 100)

    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()

    // the execution context variable
    implicit val ec = actorSystem.dispatcher

    val factorials: Source[BigInt, NotUsed] = source.scan(BigInt(1))((acc, next) => {
      println(Thread.currentThread().getName)
      acc * next
    })

    val result: Future[IOResult] = factorials.map(num => ByteString(s"$num\n")).
      runWith(FileIO.toPath(Paths.get("factorials.txt")))

    Await.result(result, Duration.Inf)
    println("write complete")

    result.foreach(r => println(r))

  }

}
