package reactive;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class HelloRxJavaCh2 {

    @Test
    public void wrongInfiniteStream() {
        Observable<Integer> randomNumbers = Observable.create(sub -> {
            Random random = new Random();
            while (true) {
                sub.onNext(random.nextInt());
            }
        });

        // client thread will be blocked due to infinite loop
        randomNumbers.subscribe(System.out::println);
    }


    @Test
    public void betterInfiniteStream() {
        Observable<Object> objectObservable = Observable.create(sub -> {
            Random random = new Random();
            Runnable r = () -> {
                while (!sub.isDisposed()) {
                    sub.onNext(random.nextInt());
                }
            };
            new Thread(r).start();
        });
        objectObservable.subscribe(v -> log(v));
        objectObservable.subscribe(v -> log(v));

        StudentRepository repository = new MemoryStudentRepository();
        Long id = 1L;
        Observable.fromCallable(() -> repository.findById(id));
    }

    @Test
    public void timerAndInterval() {
        Observable.timer(1, TimeUnit.SECONDS).subscribe(zero -> log(zero));
        Observable.interval(1, TimeUnit.SECONDS).subscribe(zero -> log(zero));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void log(Object value) {
        System.out.println(Thread.currentThread().getName() + " " + value);
    }

    @Test
    public void createFactoryMethod() {
        Observable<Object> just = Observable.just("nins");
        Single<String> single = Single.just("Nills");
    }

    @Test
    public void subject() {
        class TwitterSubject {

            private final PublishSubject<Status> subject = PublishSubject.create();

            public TwitterSubject() {
                TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
                twitterStream.addListener(new StatusListener() {
                    @Override
                    public void onStatus(Status status) {
                        subject.onNext(status);
                    }

                    @Override
                    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                    }

                    @Override
                    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                    }

                    @Override
                    public void onScrubGeo(long userId, long upToStatusId) {

                    }

                    @Override
                    public void onStallWarning(StallWarning warning) {
                    }

                    @Override
                    public void onException(Exception ex) {
                        subject.onError(ex);
                    }
                });
                twitterStream.sample();
            }

            public Observable<Status> observe() {
                return subject;
            }
        }

    }

    public static void main(String[] args) {
        Observable<String> delayed = delayed("name");
        Disposable disposable = delayed.subscribe(System.out::println);
        disposable.dispose();
    }

    static <T> Observable delayed(T x) {
        return Observable.create(sub -> {
            Runnable r = () -> {
                sleep(10, SECONDS);
                if (!sub.isDisposed()) {
                    sub.onNext(x);
                    sub.onComplete();
                }
            };

            Thread thread = new Thread(r);
            thread.start();
            sub.setCancellable(() -> {
                System.out.println("canceled!!");
                thread.interrupt();
            });
        });
    }

    static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            // do nothing
        }
    }


}

interface StudentRepository {

    Student findById(Long id);
}

class MemoryStudentRepository implements StudentRepository {

    private ConcurrentMap<Long, Student> students = new ConcurrentHashMap<>();

    @Override
    public Student findById(Long id) {
        return students.get(id);
    }
}

class Student {

    private long id;
    private String name;
    private int score;

    public Student(long id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }
}
