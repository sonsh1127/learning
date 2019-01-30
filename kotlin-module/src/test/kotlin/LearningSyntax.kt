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
}