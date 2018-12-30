package reactive.ch7;

import io.reactivex.Observable;
import org.junit.Test;

public class ErrorHandling {

    @Test
    public void subscribeUnhandledException() {
        Observable
                .create(subscriber -> {
                    try {
                        subscriber.onNext(1 / 0);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                })
                .subscribe(System.out::println);
    }

    @Test
    public void unCaughtExceptionOnCreate() {
        Observable
                .create(subscriber ->
                        subscriber.onNext(1 / 0)
                )
                .subscribe(System.out::println);
    }

    @Test
    public void fromCallable() {
        // using fromCallble, you don't need to exception manually
        Observable.fromCallable(() -> 1 / 0)
                .subscribe(System.out::println);
    }

    @Test
    public void exceptionOnOperator() {
        Observable
                .just(1, 0)
                .map(x -> 10 / x)
                .subscribe(System.out::println,
                        e -> {
                        });
    }

    @Test
    public void exceptionOnOperator2() {
        Observable
                .just(1, 0)
                .flatMap( x -> x == 0
                ? Observable.error(new ArithmeticException("nins"))
                        : Observable.just(10 / x))
                .subscribe(System.out::println);
    }

}
