package concurrent;

import java.util.stream.IntStream;

public class PrintlnFun {
    public static void main(String[] args) {
        System.out.println("Hello world");
        IntStream.range(0, 4).parallel().forEach(
                System.out::println
        );
    }
}
