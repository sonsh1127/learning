package scalabook

object Ch26 {

  def main(args: Array[String]): Unit = {

    val x = "sonsh1127@gmail.com"

    x match {
      case Email(u, d) =>println(s"user $u domain $d")
    }
  }

}


object Email {
  // injection method
  def apply(user: String, domain: String) = user + "@" + domain

  // extractor method
  def unapply(arg: String): Option[(String, String)] = {
    val parts = arg split "@"
    if (parts.length == 2)
      Some(parts(0), parts(1))
    else None
  }
}

class Email(user: String, domain: String)
