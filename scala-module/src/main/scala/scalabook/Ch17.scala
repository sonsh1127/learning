package scalabook

import scala.collection.mutable.ListBuffer

object Ch17 {
  def main(args: Array[String]): Unit = {
    val buf = new ListBuffer[Int]
    buf += 1
    2 +=: buf
    println(buf)
    println(countWords("See Spot run! Run, Spot. Run!"))
  }

  def hasUpperCase(s: String) = s.exists(_.isUpper)

  def mapTest() {


    val listMap = ("A"->List("key1", "key2"))
  }

  def countWords(text: String) = {
    val counts = scala.collection.mutable.Map.empty[String, Int]
    for (rawWord <- text.split("[,!.]+")) {
      val word = rawWord.toLowerCase
      val oldCount = if (rawWord.contains(word)) counts(word)
      else 0
      counts += (word -> (oldCount+1))
    }
    counts
  }

}




