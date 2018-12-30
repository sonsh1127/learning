package reactive.ch7;

import io.reactivex.Observable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Test;

public class ErrorHandlingDeclaredWay {

    @Test
    public void timeout() {
        Observable<Confirmation> confirmation = confirmation();
        confirmation
                .timeout(210, TimeUnit.MILLISECONDS)
                .subscribe(
                        System.out::println,
                        th -> {
                            if (th instanceof TimeoutException) {
                                System.out.println("Too Long");
                            } else {
                                th.printStackTrace();
                            }
                        },
                        () -> System.out.println("nills")

                );

        sleepFor(1000);
    }

    @Test
    public void enhancedTimeout() {

        nextSolarEclipseDate(LocalDate.of(2016, Month.SEPTEMBER, 1))
                .timeInterval()
                .timeout(
                        Observable.timer(1000, TimeUnit.MILLISECONDS),
                        date -> Observable.timer(100, TimeUnit.MILLISECONDS)
                ).subscribe(System.out::println);

        int millis = 10_000;
        sleepFor(millis);
    }

    private void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void retry() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(th -> System.err.println("will retry" + th))
                .retry(5)
                .subscribe(System.out::println);

        int millis = 10_000;
        sleepFor(millis);
    }

    @Test
    public void retryWithException() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(th -> System.err.println("will retry" + th))
                .retry((attempt, e) -> attempt < 10 && e instanceof TimeoutException)
                .subscribe(System.out::println);

        int millis = 10_000;
        sleepFor(millis);
    }

    @Test
    public void retryWhen() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(th -> System.err.println("will retry" + th))
                .retryWhen(failures -> failures.delay(1, TimeUnit.SECONDS))
                .subscribe(System.out::println);

        int millis = 10_000;
        sleepFor(millis);
    }


    static final int ATTEMPTS = 11;

    @Test
    public void retryWhenWithExponentialBackoff() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(th -> System.err.println(new Date()  + " will retry" + th))
                .retryWhen(failures -> {
                    Observable<Observable<Long>> observable = failures.zipWith(
                            Observable.range(1, ATTEMPTS),
                            this::handleRetryAttempt
                    );
                    return observable.flatMap(x -> x);
                })
                .subscribe(System.out::println);

        int millis = 40_000;
        sleepFor(millis);
    }

    private Observable<Long> handleRetryAttempt(Throwable error, int attempt) {
        switch (attempt) {
            case 1:
                return Observable.just(42L);
            case ATTEMPTS:
                return Observable.error(error);
            default:
                long expectDelay = (long) Math.pow(2, attempt - 1);
                return Observable.timer(expectDelay, TimeUnit.SECONDS);
        }
    }

    @Test
    public void timer() {
        Observable
                .timer(1, TimeUnit.SECONDS)
                .subscribe(zero -> System.out.println(zero),
                        th -> {
                        },
                        () -> System.out.println("completed")
                );
        sleepFor(3_000);
    }

    private Observable<String> risky() {
        return Observable.fromCallable(
                () -> {
                    if (Math.random() < 0.1) {
                        Thread.sleep((long) (Math.random() * 2000));
                        return "OK";
                    } else {
                        throw new RuntimeException("Transient");
                    }
                }
        );
    }


    private Observable<LocalDate> nextSolarEclipseDate(LocalDate after) {
        return Observable
                .just(
                        LocalDate.of(2016, Month.MARCH, 9),
                        LocalDate.of(2016, Month.SEPTEMBER, 1),
                        LocalDate.of(2017, Month.FEBRUARY, 26),
                        LocalDate.of(2017, Month.AUGUST, 21),
                        LocalDate.of(2018, Month.FEBRUARY, 15),
                        LocalDate.of(2018, Month.JULY, 13),
                        LocalDate.of(2018, Month.AUGUST, 11),
                        LocalDate.of(2019, Month.JANUARY, 6),
                        LocalDate.of(2019, Month.JULY, 2),
                        LocalDate.of(2019, Month.DECEMBER, 26)
                )
                .skipWhile(localDate -> !localDate.isAfter(after))
                .zipWith(
                        Observable.interval(500, 50, TimeUnit.MILLISECONDS),
                        (date, x) -> date
                );
    }

    Observable<Confirmation> confirmation() {
        Observable<Confirmation> delayBeforeCompletion =
                Observable
                        .<Confirmation>empty()
                        .delay(200, TimeUnit.MILLISECONDS);

        return Observable
                .just(new Confirmation())
                .delay(100, TimeUnit.MILLISECONDS)
                .concatWith(delayBeforeCompletion);
    }

}

class Confirmation {

    private long time = System.currentTimeMillis();

    @Override
    public String toString() {
        return "Confirmation{" +
                "time=" + time +
                '}';
    }
}

