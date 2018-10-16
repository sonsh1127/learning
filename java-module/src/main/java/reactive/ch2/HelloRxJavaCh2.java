package reactive.ch2;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class HelloRxJavaCh2 {

    private Observable<Integer> intObservable = Observable.create(sub -> {
        System.out.println("Connected");
        AtomicInteger integer = new AtomicInteger(0);
        Runnable r = () -> {
            while (!sub.isDisposed()) {
                sub.onNext(integer.incrementAndGet());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        sub.setCancellable(() -> System.out.println("Canceled"));
        new Thread(r).start();
    });

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
    public void refCount() {
        Observable<Integer> lazy = intObservable.publish().refCount();
        System.out.println(lazy.getClass().getName());
        intObservable.doOnNext((v) -> System.out.println("do On Next")).subscribe();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    @Test
    public void tweeterObserver() {
        Observable<Status> tweeterObserver = Observable.create(subscriber -> {
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(
                    new StatusListener() {
                        @Override
                        public void onStatus(Status status) {
                            if (subscriber.isDisposed()) {
                                twitterStream.shutdown();
                            } else {
                                subscriber.onNext(status);
                            }
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
                            if (subscriber.isDisposed()) {
                                twitterStream.shutdown();
                            } else {
                                subscriber.onError(ex);
                            }
                        }
                    }
            );

            subscriber.setCancellable(() -> {
                twitterStream.shutdown();
            });
        });

        Disposable sub1 = tweeterObserver.subscribe();
        System.out.println("subscribed 1");
        Disposable sub2 = tweeterObserver.subscribe();
        System.out.println("subscribed 2");
        sub1.dispose();
        System.out.println("Unsubscribed 1");
        sub2.dispose();
        System.out.println("Unsubscribed 2");

        tweeterObserver.publish().refCount();
    }


    @Test
    public void connectableObservable() {
        ConnectableObservable<Integer> publish = intObservable.publish();
        publish.subscribe(v -> log(v));
        publish.connect();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

