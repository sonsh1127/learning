package reactive;

import static io.reactivex.Observable.interval;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.timer;

import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class HelloRxJavaCh3 {

    // rx operators
    @Test
    public void delay() {

        just("X", "Y", "Z")
                .delay(1, TimeUnit.SECONDS)
                .subscribe(t -> {
                    System.out.println(
                            Thread.currentThread().getName() + " : " + System.currentTimeMillis()
                                    + " " + t);
                });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer(1, TimeUnit.SECONDS).
                flatMap(i -> just("X", "Y", "Z"))
                .subscribe(t -> {
                    System.out.println(
                            Thread.currentThread().getName() + " : " + System.currentTimeMillis()
                                    + " " + t);
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        interval(1, TimeUnit.SECONDS).
                flatMap(i -> just("X", "Y", "Z"))
                .subscribe(t -> {
                    System.out.println(
                            Thread.currentThread().getName() + " : " + System.currentTimeMillis()
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
                .subscribe(System.out::println);

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
