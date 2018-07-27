package akka.stream

import java.nio.file.Paths

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString

import scala.concurrent.Future

object QuickStart extends App {
  val source: Source[Int, NotUsed] = Source(1 to 100)

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  val factorials: Source[BigInt, NotUsed] = source.scan(BigInt(1))((acc, next) => acc * next)

  val result: Future[IOResult] = factorials.map(num => ByteString(s"$num\n")).
    runWith(FileIO.toPath(Paths.get("factorials.txt")))
  result.onComplete(_ => actorSystem.terminate())

}
