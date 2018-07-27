package akka.iot

import akka.actor
import akka.actor.{Actor, ActorLogging, Props}

class IotSupervisor extends Actor with ActorLogging{

  override def preStart(): Unit = log.info("Iot Application started")

  override def postStop(): Unit = log.info("Iot Application stopped")
  override def receive = Actor.emptyBehavior
}

object IotSupervisor {
  def props(): Props = actor.Props(new IotSupervisor)
}

object Device {
  def props(groupId: String, deviceId: String): Props = Props(new Device(groupId, deviceId))

  final case class RecordTemperature(requestId: Long, value: Double)
  final case class TemperatureRecorded(requestId: Long)

  final case class ReadTemperature(requestId: Long)
  final case class RespondTemperature(requestId: Long, value: Option[Double])
}

class Device(groupId: String, deviceId: String) extends Actor with ActorLogging{

  import Device._

  var lastTemperatureReading: Option[Double] = None

  override def preStart(): Unit = log.info("Device actor {}- {} started", groupId, deviceId)

  override def postStop(): Unit = log.info("Device actor {} - {} stopped", groupId, deviceId)

  override def receive: Receive = {
    case RecordTemperature(id, value) =>
      log.info("Recorded temperature reading {} with {}", value, id)
      lastTemperatureReading = Some(value)
      sender() ! TemperatureRecorded(id)

    case ReadTemperature(id) ⇒
      sender() ! RespondTemperature(id, lastTemperatureReading)

  }
}

