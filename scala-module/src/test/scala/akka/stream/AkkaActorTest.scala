package akka.stream

import java.util.concurrent.Executors

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext

object AkkaActorTest {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()

    val actorRef = actorSystem.actorOf(Props[HelloActor], "nins")
    val actorRef2 = actorSystem.actorOf(Props[HelloActor], "cons")
    val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))


    for (i <- 1 to 100000) {
      ec.submit(new Runnable {
        override def run(): Unit = {
          actorRef ! s"nins ${i}"
          actorRef2 ! s"cons ${i}"

         // actorRef2 ! s"cons ${i}"
        }
      })

    }


  }
}


class HelloActor extends Actor {
  override def receive: Receive = {
    case msg => {
      Thread.sleep(1000)
      println(s"${Thread.currentThread().getName} + hello ${msg}")
    }
  }
}
