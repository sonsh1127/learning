package algorithm;

import java.util.Arrays;

public class PrimeSequenceGenerator {

    public static boolean[] generate(int n) {
        if (n < 2) {
            throw new IllegalArgumentException("argument must be more than 2");
        }

        boolean[] result = new boolean[n + 2];
        Arrays.fill(result, true);
        int max = (int) Math.sqrt(n);

        for (int i = 2; i <= max; i++) {
            if (result[i]) {
                markToFalse(result, i);
            }
        }
        return result;
    }

    private static void markToFalse(boolean[] result, int step) {
        for (int i = step * 2; i < result.length; i += step) {
            result[i] = false;
        }
    }
}
