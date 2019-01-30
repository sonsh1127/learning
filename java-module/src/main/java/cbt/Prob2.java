package cbt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.junit.Assert;

public class Prob2 {

    private static final int FLAT = 1;
    private static final int OBSTRACLE = 9;
    private static final int NOPASS = 0;

    public static void main(String[] args) {
        List<List<Integer>> paths = new ArrayList<>();

        paths.add(Arrays.asList(1, 1, 1, 1));
        paths.add(Arrays.asList(1, 0, 1, 1));
        paths.add(Arrays.asList(1, 0, 0, 9));

        Assert.assertEquals(5, new Prob2().shortestDistance(paths, 3, 4));
    }

    public int shortestDistance(List<List<Integer>> paths, int numberOfRows, int numberOfCols) {
        return new Graph(numberOfRows, numberOfCols, paths).bfs();
    }

    class Graph {

        int rows, cols;
        int[][] graph;

        public Graph(int rows, int cols, List<List<Integer>> paths) {
            this.rows = rows;
            this.cols = cols;
            this.graph = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    graph[i][j] = paths.get(i).get(j);
                }
            }
        }

        int bfs() {
            Queue<Point> queue = new LinkedList<>();

            boolean[][] discovered = new boolean[rows][cols];
            queue.add(new Point(0, 0));
            discovered[0][0] = true;

            while (!queue.isEmpty()) {
                Point current = queue.poll();

                if (graph[current.x][current.y] == OBSTRACLE) {
                    return current.distance;
                }

                List<Point> adjList = adjList(current, discovered);
                for (Point p : adjList) {
                    p.distance = current.distance + 1;
                    discovered[p.x][p.y] = true;
                    queue.add(p);
                }
            }
            return -1;
        }


        private List<Point> adjList(Point p, boolean[][] discovered) {
            List<Point> list = new ArrayList<>();

            if (p.x > 0 && graph[p.x - 1][p.y] != NOPASS && !discovered[p.x - 1][p.y]) {
                list.add(new Point(p.x - 1, p.y));
            }

            if (p.x < rows - 1 && graph[p.x + 1][p.y] != NOPASS && !discovered[p.x + 1][p.y]) {
                list.add(new Point(p.x + 1, p.y));
            }

            if (p.y > 0 && graph[p.x][p.y - 1] != NOPASS && !discovered[p.x][p.y - 1]) {
                list.add(new Point(p.x, p.y - 1));
            }

            if (p.y < cols - 1 && graph[p.x][p.y + 1] != NOPASS && !discovered[p.x][p.y + 1]) {
                list.add(new Point(p.x, p.y + 1));
            }

            return list;
        }
    }

    class Point {

        int x;
        int y;

        int distance = 0;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", distance=" + distance +
                    '}';
        }
    }
}
