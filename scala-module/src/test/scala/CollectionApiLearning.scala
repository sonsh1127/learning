import org.junit.Test

class CollectionApiLearning {

  @Test
  def defaultApis() {
    val list = List(1, 2, 3, 4)
    val twoToFive = list.map(_ + 1)
    println(twoToFive)
    val res = list.flatMap(1 to _)
    println(res)
    val res2 = list.map(1 to _)
    println(res2)
    val scan = list.scan(0)(_ + _)
    println(scan)
  }
}