package akka

import akka.actor.ActorSystem
import akka.iot.Device
import akka.testkit.TestProbe
import org.junit.Assert._
import org.junit.{Assert, Test}

class IotActorTest {

  @Test
  def run() {
    implicit val actorSystem = ActorSystem()
    val probe = TestProbe()
    val deviceActor = actorSystem.actorOf(Device.props("group", "device"))
    deviceActor.tell(Device.ReadTemperature(requestId = 42L), probe.ref)

    val response = probe.expectMsgType[Device.RespondTemperature]
    assertEquals(42L, response.requestId)
    assertEquals(None, response.value)
  }

  @Test
  def record() {

    implicit val system = ActorSystem()

    val probe = TestProbe()
    val deviceActor = system.actorOf(Device.props("group", "device"))

    deviceActor.tell(Device.RecordTemperature(requestId = 1, 24.0), probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(requestId = 1))

    deviceActor.tell(Device.ReadTemperature(requestId = 2), probe.ref)
    val response1 = probe.expectMsgType[Device.RespondTemperature]
    Assert.assertEquals(2, response1.requestId)
    Assert.assertEquals(Some(24.0), response1.value)

    deviceActor.tell(Device.RecordTemperature(requestId = 3, 55.0), probe.ref)
    probe.expectMsg(Device.TemperatureRecorded(requestId = 3))

    deviceActor.tell(Device.ReadTemperature(requestId = 4), probe.ref)
    val response2 = probe.expectMsgType[Device.RespondTemperature]
    Assert.assertEquals(response2.requestId, 4)
    Assert.assertEquals(response2.value, Some(55.0))
  }

}
