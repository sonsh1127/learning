package scalabook

import scala.collection.mutable.ArrayBuffer
import collection.JavaConverters._
import scala.collection.mutable

object ConversionTest {
  def main(args: Array[String]): Unit = {
    val javaUtilList: java.util.List[Int] = ArrayBuffer(1,2,3).asJava
    val scalaBuf = javaUtilList.asScala
    val javaUtilMap = mutable.HashMap("first" -> 1, "second" -> 2).asJava
  }
}
