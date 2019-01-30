package syntax;

import org.junit.Test;

public class TryFinallyLearning {



    @Test
    public void call2() {

        call();
    }

    void call() {
        int i = 0;
        try{
            while (true) {
                if (i == 2) {
                    break;
                }
                i ++;
            }
        }finally {
            System.out.println("finally");
        }
    }

}
