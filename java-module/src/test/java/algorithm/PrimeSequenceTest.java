package algorithm;

import org.junit.Assert;
import org.junit.Test;

public class PrimeSequenceTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalid() {
        PrimeSequenceGenerator.generate(1);
    }

    @Test
    public void simple() {
        boolean[] isPrimes = PrimeSequenceGenerator.generate(2);
        Assert.assertTrue(isPrimes[2]);
    }

    @Test
    public void complex() {
        boolean[] isPrimes = PrimeSequenceGenerator.generate(99);
        Assert.assertTrue(isPrimes[2]);
        Assert.assertTrue(isPrimes[13]);
        Assert.assertFalse(isPrimes[26]);
    }

}
