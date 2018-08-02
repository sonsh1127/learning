package akka

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.iot.actor.{Device, DeviceManager}
import akka.testkit.TestProbe
import org.junit.Assert._
import org.junit.Test

import scala.concurrent.duration.FiniteDuration

class DeviceActorTest {

  @Test
  def readTemperature() {
    implicit val actorSystem = ActorSystem()
    val probe = TestProbe()
    val deviceActor = actorSystem.actorOf(Device.props("group", "device"))
    deviceActor.tell(Device.ReadTemperature(requestId = 42L), probe.ref)

    val response = probe.expectMsgType[Device.RespondTemperature]
    assertEquals(42L, response.requestId)
    assertEquals(None, response.value)
  }

  @Test
  def recordTemperature() {

    implicit val system = ActorSystem()

    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

    deviceActor.tell(Device.RecordTemperature(requestId = 1, 24.0), probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(requestId = 1))

    deviceActor.tell(Device.ReadTemperature(requestId = 2), probe.ref)
    val response1 = probe.expectMsgType[Device.RespondTemperature]
    assertEquals(2, response1.requestId)
    assertEquals(Some(24.0), response1.value)

    deviceActor.tell(Device.RecordTemperature(requestId = 3, 55.0), probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(requestId = 3))

    deviceActor.tell(Device.ReadTemperature(requestId = 4), probe.ref)
    val response2 = probe.expectMsgType[Device.RespondTemperature]
    assertEquals(response2.requestId, 4)
    assertEquals(response2.value, Some(55.0))
  }

  @Test
  def register() {
    implicit val system = ActorSystem()
    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))
    deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "device"), probe.ref)
    probe.expectMsg(DeviceManager.DeviceRegistered)

    assertEquals(deviceActor, probe.lastSender)
  }

  @Test
  def register_invalid() {
    implicit val system = ActorSystem()
    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

    deviceActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device"), probe.ref)
    probe.expectNoMessage(new FiniteDuration(500, TimeUnit.MILLISECONDS))

    deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "wrongdevice"), probe.ref)
    probe.expectNoMessage(new FiniteDuration(500, TimeUnit.MILLISECONDS))
  }
}



