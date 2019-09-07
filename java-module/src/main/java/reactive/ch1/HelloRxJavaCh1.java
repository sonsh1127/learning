package reactive.ch1;

import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class HelloRxJavaCh1 {

    @Test
    public void hello() {
        Observable obs = Observable.create(s -> {
            s.onNext("Hello world");
            s.onComplete();
        });

        obs
                .test()
                .assertResult("Hello world");
    }

    @Test
    public void sync() {
        Observable.create(s -> {
            new Thread(() -> {
                s.onNext("One");
            }).start();
        })
                .doOnNext(i -> System.out.println(Thread.currentThread()))
                .map(i -> "value " + i)
                .subscribe(r -> System.out.println(Thread.currentThread() + r));
        System.out.println("MainEnded " + Thread.currentThread());
    }

    @Test
    public void merge() throws InterruptedException {
        Observable<String> a = Observable.create(s -> new Thread(() -> {
            s.onNext("One");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.onNext("Two");
            s.onComplete();
        }).start());

        Observable<String> b = Observable.create(s -> new Thread(() -> {
            s.onNext("Three");
            s.onNext("Four");
            s.onComplete();
        }).start());

        Observable<String> c = Observable.merge(a, b);
        c.subscribe(
                r -> System.out.println(r + " " + Thread.currentThread()),
                System.out::println,
                () -> System.out.println("complete" + Thread.currentThread())
        );

        Thread.sleep(1000);
    }

    @Test
    public void cache() {
        Observable<Integer> ints = Observable.create(s -> {
            System.out.println("Create");
            s.onNext(42);
            s.onComplete();
        });

        Observable<Object> intsCached = Observable.create(s -> {
            System.out.println("Create1");
            s.onNext(42);
            s.onComplete();
        }).cache();

        ints.subscribe(s -> System.out.println("A: " + s));
        ints.subscribe(s -> System.out.println("B: " + s));
        intsCached.subscribe(s -> System.out.println("A1: " + s));
        intsCached.subscribe(s -> System.out.println("B1: " + s));
    }
}
