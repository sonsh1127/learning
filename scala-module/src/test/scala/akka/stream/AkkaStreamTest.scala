package akka.stream

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Keep, Merge, RunnableGraph, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}
import org.junit.Test

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}

class AkkaStreamTest {

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
      .to(Sink.foreach(x => println(Thread.currentThread().getName() + " nins " + x)))

    // starting from a source
    val source = Source(1 to 6).map(_ * 2)
    val g = source.to(Sink.foreach(r => println(Thread.currentThread().getName() + " " + r)))
    g.run()


    // starting from a sink
    val sink: Sink[Int, NotUsed] = Flow[Int].map(_ * 9).to(Sink.foreach(println(_)))
    Source(1 to 6).to(sink)

    // broadcast to sink inline
    val otherSink: Sink[Int, NotUsed] = Flow[Int].alsoTo(Sink.foreach(println(_))).to(Sink.ignore)
    Source(1 to 6).to(otherSink).run()

    val runnableGraph2: RunnableGraph[NotUsed] =
      Source(1 to 6).via(Flow[Int].map(_ * 2)).to(Sink.fold[Int, Int](0)(_ + _))

    runnableGraph.run()
    Thread.sleep(13000)
  }


  @Test
  def combineMaterializedValue() {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher
    // An source that can be signalled explicitly from the outside
    val source: Source[Int, Promise[Option[Int]]] = Source.maybe[Int]
  }


  @Test
  def buildGraph() {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher

    // An source that can be signalled explicitly from the outside
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val in = Source(1 to 10)
      val out = Sink.ignore

      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))

      val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
      bcast ~> f4 ~> merge
      ClosedShape
    })

    val res = g.run()
    println(res)
  }

  @Test
  def log() {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher

    Source(1 to 10).log("default").withAttributes(
      Attributes.logLevels(
        onElement = Logging.WarningLevel,
        onFinish = Logging.InfoLevel,
        onFailure = Logging.DebugLevel
      )
    ).map(_ + 1).
      runWith(Sink.ignore)
    Thread.sleep(3000)
  }

  @Test
  def alsoTo() {
    implicit val actorSystem = ActorSystem()
    implicit val actorMaterializer = ActorMaterializer()
    implicit val ec = actorSystem.dispatcher

    Source(1 to 10)
      .alsoTo(Sink.foreach(v => {
        println(s"first ${Thread.currentThread().getName} $v")
      }))
      .alsoTo(Sink.foreach(v => println(s"second ${Thread.currentThread().getName} $v"))
      ).to(Sink.foreach(v => println(s"third ${Thread.currentThread().getName} $v"))
    ).run()
  }

}
