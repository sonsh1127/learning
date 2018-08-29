package akka.stream

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future
import scala.util.Success

object StreamSortExample extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  val src = Source(List(1, 7, 4, 3))
  val res: Future[Seq[Int]] = src.runWith(Sink.seq)
  res.onComplete {
    case Success(r) => println("success" + r.sortWith((l, r) => l < r))
  }
  Thread.sleep(3000)
}
