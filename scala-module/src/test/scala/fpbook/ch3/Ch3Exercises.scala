package fpbook.ch3

import org.junit.Assert._
import org.junit.{Assert, Test}

class Ch3Exercises {

  @Test
  def prob1_caseMatch() {
    val x = List(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }

    assertEquals(3, x)
  }

  @Test
  def prob2_tail() {
    val xs = List(1, 2, 3)
    assertEquals(List(2, 3), List.tail(xs))
  }

  @Test
  def prob3_setHead() {
    val xs = List(1, 2, 3)
    assertEquals(List(4, 2, 3), List.setHead(4, xs))
  }

  @Test
  def prob4_drop() {

    val xs = List(1, 2, 3)
    assertEquals(List(2, 3), List.drop(xs, 1))
    assertEquals(List(3), List.drop(xs, 2))
    assertEquals(Nil, List.drop(xs, 3))
    assertEquals(Nil, List.drop(xs, 4))
  }

  @Test
  def prob5_dropWhile() {
    val xs = List(1, 3, 5, 8)
    assertEquals(List(8), List.dropWhile(xs, (x: Int) => x % 2 == 1))
  }

  @Test
  def prob6_init() {
    val xs = List(1, 2, 3, 4)
    assertEquals(List(1, 2, 3), List.init(xs))
  }

  @Test
  def prob7_foldRight() {
    val xs = List(1.0, 2.0, 3.0, 0, 4.0)
    val double = List.product2(xs)

  }

  @Test
  def prob8_foldRightCons() {
    val res = List.foldRight(List(1, 2, 3), Nil: List[Int])((x, xs) => Cons(x, xs))
    println(res)
  }

  @Test
  def prob9_length() {
    val len = List.length(List(1, 2, 3))
    assertEquals(3, len)
  }

  @Test
  def prob10_foldLeft() {
    //not implemented
  }

  @Test
  def prob11_sumUsingFoldLeft() {
    val sum = List.sum3(List(1, 2, 3))
    assertEquals(6, sum)
  }

  @Test
  def prob12_reverse() {
    val reversed = List.reverse(List(1, 2, 3))
    assertEquals(List(3, 2, 1), reversed)
  }

  @Test
  def append() {
    val xs = List(1, 2, 3)
    val ys = List(4, 5, 6)

    val zs = List.append(xs, ys)
    assertEquals(List(1, 2, 3, 4, 5, 6), zs)
  }

  @Test
  def prob13_foldLeftViaFoldRight() {
    val x = List("1", "2", "3")
    val res = List.foldLeftViaFoldRight(x, Nil: List[String])((xs, x) => Cons(x, xs))
    assertEquals(List("3", "2", "1"), res)
  }

  @Test
  def prob13_foldRightViaFoldLeft() {
    val x = List("1", "2", "3")
    val res = List.foldRightViaFoldLeft(x, Nil: List[String])(Cons(_, _))
    assertEquals(List("1", "2", "3"), res)
  }

  @Test
  def prob14_appendViaFold() {

    val list = List.append_viaFold(List(1, 2, 3), List(4, 5))
    assertEquals(List(1, 2, 3, 4, 5), list)
  }

  @Test
  def prob15_flatten() {
    val listList = List(List(1, 2), Nil, List(3, 4))
    val flatten = List.flatten(listList)
    println(flatten)
  }

  @Test
  def prob16_addOne() {
    val list = List.addOne(List(1, 2, 3))
    val list2 = List.addOne2(List(1, 2, 3))
    assertEquals(List(2, 3, 4), list)
    assertEquals(List(2, 3, 4), list2)
  }

  @Test
  def prob17_doubleToString() {
    val list = List.mapToString(List(1.1, 2.2))
    val list2 = List.mapToString2(List(1.1, 2.2))

    assertEquals(List("1.1", "2.2"), list)
    assertEquals(List("1.1", "2.2"), list2)
  }

  @Test
  def prob18_map() {
    val mapped = List.map(List(1, 2, 3))(_ * 2)
    assertEquals(List(2, 4, 6), mapped)
  }

  @Test
  def prob19_filter() {
    val filtered = List.filter(List(1, 2, 3))(_ % 2 == 1)
    assertEquals(List(1, 3), filtered)
    val filtered2 = List.filter2(List(1, 2, 3))(_ % 2 == 1)
    assertEquals(List(1, 3), filtered2)
  }

  @Test
  def prob20_flatMap() {
    val list = List(1, 2, 3)
    val res = List.flatMap(list)(x => if (x % 2 == 0) Nil else List(x, x))
    println(res)
  }

  @Test
  def prob21_filterViaFlatMap() {
    val filtered = List.filterViaFlatMap(List(1, 2, 3))(_ % 2 == 1)
    assertEquals(List(1, 3), filtered)
  }

  @Test
  def prob22_listAdd() {
    val xs = List(1, 2, 3)
    val ys = List(4, 5, 6)
    val res = List.addList(xs, ys)
    assertEquals(List(5, 7, 9), res)
  }

  @Test
  def prob23_zipWith() {
    val xs = List(1, 2, 3)
    val ys = List(4, 5, 6)
    val res = List.zipWith(xs, ys)((x, y) => x * y)
    assertEquals(List(4, 10, 18), res)
  }

  @Test
  def prob24_hasSubsequence() {
    val sup = List(1, 2, 3, 4)
    assertEquals(true, List.hasSubsequence(sup, List(1, 2)))
    assertEquals(true, List.hasSubsequence(sup, List(2, 3)))
    assertEquals(true, List.hasSubsequence(sup, List(4)))

    assertEquals(true, List.hasSubsequence2(sup, List(1, 2)))
    assertEquals(true, List.hasSubsequence2(sup, List(2, 3)))
    assertEquals(true, List.hasSubsequence2(sup, List(4)))

    assertEquals(true, List.hasSubsequence(sup, List(1, 3)))
    assertEquals(false, List.hasSubsequence2(sup, List(1, 3)))
    assertEquals(false, List.hasSubsequence(sup, List(1, 5)))
  }

  @Test
  def prob25_size() {
    val tree = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))
    assertEquals(5, Tree.size(tree))
    assertEquals(5, Tree.sizeViaFold(tree))
  }

  @Test
  def prob26_maximum() {
    val tree = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))
    assertEquals(3, Tree.maximum(tree))
    assertEquals(3, Tree.maximumViaFold(tree))
  }

  @Test
  def prob27_depth() {
    val tree = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))
    assertEquals(2, Tree.depth(tree))
    assertEquals(2, Tree.depthViaFold(tree))
  }

  @Test
  def prob28_map() {
    val tree = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))
    val mappedTree = Tree.map(tree)(_ * 2)
    val mappedTree2 = Tree.mapViaFold(tree)(_ * 2)
    assertEquals(6, Tree.maximum(mappedTree))
    assertEquals(6, Tree.maximum(mappedTree2))
  }

  @Test
  def prob29_fold() {
    val tree = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))
    val sum = Tree.fold(tree)(x => x)((x, y) => x + y)
    assertEquals(6, sum)
  }

}
