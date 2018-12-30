package reactive.ch7;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;

public class UnitTestSupport {

    @Test
    public void testScheduler() throws InterruptedException {

        TestScheduler testScheduler = new TestScheduler();
        Observable<String> fast = Observable
                .interval(10, MILLISECONDS, testScheduler)
                .map(x -> "F" + x)
                .take(3);
        Observable<String> slow = Observable
                .interval(50, MILLISECONDS, testScheduler)
                .map(x -> "S" + x);

        Observable<String> stream = Observable.concat(fast, slow);
        stream.subscribe(x -> System.out.println(Thread.currentThread().getName() + " " + x));
        System.out.println("Subscribed");

        SECONDS.sleep(1);
        System.out.println("After One Second");
        testScheduler.advanceTimeBy(25, MILLISECONDS);

        SECONDS.sleep(1);
        System.out.println("After One more Second");
        // advance relative time
        testScheduler.advanceTimeBy(75, MILLISECONDS);

        SECONDS.sleep(1);
        System.out.println("... and one more");

        // move to absolute time
        testScheduler.advanceTimeTo(200, MILLISECONDS);
    }

    @Test
    public void shouldApplyConcatMapInOrder() {
        List<String> list = Observable.range(1, 3)
                .concatMap(x -> Observable.just(x, -x))
                .map(Objects::toString)
                // to verify result we make result blocking observable
                .toList()
                .blockingGet();

        List<String> expected = Arrays.asList("1", "-1", "2", "-2", "3", "-3");
        Assert.assertEquals(expected, list);
    }

    @Test
    public void exception() {
        Path path = Paths.get("404.txt");
        try {
            Observable
                    .fromCallable(() -> Files.readAllLines(path))
                    .blockingFirst();
            fail("");
        } catch (Exception e) {
        }
    }

    @Test
    public void concatMapDelayErrorLearning() {

    }
}