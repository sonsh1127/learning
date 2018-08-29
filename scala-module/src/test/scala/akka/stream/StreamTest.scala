package akka.stream

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, RunnableGraph, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}
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

  @Test
  def gettingStart() {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher

    val source = Source(1 to 10)
    val sink = Sink.fold[Int, Int](0)(_ + _)
    val runnableGraph: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)
    val future: Future[Int] = runnableGraph.run()
    val res = Await.result(future, Duration.Inf)
    println(res)
  }

  @Test
  def defineSourceAndSink() {

    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher


    // Creating a source from an iterable
    val src1 = Source(List(1, 2, 3))

    // Creating a source from a future
    val src2 = Source.fromFuture(Future.successful("Hello Streams"))


    // Create a source from a single element
    val src3 = Source.single("nills")

    // an empty source
    val emptySource = Source.empty

    // Sink that folds over the stream and returns a Future
    // of the final result as its materialized value
    Sink.fold[Int, Int](0)(_ + _)

    val res: Future[Int] = Source(1 to 10).runWith(Sink.head)
    Await.result(res, Duration.Inf)

    println(res)

    val sink: Sink[String, Future[Done]] = Sink.foreach[String](println(_))

  }

  @Test
  def wireUpStream() {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher

    // Explicitly creating and wiring up a Source, Sink and Flow
    val runnableGraph: RunnableGraph[NotUsed] = Source(1 to 6).via(Flow[Int].map(_ * 2))
      .to(Sink.foreach(x => println(Thread.currentThread().getName() + " " + x)))

    // starting from a source
    val source = Source(1 to 6).map(_ * 2)
    source.to(Sink.foreach(println(_)))

    // starting from a sink
    val sink: Sink[Int, NotUsed] = Flow[Int].map( _ * 2).to(Sink.foreach(println(_)))
    Source(1 to 6).to(sink)

    // broadcast to sink inline
    val otherSink: Sink[Int, NotUsed] = Flow[Int].alsoTo(Sink.foreach(println(_))).to(Sink.ignore)
    Source(1 to 6).to(otherSink)

    val runnableGraph2: RunnableGraph[NotUsed] =
      Source(1 to 6).via(Flow[Int].map(_ * 2)).to(Sink.fold[Int, Int](0)(_ + _))

    runnableGraph.run()
    Thread.sleep(1000)
  }

}
