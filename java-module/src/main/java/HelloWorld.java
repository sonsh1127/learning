import java.util.List;
import javax.swing.JButton;

public class HelloWorld {

    public static void main(String[] args) {
        new JButton("Hello world").addActionListener(e -> {
            System.out.println("clicked");
        });
    }
    static void workWithList(List<? extends Parent> list) {
    }

    static void workWithListAnc(List<? super Parent> list) {
    }
}

abstract class Ancestor {
    abstract void doWork(Object obj);
}

class Parent extends Ancestor {
    @Override
    void doWork(Object obj) {
    }
}

class Child extends Parent {
}
