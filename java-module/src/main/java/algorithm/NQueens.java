package algorithm;

import java.util.Arrays;

/**
 * Imperative NQueens implementation
 */
public class NQueens {

    private int[] queens;
    private final int SIZE;

    private static final int EMPTY = -1;

    public NQueens(int size) {
        queens = new int[size];
        this.SIZE = size;
        Arrays.fill(queens, EMPTY);
    }

    public static void main(String[] args) {
        NQueens nQueens = new NQueens(5);
        nQueens.printSolutions();
    }

    void printSolutions() {
        putQueen(0);
    }

    private void putQueen(int level) {
        if (level == SIZE) {
            printResult();
        } else {
            for (int i = 0; i < SIZE; i++) {
                if (isPossible(level, i)) {
                    queens[level] = i;
                    putQueen(level + 1);
                    queens[level] = EMPTY;
                }
            }
        }
    }

    private boolean isPossible(int level, int candidate) {
        for (int col = 0; col < level; col++) {
            if (queens[col] == candidate || level - col == Math.abs(candidate - queens[col])) {
                return false;
            }
        }
        return true;
    }

    private void printResult() {
        System.out.println("### Solution ###");
        Arrays.stream(queens).forEach( queen -> {
            String[] chess = new String[SIZE];
            Arrays.fill(chess, "#");
            chess[queen] = "Q";
            System.out.println(Arrays.toString(chess));
        });
        System.out.println();
    }

}
