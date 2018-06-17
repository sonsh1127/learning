package algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class Festival {

    static int C, N, L;

    static int[] partialSum;

    public static void main(String[] args) throws Throwable, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        C = Integer.parseInt(br.readLine());

        for (int test = 1; test <= C; test++) {

            String[] line = br.readLine().split(" ");

            N = Integer.parseInt(line[0]);
            L = Integer.parseInt(line[1]);

            partialSum = new int[N+1];

            line = br.readLine().split(" ");

            int sum = 0;
            for (int i = 0; i < N; i++) {
                int cost = Integer.parseInt(line[i]);
                sum += cost;
                partialSum[i+1] = sum;
            }

            double min = getMinAverage(L);

            System.out.println(min);
        }
    }

    public static double getMinAverage(int l) {
        double minAverage = Double.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            for (int j = i + l - 1; j < N; j++) {
                int sum = partialSum[j+1] - partialSum[i];
                double len = j - i + 1;
                minAverage = Math.min(minAverage, sum / len);
            }
        }
        return minAverage;
    }

}
