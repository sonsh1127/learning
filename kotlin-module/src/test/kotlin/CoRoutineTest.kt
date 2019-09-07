import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.concurrent.thread

class CoRoutineTest {

    @Test
    fun simple() {
        GlobalScope.launch {
            println("d ${Thread.currentThread().name}")
            delay(1000L)
            println("World ${Thread.currentThread().name}")
        }
        println("Hello ${Thread.currentThread().name}")
        Thread.sleep(2000L)
    }


    @Test
    fun thread() {

        thread {
            println("d ${Thread.currentThread().name}")
        }

        println("Hello ${Thread.currentThread().name}")
        Thread.sleep(2000L)
    }

    @Test
    fun mix() {
        println(Thread.currentThread().name)
        GlobalScope.launch { // launch new coroutine in background and continue
            delay(1000L)
            println("World! ${Thread.currentThread().name}" )
        }
        println("Hello,") // main thread continues here immediately
        runBlocking {     // but this expression blocks the main thread
            println("run in blocking ${Thread.currentThread().name} ${Thread.currentThread().id}")
            delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
        }
        println("main ended ${Thread.currentThread().name}")
    }

}