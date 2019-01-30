package com.nins.concurrent.actor.helloactors

import akka.actor.{Actor, ActorSystem, Props}

object HelloActors extends App {

  val system = ActorSystem("HelloActors")

  val talker = system.actorOf(Props[Talker], "talker")

  talker ! Greet("nins")
  talker ! Praise("cons")
  talker ! Celebrate("nills", 15)

  Thread.sleep(2000)

  system.terminate()
}

class Talker extends Actor {
  override def receive: Receive = {
    case Greet(name) => println(s"Hello $name")
    case Praise(name) => println(s"$name, you're amazing")
    case Celebrate(name, age) => println(s"Here's to another $age years, $name")
  }
}

case class Greet(name: String)
case class Praise(name: String)
case class Celebrate(name: String, age: Int)
