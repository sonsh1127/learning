package pattern.asyncmethod;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ThreadAsyncExecutor implements AsyncExecutor{

    @Override
    public <T> AsyncResult<T> startProcess(Callable<T> task) {
        return startProcess(task, null);
    }

    @Override
    public <T> AsyncResult<T> startProcess(Callable<T> task, AsyncCallback<T> callback) {
        CompletableResult<T> asyncResult= new CompletableResult(callback);
        new Thread(()-> {
            try {
                T result = task.call();
                asyncResult.setResult(result);
            } catch (Exception e) {
                asyncResult.setException(e);
            }
        }).start();

        return asyncResult;
    }

    @Override
    public <T> T endProcess(AsyncResult<T> asyncResult)
            throws ExecutionException, InterruptedException {
        if (!asyncResult.isCompleted()){
            asyncResult.await();
        }
        return asyncResult.getValue();
    }

    private static class CompletableResult<T> implements AsyncResult<T> {

        static final int RUNNING = 1;
        static final int FAILED = 2;
        static final int COMPLETED = 3;

        private volatile int state = RUNNING;

        private Object lock = new Object();
        private T result;
        private Exception exception;

        Optional<AsyncCallback> asyncCallback;

        CompletableResult(AsyncCallback<T> callback){
           this.asyncCallback = Optional.ofNullable(callback);
        }

        public void setException(Exception exception) {
            this.state = FAILED;
            this.exception = exception;

            this.asyncCallback.ifPresent(callback -> callback.onComplete(null, Optional.of(exception)));
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        public void setResult(T value) {
            this.result = value;
            this.state = COMPLETED;
            this.asyncCallback.ifPresent(callback -> callback.onComplete(result, Optional.empty()));
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        @Override
        public boolean isCompleted() {
            return state > RUNNING;
        }

        @Override
        public T getValue() throws ExecutionException {
            if (state == COMPLETED){
                return result;
            }
            else if (state == FAILED) {
                throw new ExecutionException(exception);
            } else {
                throw new IllegalStateException("Execution not completed yet");
            }
        }
        @Override
        public void await() throws InterruptedException {
            synchronized (this.lock) {
                while (state == RUNNING){
                    lock.wait();
                }
            }
        }
    }
}
