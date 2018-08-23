package com.kakao.nills.akka.iot.actor

import akka.actor.{Actor, ActorLogging, Props}

class IotSupervisor extends Actor with ActorLogging{

  override def preStart(): Unit = log.info("Iot Application started")
  override def postStop(): Unit = log.info("Iot Application stopped")

  override def receive = Actor.emptyBehavior
}

object IotSupervisor {
  def props(): Props = Props(new IotSupervisor)
}


