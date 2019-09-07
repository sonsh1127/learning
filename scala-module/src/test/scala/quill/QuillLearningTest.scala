package quill

import com.typesafe.config.ConfigFactory
import io.getquill._
import org.junit.{AfterClass, BeforeClass, Test}

class QuillLearningTest {


  val ctx = QuillLearningTest.ctx;

  @Test
  def ioMonad() {

    import ctx._

    val p = Person(0, "John", 22)

    val a =
      ctx.runIO(query[Person].insert(lift(p))).flatMap { _ =>
        ctx.runIO(query[Person])
      }

   val allPeople = ctx.runIO(query[Person])

    val b =
      ctx.runIO(query[Person].insert(lift(p))).flatMap { _ =>
        allPeople
      }
    // produce the same result when executed

    println(performIO(a) == performIO(b))
    //println(resa)
    //println(resb)
  }

  @Test 
  def sqlMirrorContext() {
    val ctx = new SqlMirrorContext(MirrorSqlDialect, Literal)
    import ctx._

    val actionMirror = run(
      query[Person].insert(Person(1, "test", 2))
    )
    println(actionMirror)

    val queryMirror = ctx.run(
      query[Person].filter(_.id == 1)
    )

    println(queryMirror)
  }

  @Test
  def nonMonad() {

    import ctx._

    val p = Person(0, "John", 22)

    ctx.run(query[Person].insert(lift(p)))


    // isn't referentially transparent because if you refactor the second database
    // interaction into a value, the result will be different:
    val allPeople = ctx.run(query[Person])
    ctx.run(query[Person].insert(lift(p)))
  }

}

case class Person(id: Int, name: String, age: Int)

object QuillLearningTest {

  lazy val config = ConfigFactory.parseString(
    s"""{"driverClassName": "org.h2.Driver", "jdbcUrl":"jdbc:h2:mem:${getClass.getSimpleName};USER=sa;PASSWORD="} """
  )

  lazy val ctx = new H2JdbcContext(
    SnakeCase,
    config
  )

  @BeforeClass
  def setup() {

    val createPerson =
      """create table PERSON(
        |ID decimal(20) ,
        |NAME varchar(100),
        |AGE decimal(10)
        |)""".stripMargin

    ctx.executeAction(createPerson)
  }

  @AfterClass
  def teardown() {
    ctx.close()
  }
}
