package reactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 *
 */
public class HelloReactor {

    @Test
    public void simpleCreate() {
        Mono<String> mono = Mono.just("Hello world");
        mono.subscribe(System.out::println);

        Flux<String> flux = Flux.just("Apple", "Banana", "Carrot");
        flux.subscribe(System.out::println, throwable -> {
        }, () -> System.out.println("Done"));

        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        Mono<String> noData = Mono.empty();
        Mono<String> data = Mono.just("foo");
        Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3);

        numbersFromFiveToSeven.subscribe(
                d -> System.out.println(d)
        );
    }

    @Test
    public void errorHandling() throws InterruptedException {
        Flux.just("foo")
                .map(s -> {
                    throw new IllegalArgumentException(s);
                })
                .subscribe(v -> System.out.println("GOT VALUE"),
                        e -> System.out.println("ERROR: " + e));
    }

    private void retryWhen() {
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(System.out::println)
                .retryWhen(companion -> companion.take(4));

        flux.subscribe(System.out::println);
    }

    private void retry() throws InterruptedException {
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) {
                        return "tick " + input;
                    }
                    throw new RuntimeException("boom");
                })
                .elapsed()
                .retry(2)
                .subscribe(System.out::println, (t) -> System.err.println(t + "NIlls"));
        Thread.sleep(5100);
    }

    private void errorLog() {
        LongAdder failureStat = new LongAdder();
        Flux<String> flux =
                Flux.just("unknown")
                        .flatMap(k -> makeFlux(k))
                        .doOnError(e -> {
                            failureStat.increment();
                        })
                        .onErrorResume(e -> Flux.just("aaaaa"));
    }

    public static Flux<String> makeFlux(String value) {
        return Flux.just(value);
    }

    @Test
    public void scheduler() {
        Flux.range(1, 10000)
                .publishOn(Schedulers.parallel())
                .subscribe((t) -> {
                    System.out.println(Thread.currentThread() + ", " + t);
                });
        try {
            Thread.sleep(55000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
