package cleancode.ch4;

public class MyPrimeGenerator {

    public static int[] generatePrimes(int maxValue) {
        if (maxValue >= 2) { // 유일하게 유효한 경우
            return calculatePrimes(maxValue);
        } else {
            return new int[0];
        }

    }

    private static int[] calculatePrimes(int maxValue) {
        boolean[] isPrime = initIsPrime(maxValue);

        checkPrime(maxValue, isPrime);

        int count = getPrimeCount(isPrime);
        return createResult(isPrime, count);
    }

    private static boolean[] initIsPrime(int maxValue) {
        boolean[] isPrime = new boolean[maxValue + 1];

        for (int i = 2; i < isPrime.length; i++) {
            isPrime[i] = true;
        }
        return isPrime;
    }

    private static void checkPrime(int maxValue, boolean[] isPrime) {
        for (int i = 2; i < Math.sqrt(maxValue + 1) + 1; i++) {
            if (isPrime[i]) {
                markNotPrime(maxValue, isPrime, i);
            }
        }
    }

    private static int[] createResult(boolean[] isPrime, int count) {
        int[] primes = new int[count];
        for (int i = 0, j = 0; i < isPrime.length; i++) {
            if (isPrime[i]) {
                primes[j++] = i;
            }
        }
        return primes;
    }

    private static int getPrimeCount(boolean[] isPrime) {
        int count = 0;
        for (int i = 0; i < isPrime.length; i++) {
            if (isPrime[i]) {
                count++; // 카운트 증가
            }
        }
        return count;
    }

    private static void markNotPrime(int maxValue, boolean[] isPrime, int i) {
        for (int j = 2 * i; j < maxValue + 1; j += i) {
            isPrime[i] = false;
        }
    }

}
