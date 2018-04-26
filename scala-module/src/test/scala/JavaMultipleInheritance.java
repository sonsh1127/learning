import java.util.Arrays;
import java.util.List;
import scalabook.MyClass;

public class JavaMultipleInheritance {

    public static void main(String[] args) {
        Myclass myClass = new Myclass();
        myClass.doWork("");

        List<Integer> list = Arrays.asList(33, 3, 209);

        int val = list.stream().mapToInt(value -> value).sum();

    }
}


interface Increment {
    default void doWork(String name){
        System.out.println("dowork");
    }
}
class Myclass implements Increment{

}

interface Doubling extends Increment{
    default void doWork(String name){
        System.out.println("doubling");
    }
}
