import javax.lang.model.element.NestingKind

object CaseClassLearningTest extends App{

  //new keyword is unnecessary
  val emailFromJohn = Email("john@daum.net","Greetings from John", "Hello world")

  // constructor parametr is public
  val title = emailFromJohn.title
  println(title)
  //

  val editedEmail = emailFromJohn.copy(title = "new Title")
  println(editedEmail)
  println(emailFromJohn)

  ///structural equality
  val first = SMS("9410", "hello")
  val second = SMS("9410", "hello")

  println(first.equals(second))
  println(first == second)

  //pattern matching
  val someSms = SMS("12345", "Are you there?")
  val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")

  println(showNotification(someSms))
  println(showNotification(someVoiceRecording))

  def showNotification(noti: Notification): String ={
    noti match {
      case Email(email, title, _) => "Email from " + email + "title: " + title
      case SMS(number, message) => "SMS from " + number
      case VoiceRecording(name, link) => "you received " + name
    }
  }

}

abstract class Notification

case class Email(sourceEmail: String, title : String, body: String) extends Notification

case class SMS(sourceNumber: String, messsage: String) extends Notification

case class VoiceRecording(contactName: String, link: String) extends Notification


