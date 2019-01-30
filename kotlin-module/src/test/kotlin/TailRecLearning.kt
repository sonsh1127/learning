import org.junit.Assert
import org.junit.Test

class TailRecLearning {

    @Test
    fun invoke_validTailRec() {
        val sum = tailRecSum(10, 0)
        Assert.assertEquals(55, sum)
    }

    @Test
    fun invoke_invalidTailRec() {
        val sum = recSum(10)
        Assert.assertEquals(55, sum)
    }

    /**
     * this function is valid tail rec
     */
    private tailrec fun tailRecSum(n: Int, acc: Int): Int {
        return if (n == 0) {
            acc
        } else {
            tailRecSum(n - 1, acc + n)
        }
    }

    /**
     *
     */
    tailrec fun recSum(n: Int) : Int{
        return if ( n == 0) {
            0
        }else {
            n + recSum(n-1)
        }
    }
}