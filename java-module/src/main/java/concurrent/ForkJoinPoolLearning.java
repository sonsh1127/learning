package concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class ForkJoinPoolLearning {

    public static void main(String[] args) {

        // instantiated ForkJoinPool
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        MyRecursiveAction recursiveAction = new MyRecursiveAction("hello world my name is nins. nice to see you");
        commonPool.execute(recursiveAction);

        int[] arr = new int[25];

        Random random = new Random();

        for (int i=0; i < arr.length; i++){
            arr[i] = random.nextInt(50);
        }

        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(arr);
        commonPool.submit(myRecursiveTask);

        int result = myRecursiveTask.join();
        System.out.println(result);



        //
    }

}


class MyRecursiveTask extends RecursiveTask<Integer> {

    private int[] arr;

    private static final int THRESHOLD = 10;

    public MyRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD){
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        }else{
           return process(this.arr);
        }
    }

    private List<MyRecursiveTask> createSubtasks(){

        List<MyRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new MyRecursiveTask(
                Arrays.copyOfRange(arr, 0, arr.length / 2)));
        dividedTasks.add(new MyRecursiveTask(
                Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
        return dividedTasks;
    }

    Integer process(int[] arr) {
        return Arrays.stream(arr).filter(a -> a > 10 && a < 27)
                .map(a -> a * 10).sum();
    }
}


// in case of no return void
class MyRecursiveAction extends RecursiveAction {

    private String workload = "";

    public static final int THRESHOLD = 20;

    private Logger logger =
            Logger.getAnonymousLogger();

    public MyRecursiveAction(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {

        if (workload.length() > THRESHOLD){
            ForkJoinTask.invokeAll(createSubTasks());
        }else{
            processing(workload);
        }
    }

    private List<MyRecursiveAction> createSubTasks() {

        List<MyRecursiveAction> subTasks = new ArrayList<>();
        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2, workload.length());

        subTasks.add(new MyRecursiveAction((partOne)));
        subTasks.add(new MyRecursiveAction((partTwo)));

        return subTasks;
    }

    private void processing(String item) {

        String result = item.toUpperCase();

        System.out.println("This result - (" + result + ") - was processed by "
                + Thread.currentThread().getName());
        logger.info("This result - (" + result + ") - was processed by "
                + Thread.currentThread().getName());

    }
}
