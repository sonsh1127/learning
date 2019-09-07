import org.junit.Test

class LearningSyntax {

    @Test
    fun countingWord() {
        val list = listOf("apple", "banana", "carrot", "apple")
        val map = list
                .groupBy { it }
                .mapValues { it -> it.value.size }
        println(map)
    }

    @Test
    fun testDebug() {


        val nins = Nins("ninnede")

        nins.doWork("manjin")


    }
}

class Nins(val name: String) {

    val age = 10

    fun doWork(param: String) {

        val list = listOf("apple", "banana", "carrot", "apple").asSequence()
        val map = list
                .groupBy { it }
                .mapValues { it -> it.value.size }

    }
}