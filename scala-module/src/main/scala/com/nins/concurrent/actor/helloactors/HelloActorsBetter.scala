package com.nins.concurrent.actor.helloactors

import akka.actor.{Actor, ActorSystem, PoisonPill, Props, Terminated}

object HelloActorsBetter extends App {

  val system = ActorSystem("ninn")

  val actor = system.actorOf(Props[Master], "master")

}

class Master extends Actor {
  val talker = context.actorOf(Props[Talker], "talker")

  override def preStart(): Unit = {
    context.watch(talker)

    talker ! Greet("ninned")
    talker ! Praise("noin")
    talker ! Celebrate("wow", 11)
    talker ! PoisonPill
  }

  override def receive: Receive = {
    case Terminated(`talker`) => context.system.terminate()
  }
}
