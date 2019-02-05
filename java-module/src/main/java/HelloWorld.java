import java.util.Arrays;
import java.util.List;

public class HelloWorld {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        integers.forEach(e ->
        {
            System.out.println(e);
            return;
        });

        for (Integer i : integers) {
            if (i > 1) {
                break;
            }

            System.out.println(i);
        }
    }
}


