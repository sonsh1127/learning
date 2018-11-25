package reactive.ch6;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.Test;

public class FlowControl {

    @Test
    public void sampling() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Observable
                .interval(7, MILLISECONDS)
                .timestamp()
                .sample(1, SECONDS)
                .map(ts -> ts.time() - startTime + "ms: " + ts.value())
                .subscribe(System.out::println);

        Thread.sleep(6000);
    }

    @Test
    public void complicatedSampling() throws InterruptedException {
        Observable<String> delayedNames = delayedNames();
        delayedNames.sample(1, SECONDS)
                .subscribe(System.out::println);
        Thread.sleep(6000);
    }

    private Observable<String> delayedNames() {
        Observable<String> names = Observable
                .just("Mary", "Patricia", "Linda",
                        "Barbara",
                        "Elizabeth", "Jennifer", "Maria", "Susan",
                        "Margaret", "Dorothy");

        Observable<Long> absoluteDelayMills = Observable
                .just(100L, 600L, 900L,
                        1200L,
                        3300L, 3400L, 3500L, 3600L,
                        4400L, 4800L);
        return names
                .zipWith(absoluteDelayMills, (n, d) -> Observable.just(n)
                        .delay(d, MILLISECONDS))
                .flatMap(o -> o);
    }

    @Test
    public void throttleLast() {
        Observable<String> observable = delayedNames()
                .throttleLast(1, SECONDS);
        waitAndPrint(observable, 6000);
    }

    @Test
    public void throttleFirst() {
        Observable<String> observable = delayedNames()
                .throttleFirst(1, SECONDS);
        waitAndPrint(observable, 6000);
    }

    @Test
    public void buffer() {
        Observable
                .range(1, 7)
                .buffer(3)
                .subscribe(System.out::println);
    }

    @Test
    public void bufferWithSkip() {
        Observable
                .range(1, 7)
                .buffer(3, 1)
                .subscribe(System.out::println);
    }

    @Test
    public void bufferWithSkipExam() {

        Random random = new Random();
        Observable
                .defer(() -> Observable.just(random.nextGaussian()))
                .repeat(1000)
                .buffer(100, 1)
                .map(this::averageOfList)
                .subscribe(System.out::println);

    }

    private double averageOfList(List<Double> list) {
        return list.stream().collect(Collectors.averagingDouble(x -> x));
    }

    @Test
    public void bufferWithSkipData() {
        Observable<List<Integer>> odd = Observable
                .range(1, 7)
                .buffer(1, 2);

        odd.subscribe(System.out::println);
    }

    @Test
    public void bufferPeriodically() {
        // it just buffer data periodically
        Observable<List<String>> observable = delayedNames()
                .buffer(1, SECONDS);
        waitAndPrint(observable, 6000);
    }

    @Test
    public void bufferWithBoundarySuppliers() {
        Observable<TeleData> upstream = create();
        Observable<List<TeleData>> samples = upstream.buffer(opening());
        waitAndPrint(samples, 60 * 1000);
    }

    @Test
    public void bufferWithOpeningClosingSuppliers() {
        Observable<TeleData> upstream = create();
        Observable<List<TeleData>> samples = upstream.buffer(
                opening(),
                duration -> Observable.empty().delay(duration.toMillis(), MILLISECONDS)
        );
        waitAndPrint(samples, 60 * 1000);
    }

    private Observable<TeleData> create() {
        return Observable.interval(100, MILLISECONDS)
                .map(x -> new TeleData(x));
    }

    private Observable<Duration> opening() {
        Observable<Duration> insideBusinessHours =
                Observable.interval(1, SECONDS)
                        .filter(x -> isBusinessHour())
                        .map(x -> Duration.ofMillis(100));

        Observable<Duration> outsideBusinessHours =
                Observable.interval(5, SECONDS)
                        .filter(x -> isBusinessHour())
                        .map(x -> Duration.ofMillis(200));
        return Observable.merge(insideBusinessHours, outsideBusinessHours);
    }

    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);

    private boolean isBusinessHour() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        LocalTime localTime = zdt.toLocalTime();
        return !localTime.isBefore(BUSINESS_START) && !localTime.isAfter(BUSINESS_END);
    }

    @Test
    public void bufferWithCount() {
        delayedNames().buffer(1, SECONDS)
                .map(List::size)
                .subscribe(System.out::println);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * window operator is more efficient than buffer when counting items on memory consumption.
     */
    @Test
    public void window() {
        Observable<Long> observable = delayedNames()
                .window(1, SECONDS)
                .flatMap(obs -> obs.count().toObservable());

        waitAndPrint(observable, 6000);
    }

    @Test
    public void debounce() {
        Observable<String> debounce = delayedNames()
                .debounce(1, SECONDS);
        waitAndPrint(debounce, 6000);
    }

    private <T> void waitAndPrint(Observable<T> obs, long waitTimeMills) {
        obs
                .subscribe(x -> System.out
                        .println(new Timestamp(System.currentTimeMillis()) + ", " + x));
        try {
            Thread.sleep(waitTimeMills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void debounce2() {
        pricesOf("SDS")
                .debounce(100, MILLISECONDS)
                .subscribe(System.out::println);
    }

    @Test
    public void debounceSelector() {
        Observable<BigDecimal> observable = pricesOf("Kakao")
                .debounce(x -> {
                    boolean goodPrice = x.compareTo(BigDecimal.valueOf(150)) > 0;
                    return Observable.empty().delay(goodPrice ? 10 : 100, MILLISECONDS);
                });
        waitAndPrint(observable, 60 * 1000);
    }

    @Test
    public void debounceWithResourceDepletion() {
        Observable<Long> observable = Observable
                .interval(90, MILLISECONDS)
                .debounce(100, MILLISECONDS);

        waitAndPrint(observable, 10 * 1000);
    }

    @Test
    public void debounceWithResourceDepletionTimeout() {
        Observable<Long> observable = Observable
                .interval(90, MILLISECONDS)
                .debounce(100, MILLISECONDS)
                .timeout(1, SECONDS);

        waitAndPrint(observable, 10 * 1000);
    }

    @Test
    public void debounceWithFirstData() {
        ConnectableObservable<Long> upstream = intervalObservable();

        Observable<Long> obs = upstream
                .debounce(100, MILLISECONDS)
                .timeout(1, SECONDS, upstream.take(1));

        upstream.connect();
        waitAndPrint(obs, 10 * 1000);
    }

    private ConnectableObservable<Long> intervalObservable() {
        return Observable
                .interval(90, MILLISECONDS)
                .publish();
    }

    @Test
    public void debounceWithEnhance() {
        ConnectableObservable<Long> upstream = intervalObservable();

        Observable<Long> observable = upstream
                .debounce(100, MILLISECONDS)
                .timeout(1, SECONDS, upstream
                        .take(1)
                        .concatWith(upstream.debounce(100, MILLISECONDS))
                        .timeout(1, SECONDS, upstream));

        upstream.connect();
        waitAndPrint(observable, 10 * 1000);
    }

    @Test
    public void debounceFinal() {
        ConnectableObservable<Long> upstream = intervalObservable();

        Observable<Long> debounce = debounce(upstream);

        upstream.connect();

        waitAndPrint(debounce, 10 * 1000);
    }

    Observable<Long> debounce(Observable<Long> upstream) {
        Observable<Long> onTimeout = upstream
                .take(1)
                .concatWith(Observable.defer(() -> debounce(upstream)));
        return upstream
                .debounce(100, MILLISECONDS)
                .timeout(1, SECONDS, onTimeout);
    }

    Observable<BigDecimal> pricesOf(String ticker) {
        return Observable
                .interval(50, MILLISECONDS)
                .flatMap(this::randomDelay)
                .map(this::randomStockPrice)
                .map(BigDecimal::valueOf);
    }

    double randomStockPrice(long x) {
        return 100 + Math.random() * 10 + (Math.sin(x / 100)) * 60.0;
    }

    Observable<Long> randomDelay(long x) {
        return Observable
                .just(x)
                .delay((long) (Math.random() * 100), MILLISECONDS);
    }

}
