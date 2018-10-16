package reactive.ch4;

import java.util.LinkedList;
import java.util.Queue;

class TrampolineWorker {

    private Queue<Runnable> queue = new LinkedList<>();

    public void schedule(Runnable task) {
        queue.add(task);
    }

    public void execute() {
        while (!queue.isEmpty()) {
            queue.poll().run();
        }
    }
}
