package scalabook

class Ch172 {
  private [this] var name: String = _
}

object Ch172 {
  def apply: Ch172 = {
    val test= new Ch172()
    test
  }
}
