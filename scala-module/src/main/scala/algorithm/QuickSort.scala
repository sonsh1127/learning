package algorithm

object QuickSort {


  def sort(list: List[Int]): List[Int] = {
    list match {
      case pivot :: tail => {
        val lessAndMore = tail.partition(_ < pivot)
        sort(lessAndMore._1) ++ List(pivot) ++ sort(lessAndMore._2)
      }
      case Nil => Nil
    }
  }

  def main(args: Array[String]): Unit = {
    val nins = List(555, 1, 4, 4, 7, -1, 16)
    val res = sort(nins)
    println(res)
  }

}
