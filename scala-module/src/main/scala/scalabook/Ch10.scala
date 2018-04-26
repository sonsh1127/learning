package scalabook

object Ch10 {
  def main(args: Array[String]): Unit = {

  }
}

abstract class Element {
  def contents: Array[String]

  def height: Int = contents.length

  def width: Int = if (height == 0) 0 else contents(0).length

  def above(that: Element): Element = {
    val elem1 = this.widen(that.width)
    val elem2 = that.widen(this.width)
    Element.elem(elem1.contents ++ elem2.contents)
  }

  def beside(that: Element): Element = {
    val this1 = this.heighten(that.height)
    val that1 = that.heighten(this.height)

    Element.elem(
      for (
        (line1, line2) <- this1.contents zip that1.contents
      ) yield line1 + line2
    )
  }

  def widen(w: Int): Element = {
    if (w <= width) {
      this
    } else {
      val left = Element.elem(' ', (w - width) / 2, height)
      val right = Element.elem(' ', w - left.width - left.width, height)
      left beside this beside right
    }
  }

  def heighten(h: Int): Element = {
    if (h <= height) {
      this
    } else {
      val top = Element.elem(' ', width, (h - height) / 2)
      val bottom = Element.elem(' ', width, h - height - top.height)
      top above this above bottom
    }
  }

  override def toString: String = contents mkString "\n"
}

object Element {

  def elem(contents: Array[String]): Element =
    new ArrayElement(contents)

  def elem(line: String): Element =
    new LineElement(line)

  def elem(ch: Char, width: Int, height: Int): Element =
    new UniformElement(ch, width, height)

  private class LineElement(s: String) extends ArrayElement(Array(s)) {
    override def height = 1

    override def width: Int = s.length
  }

  private class ArrayElement(cont: Array[String]) extends Element {
    val contents = cont
  }

  private class UniformElement(ch: Char,
                               override val width: Int,
                               override val height: Int) extends Element {
    private val line = ch.toString * width

    def contents = Array.fill(height)(line)
  }

}

object Sprial {
  def sprial(nEdges: Int, direction: Int): Element = {
    val space = Element.elem(" ")
    val corner = Element.elem("+")
    if (nEdges == 1) {
      Element.elem("+")
    } else {
      val sp = sprial(nEdges-1, (direction+3) % 4)
      def verticalBar = Element.elem('|', 1, sp.height)
      def horizontalBar = Element.elem('-', sp.width, 1)

      null
    }
  }

  def main(args: Array[String]): Unit = {
    val length = args(0).toInt
    println(sprial(length, 0))
  }
}

class MyClass {
  val f: String = "hello"
}

