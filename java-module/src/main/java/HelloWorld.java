import java.util.List;
import javax.swing.JButton;

public class HelloWorld {
    public static void main(String[] args) {
        new JButton("Hello world").addActionListener(e -> {
            System.out.println("clicked");
        });
    }
}


