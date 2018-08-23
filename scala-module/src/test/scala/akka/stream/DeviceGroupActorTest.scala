package akka.stream

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, PoisonPill}
import akka.testkit.TestProbe
import com.kakao.nills.akka.iot.actor.{Device, DeviceGroup, DeviceManager}
import org.junit.Assert.assertNotEquals
import org.junit.Test

import scala.concurrent.duration.FiniteDuration

class DeviceGroupActorTest {

  @Test
  def register() {
    implicit val system = ActorSystem()

    val probe = TestProbe()
    val groupId = "groupId"
    val groupActor = system.actorOf(DeviceGroup.props(groupId))

    groupActor.tell(DeviceManager.RequestTrackDevice(groupId, "deviceId1"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)

    val deviceActor1 = probe.lastSender

    groupActor.tell(DeviceManager.RequestTrackDevice(groupId, "deviceId2"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)

    val deviceActor2 = probe.lastSender

    assertNotEquals(deviceActor1, deviceActor2)

    deviceActor1.tell(Device.RecordTemperature(requestId = 0, 1.0), probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(requestId = 0))
    deviceActor2.tell(Device.RecordTemperature(requestId = 1, 2.0), probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(requestId = 1))

  }

  @Test
  def register_invalid() {
    implicit val system = ActorSystem()
    val probe = TestProbe()
    val groupActor = system.actorOf(DeviceGroup.props("groupId"))
    groupActor.tell(DeviceManager.RequestTrackDevice("wrongGroupId", "deviceId"), probe.ref)
    probe.expectNoMessage(new FiniteDuration(500, TimeUnit.MILLISECONDS))
  }


  @Test
  def list() {
    implicit val system = ActorSystem()
    val probe = TestProbe()

    val groupActor = system.actorOf(DeviceGroup.props("groupId"))
    groupActor.tell(DeviceManager.RequestTrackDevice("groupId", "device1"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)
    groupActor.tell(DeviceManager.RequestTrackDevice("groupId", "device2"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)

    groupActor.tell(DeviceGroup.RequestDeviceList(1), probe.ref)
    probe.expectMsg(DeviceGroup.ReplyDeviceList(requestId = 1, Set("device1", "device2")))
  }

  @Test
  def shutdown() {
    implicit val system = ActorSystem()
    val probe = TestProbe()
    val groupActor = system.actorOf(DeviceGroup.props("group"))

    groupActor.tell(DeviceManager.RequestTrackDevice("group", "device1"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)
    val toShutDown = probe.lastSender

    groupActor.tell(DeviceManager.RequestTrackDevice("group", "device2"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)

    groupActor.tell(DeviceGroup.RequestDeviceList(requestId = 0), probe.ref)
    probe.expectMsg(DeviceGroup.ReplyDeviceList(requestId = 0, Set("device1", "device2")))

    probe.watch(toShutDown)
    toShutDown ! PoisonPill
    probe.expectTerminated(toShutDown)

    // using awaitAssert to retry because it might take longer for the groupActor
    // to see the Terminated, that order is undefined
    probe.awaitAssert {
      groupActor.tell(DeviceGroup.RequestDeviceList(requestId = 1), probe.ref)
      probe.expectMsg(DeviceGroup.ReplyDeviceList(requestId = 1, Set("device2")))
    }
  }
}
