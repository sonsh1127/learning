import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;

public class HelloWorld {
    public static void main(String[] args) {
        new JButton("Hello world").addActionListener(e -> {
            System.out.println("clicked");
        });
    }
}


