package scalabook

class Ch18 {


}

abstract class Simulation {
  type Action = () => Unit

  case class WorkItem(time: Int, action: Action)

  private var curTime = 0

  def currentTime: Int = curTime

  private var agenda: List[WorkItem] = List()

  private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || ag.head.time < item.time) {
      item :: ag
    } else {
      ag.head :: insert(ag.tail, item)
    }
  }

  def afterDelay(delay: Int)(block: => Unit) {
    val item = WorkItem(currentTime + delay, () => block)
    insert(agenda, item)
  }

  private def next() {
    (agenda) match {
      case item :: rest =>
        agenda = rest
        curTime = item.time
        item.action()
    }
  }

  def run() {
    afterDelay(0) {
      println("simulation started, time = " + currentTime + "***")
    }

    while (!agenda.isEmpty) next()
  }

}

abstract class BasicCircuitSimulation extends Simulation {

  def InverterDelay: Int

  def AndGateDelay: Int

  def OrGateDelay: Int

  class Wire {
    private var sigVal = false
    private var actions: List[Action] = List()

    def getSignal = sigVal

    def setSigVal(sig: Boolean) = {
      if (sig != sigVal) {
        sigVal = sig
        actions.foreach(a => a())
      }
    }

    def addAction(a: Action) = {
      actions = a :: actions
      a()
    }
  }

  def inverter(input: Wire, output: Wire) {
    def inverterAction() {
      val inputSig = input.getSignal
      afterDelay(InverterDelay) {
        output setSigVal !inputSig
      }
    }

    input addAction inverterAction
  }

  def andGate(i1: Wire, i2: Wire, output: Wire) {
    def andAction() {
      val i1Sig = i1.getSignal
      val i2Sig = i2.getSignal
      afterDelay(AndGateDelay) {
        output setSigVal i1Sig & i2Sig
      }
    }

    i1.addAction(andAction)
    i2.addAction(andAction)

  }

  def orGate(i1: Wire, i2: Wire, output: Wire) {
    def orAction() {
      val i1Sig = i1.getSignal
      val i2Sig = i2.getSignal
      afterDelay(OrGateDelay) {
        output setSigVal i1Sig | i2Sig
      }
    }

    i1.addAction(orAction)
    i2.addAction(orAction)
  }

  def probe(name: String, wire: Wire) {
    def probeAction() {
      println(s"$name currentTime new-value ${wire.getSignal}")
    }

    wire addAction probeAction
  }


}

abstract class CircutSimulation extends BasicCircuitSimulation {
  def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire) {
    val d, e = new Wire()
    orGate(a, b, d)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }

  def fullAdder(a: Wire, b: Wire, cin: Wire, sum: Wire, cout: Wire) {
    val s, c1, c2 = new Wire
    halfAdder(a, cin, s, c1)
    halfAdder(b, s, sum, c2)
    orGate(c1, c2, cout)
  }
}

object MySimulation extends CircutSimulation {
  override def InverterDelay = 1

  override def AndGateDelay = 3

  override def OrGateDelay = 5

  def main(args: Array[String]): Unit = {

    val input1, input2, sum, carry: MySimulation.Wire = new Wire
    probe("sum", sum)
    probe("carry", carry)

    halfAdder(input1, input2, sum, carry)

    input1 setSigVal (true)
    MySimulation.run()

  }
}

object Driver {
  def main(args: Array[String]): Unit = {

  }
}
