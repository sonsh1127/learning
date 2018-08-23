package com.kakao.nills.akka

import akka.actor.{Actor, ActorSystem, Props}

import scala.io.StdIn

object ActorExample extends App{

  val actorSystem = ActorSystem("testSystem")
  val firstRef = actorSystem.actorOf(Props[PrintMyActorRefActor], "first-actor")

  println(s"First: $firstRef")
  firstRef ! "printit"

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally actorSystem.terminate()
}

class PrintMyActorRefActor extends Actor {
  override def receive: Receive = {
    case "printit" =>
      val secondRef = context.actorOf(Props.empty, "second-actor")
      println(secondRef)
  }
}

object ActorLifeCycle extends App {

  val actorSystem = ActorSystem("actorLifeCycleSystem")

  val firstRef = actorSystem.actorOf(Props[StartStopActor1], "first")
  println(s"First: $firstRef")
  firstRef ! "stop"

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally actorSystem.terminate()
}


class StartStopActor1 extends Actor {
  override def preStart(): Unit = {
    println(s"${Thread.currentThread().getName}first started")
    context.actorOf(Props[StartStopActor2], "second")
  }
  override def postStop(): Unit = println("first stopped")

  override def receive: Receive = {
    case "stop" ⇒ context.stop(self)
  }
}

class StartStopActor2 extends Actor {
  override def preStart(): Unit = println(s"${Thread.currentThread().getName} second started")
  override def postStop(): Unit = println(s"${Thread.currentThread().getName} second stopped")

  // Actor.emptyBehavior is a useful placeholder when we don't
  // want to handle any messages in the actor.
  override def receive: Receive = Actor.emptyBehavior
}

object ActorSupervise extends App {

  val system = ActorSystem("nins")

  val supervisingActor = system.actorOf(Props[SupervisingActor], "supervising-actor")
  supervisingActor ! "failChild"
}

class SupervisingActor extends Actor {
  val child = context.actorOf(Props[SupervisedActor], "supervised-actor")

  override def receive: Receive = {
    case "failChild" ⇒ child ! "fail"
  }
}

class SupervisedActor extends Actor {
  override def preStart(): Unit = println("supervised actor started")
  override def postStop(): Unit = println("supervised actor stopped")

  override def receive: Receive = {
    case "fail" ⇒
      println("supervised actor fails now")
      throw new Exception("I failed!")
  }
}
