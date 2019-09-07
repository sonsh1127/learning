package learning

import java.io.File

import scala.io.Source

object IteratorTest {

  def main(args: Array[String]): Unit = {

    val source = Source.fromFile(new File("./wiki-dump.xml")).getLines()

    /*val itrs = source.grouped(100)

    itrs.foreach(it =>
      println(it.toList)
    )*/

  }

}
