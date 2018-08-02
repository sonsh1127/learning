package akka.iot.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.iot.actor.DeviceManager.RequestTrackDevice

class DeviceManager extends Actor with ActorLogging {

  var groupIdToActor = Map.empty[String, ActorRef]
  var actorToGroupId = Map.empty[ActorRef, String]

  override def preStart(): Unit = log.info("Device Manager started")

  override def postStop(): Unit = log.info("Device Manager stopped")

  override def receive: Receive = {
    case trackMsg@RequestTrackDevice(groupId, _) ⇒
      groupIdToActor.get(groupId) match {
        case Some(ref) ⇒
          ref forward trackMsg
        case None ⇒
          log.info("Creating device group actor for {}", groupId)
          val groupActor = context.actorOf(DeviceGroup.props(groupId), "group-" + groupId)
          context.watch(groupActor)
          groupActor forward trackMsg
          groupIdToActor += groupId -> groupActor
          actorToGroupId += groupActor -> groupId
      }

    case Terminated(groupActor) =>
      val groupId = actorToGroupId(groupActor)
      log.info("Device group actor for {} has been terminated", groupId)
      actorToGroupId -= groupActor
      groupIdToActor -= groupId
  }
}

object DeviceManager {
  def props() = Props(new DeviceManager)

  final case class RequestTrackDevice(groupId: String, deviceId: String)

  final case object DeviceRegistered

}