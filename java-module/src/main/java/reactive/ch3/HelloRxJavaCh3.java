package reactive.ch3;

import static io.reactivex.Observable.empty;
import static io.reactivex.Observable.interval;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.range;
import static io.reactivex.Observable.timer;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import reactive.common.Pair;

public class HelloRxJavaCh3 {

    // rx operators
    @Test
    public void delay() {
        System.out.println("Start Test : " + LocalDateTime.now());
        just("X", "Y", "Z")
                .delay(1, TimeUnit.SECONDS)
                .subscribe(t -> {
                    System.out.println(
                            Thread.currentThread().getName() + " : " + LocalDateTime.now()
                                    + " " + t);
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Start Second Test : " + LocalDateTime.now());
        timer(1, TimeUnit.SECONDS).
                flatMap(i -> just("X", "Y", "Z"))
                .subscribe(t -> {
                    System.out.println(
                            Thread.currentThread().getName() + " : " + LocalDateTime.now()
                                    + " " + t);
                });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Start Third Test : " + LocalDateTime.now());
        interval(1, TimeUnit.SECONDS).
                flatMap(i -> just("X", "Y", "Z"))
                .subscribe(t -> {
                    System.out.println(
                            Thread.currentThread().getName() + " : " + LocalDateTime.now()
                                    + " " + t);
                });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void delayAndFlatMap() {
        just("Loream", "insum", "dolor", "sit", "amet", "consectetur", "adipsiscing",
                "elit")
                .delay(word -> timer(word.length(), TimeUnit.SECONDS))
                .subscribe(x -> System.out.println(x + " " + Thread.currentThread().getName() + " " + LocalDateTime.now()));

        sleepForSeconds(15);

        System.out.println("Second");

        just("Loream", "insum", "dolor", "sit", "amet", "consectetur", "adipsiscing",
                "elit")
                .flatMap(word -> timer(word.length(), TimeUnit.SECONDS).map(x -> word))
                .subscribe(System.out::println);

        sleepForSeconds(15);
    }

    @Test
    public void flatMapAndSeq() {
        just(7, 2).map(i -> {
            Thread.sleep(i * 100);
            return i;
        })
                .subscribe(System.out::println);

        sleepForSeconds(3L);

        // 1. change sequnce
        just(10L, 1L).flatMap(
                i -> just(i).delay(i, TimeUnit.SECONDS)
        ).subscribe(System.out::println);

        sleepForSeconds(15L);

        // 2. infinite sequence
        just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                .flatMap(this::loadRecordsFor)
                .subscribe(System.out::println);

        sleepForSeconds(10);

        System.out.println("concatMap preserve the sequence of upstream event");
        just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                .concatMap(this::loadRecordsFor)
                .subscribe(System.out::println);

        sleepForSeconds(10);

    }

    @Test
    public void flatMapWithMaxConcurrent() {
        // flatMap with maxConcurrent
        List<User> manyUsers = new ArrayList<>();
        Observable<Profile> profiles = Observable.fromIterable(manyUsers).flatMap(User::loadProfile,
                10);

    }

    @Test
    public void handleManyObserversWithMerge() {

        CarPhoto photo = new CarPhoto();

        // Observable.merge
        Observable<LicensePlate> all = Observable.merge(
                fastAlgo(photo),
                preciseAlgo(photo),
                experimentalAlgo(photo)
        );

        // obs.mergeWith
        Observable<LicensePlate> obs = fastAlgo(photo);
        obs.mergeWith(preciseAlgo(photo));

        // if you want to delay exceptions you can use mergeDelayError method.
    }

    @Test
    public void handleManyObserversWithZip() {

        WeatherStation station = null;
        Observable<Temperature> temperature = station.temperature();
        Observable<Wind> wind = station.wind();

        Observable<Weather> weatherObservable = temperature
                .zipWith(wind, (t, w) -> new Weather(t, w));

        temperature.zipWith(wind, Weather::new);

        //
        Observable<Integer> oneToEight = range(1, 8);
        Observable<String> ranks = oneToEight.map(Object::toString);
        Observable<String> files = oneToEight
                .map(x -> 'a' + x - 1)
                .map(ch -> Character.toString((char) ch.intValue()));

        files.flatMap(file -> ranks.map(r -> file + r))
                .subscribe(
                        System.out::println
                );
    }

    @Test
    public void combineLatest() {
        /*Observable<Long> red = Observable.interval(10, TimeUnit.MILLISECONDS);
        Observable<Long> green = Observable.interval(10, TimeUnit.MILLISECONDS);
        Observable.zip(
                red.timestamp(),
                green.timestamp(),
                (r, g) -> r.time() - g.time()
        ).forEach(System.out::println);

        sleepForSeconds(10);*/

        Observable<String> slow = interval(17, TimeUnit.MILLISECONDS).map(x -> "S" + x);
        Observable<String> fast = interval(10, TimeUnit.MILLISECONDS).map(x -> "F" + x);

        Disposable disposable = Observable.combineLatest(
                slow,
                fast,
                (s, f) -> f + ":" + s
        ).forEach(System.out::println);
        sleepForSeconds(1);

        disposable.dispose();
        System.out.println("=========================================");
        slow.withLatestFrom(fast, (s, f) -> s + ":" + f)
                .forEach(System.out::println);
        sleepForSeconds(3);

        withLatestFrom();
    }

    @Test
    public void withLatestFrom() {
        System.out.println("*****************************************");

        Observable<String> fastButLateStart = interval(10, TimeUnit.MILLISECONDS).map(x -> "F" + x)
                .delay(100, TimeUnit.MILLISECONDS)
                .startWith("FX");
        Observable<String> slowButFastStart = interval(17, TimeUnit.MILLISECONDS)
                .map(x -> "S" + x);

        slowButFastStart.withLatestFrom(fastButLateStart, (s, f) -> s + ":" + f)
                .forEach(System.out::println);
        sleepForSeconds(3);
    }

    @Test
    public void amb() {

        Observable.amb(
                Arrays.asList(stream(100, 17, "S"),
                        stream(200, 10, "F"))
        )
                .subscribe(System.out::println);
        sleepForSeconds(2);
    }


    @Test
    public void scan() {

        Observable<Long> progress = Observable.just(10L, 14L, 12L, 13L, 14L, 16L);
        progress.scan((total, chunk) -> total + chunk)
                .forEach(System.out::println);

        Single<Long> res = progress.reduce(0L, (total, chunk) -> total + chunk);

        // explict initial Value
        range(2, 100)
                .scan(BigInteger.ONE, (big, cur) -> big.multiply(BigInteger.valueOf(cur)))
                .forEach(System.out::println);
    }

    @Test
    public void collect() {
        Single<List<Integer>> all = range(10, 20)
                .reduce(new ArrayList<>(), (list, item) -> {
                    list.add(item);
                    return list;
                });

        Single<List<Object>> all2 = range(10, 20)
                .collect(ArrayList::new, List::add);

        Single<String> string = range(1, 10)
                .collect(StringBuilder::new, (sb, x) -> sb.append(x).append(", "))
                .map(StringBuilder::toString);
    }

    @Test
    public void distinct() {
        just(2, 1, 1, 1, 2, 3).distinct().forEach(System.out::println);
        System.out.println("Nills #############");
        just(2, 1, 1, 1, 2, 3).distinctUntilChanged().forEach(System.out::println);
    }

    @Test
    public void skipAndTake() {
        range(1, 5).take(3); // 1,2,3
        range(1, 5).skip(3); // 4,5
        range(1, 5).skip(5); // empty
        range(1, 5).takeLast(2); //4,5
        range(1, 5).skipLast(2); // 1,2,3

    }

    @Test
    public void concat() {
        long id = 1L;
        Single<String> observable = Observable.concat(
                loadFromCache(id),
                loadFromDb(id)
        ).first("");

        observable.subscribe(System.out::println);
        id = 2L;
        observable.subscribe(System.out::println);
    }


    @Test
    public void mergeSpeak() {

        Observable<String> alice = speak("To be, not to be: that is the question", 110);
        Observable<String> bob = speak("Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak("There are more things in Heaven and Earth, "
                        + "Horatio, than are dreamt of in your philosophy"
                , 100);

        Observable.merge(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        ).subscribe(System.out::println);

        sleepForSeconds(10);

    }

    @Test
    public void concatSpeak() {

        Observable<String> alice = speak("To be, not to be: that is the question", 110);
        Observable<String> bob = speak("Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak("There are more things in Heaven and Earth, "
                        + "Horatio, than are dreamt of in your philosophy"
                , 100);

        Observable.concat(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        ).subscribe(System.out::println);

        sleepForSeconds(10);
    }

    @Test
    public void switchOnNextSpeak() {
        Observable<String> alice = speak("To be, not to be: that is the question", 110);
        Observable<String> bob = speak("Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak("There are more things in Heaven and Earth, "
                        + "Horatio, than are dreamt of in your philosophy"
                , 100);

        Random random = new Random();
        Observable<Observable<String>> quotes = just(alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        )
        //.flatMap(innerObs -> just(innerObs).delay(random.nextInt(5), TimeUnit.SECONDS));
        .map(innerObs -> innerObs.delay(random.nextInt(5), TimeUnit.SECONDS));
        Observable.switchOnNext(quotes).subscribe(System.out::println);
        sleepForSeconds(10);
    }

    Observable<String> speak(String quote, long millisPerChar) {
        String[] tokens = quote.replaceAll("[:,]", "").split(" ");

        Observable<String> words = Observable.fromArray(tokens);
        Observable<Long> absoluteDelay = words.map(String::length)
                .map(len -> len * millisPerChar)
                .scan((total, current) -> total + current);

        return words.zipWith(absoluteDelay.startWith(0L), Pair::of)
                .flatMap(p -> just(p.getLeft()).delay(p.getRight().longValue(), TimeUnit.MILLISECONDS));

    }

    private Observable<String> loadFromCache(long id) {
        System.out.println("load from cache");
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id == 1L ? just("1") : empty();
    }

    private Observable<String> loadFromDb(long id) {
        System.out.println("load from db");
        sleepForSeconds(1);
        return Observable.just(Long.toString(id));
    }

    private Observable<String> stream(int initialDay, int interval, String name) {
        return Observable.interval(initialDay, interval, TimeUnit.MILLISECONDS).map(x -> name)
                .doOnSubscribe((x) -> System.out.println("Subscribied to " + name))
                .doOnDispose(() -> System.out.println("Unsubscribed to " + name));
    }

    interface WeatherStation {

        Observable<Temperature> temperature();

        Observable<Wind> wind();
    }

    class Weather {

        Weather(Temperature temperature, Wind wind) {
        }
    }

    class Temperature {

    }

    class Wind {

    }

    private Observable<LicensePlate> fastAlgo(CarPhoto carPhoto) {
        return just(new LicensePlate());
    }

    private Observable<LicensePlate> preciseAlgo(CarPhoto carPhoto) {
        return just(new LicensePlate());
    }

    private Observable<LicensePlate> experimentalAlgo(CarPhoto carPhoto) {
        return just(new LicensePlate());
    }

    class CarPhoto {

    }

    class LicensePlate {

    }

    class User {

        public Observable<Profile> loadProfile() {
            // http connect ....
            return just(new Profile());
        }
    }

    class Profile {

    }

    private Observable<String> loadRecordsFor(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case SUNDAY:
                return Observable
                        .interval(90, TimeUnit.MILLISECONDS)
                        .take(5)
                        .map(i -> "Sun-" + i);
            case MONDAY:
                return Observable
                        .interval(65, TimeUnit.MILLISECONDS)
                        .take(5)
                        .map(i -> "Mon-" + i);
            default:
                return Observable.empty();
        }
    }

    private void sleepForSeconds(long timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    enum DayOfWeek {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY
    }

}

