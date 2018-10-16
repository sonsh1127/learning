package reactive.ch4;

import static reactive.common.LogUtil.log;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.Test;
import reactive.common.LogUtil;
import reactive.common.Pair;

public class HelloRxJavaCh4 {

    PersonDao personDao = new PersonDao();
    FlightService flightService = new FlightService();

    ExecutorService poolA = Executors.newFixedThreadPool(10, threadFactory("Sched-A-%d"));
    Scheduler schedulerA = Schedulers.from(poolA);
    ExecutorService poolB = Executors.newFixedThreadPool(10, threadFactory("Sched-B-%d"));
    Scheduler schedulerB = Schedulers.from(poolB);
    ExecutorService poolC = Executors.newFixedThreadPool(10, threadFactory("Sched-C-%d"));
    Scheduler schedulerC = Schedulers.from(poolC);

    @Test
    public void personDaoToBlocking() {
        personDao.listPeople().toList();
    }

    @Test
    public void laziness() {
        personDao.allPerople(1)
                .take(2)
                .subscribe();
    }

    @Test
    public void allPage() {
        Observable.range(0, Integer.MAX_VALUE)
                .map(i -> personDao.listPeople(i))
                .takeWhile(list -> !list.isEmpty())
                .subscribe();
    }

    @Test
    public void imperativeNonConcurrency() {
        Flight flight = flightService.lookupFlight("LOT 783");
        Passenger passenger = flightService.findPassenger(42L);
        Ticket ticket = flightService.bookTicket(flight, passenger);
        flightService.sendEmail(ticket);
    }

    @Test
    public void rxWithConcurrency() throws InterruptedException {
        Observable<Flight> flight = flightService.rxLookupFlight("Lot 783")
                .subscribeOn(Schedulers.io());
        Observable<Passenger> passenger = flightService.rxFindPassenger(42L)
                .subscribeOn(Schedulers.io());
        Observable<Ticket> ticket = flight.zipWith(passenger, (f, p) -> flightService
                .rxBookTicket(f, p))
                .flatMap(obs -> obs);
        System.out.println(LocalDateTime.now());
        ticket.subscribe(flightService::sendEmail);
        Thread.sleep(3000);
    }

    @Test
    public void sendEmailAndErrorHandling() {
        List<Ticket> tickets = new ArrayList<>();
        ExecutorService pool = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Pair<Ticket, Future<SmtpResponse>>> tasks = tickets.stream().map(ticket -> {
            Future<SmtpResponse> future = pool.submit(() -> flightService.sendEmail(ticket));
            return Pair.of(ticket, future);
        }).collect(Collectors.toList());

        List<Ticket> ticketStream = tasks.stream().flatMap(task -> {
            try {
                task.getRight().get(1, TimeUnit.SECONDS);
                return Stream.empty();
            } catch (Exception e) {
                return Stream.of(task.getLeft());
            }
        }).collect(Collectors.toList());
    }

    @Test
    public void sendEmailAndErrorHandlingRx() {
        List<Ticket> tickets = new ArrayList<>();
        List<Ticket> objects = Observable.fromIterable(tickets)
                .flatMap(ticket ->
                        flightService.rxSendEmail(ticket).
                                flatMap(obs -> Observable.<Ticket>empty())
                                .doOnError(err -> System.out.println(err))
                                .onErrorReturn(err -> ticket)
                ).toList().blockingGet();
    }

    @Test
    public void substituteCallback() {

    }

    @Test
    public void pollingPeriodically() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(this::getData)
                .distinctUntilChanged()
                .subscribe(x -> System.out.println(x));
        sleepForMillis(10000);
    }

    private long getData(long x) {
        return (x % 10 == 0) ? 1L : 2L;
    }

    @Test
    public void pollingDistinctDataOnly() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .flatMapIterable(this::makeRange)
                .distinct()
                .subscribe(x -> System.out.println(x));
        sleepForMillis(10000);
    }

    private List<Long> makeRange(long value) {
        return LongStream.range(value - 2, value + 3)
                .boxed()
                .collect(Collectors.toList());
    }

    @Test
    public void immediateScheduler() {
        Scheduler scheduler = Schedulers.single();
    }

    @Test
    public void workerTest() {
        TrampolineWorker trampolineWorker = new TrampolineWorker();
        trampolineWorker.schedule(
                () -> {
                    log("Outer");
                    trampolineWorker.schedule(
                            () -> {
                                log("Inner");
                            }
                    );
                    log("Outer End");
                }
        );
        trampolineWorker.execute();
    }

    @Test
    public void noScheduler() {
        log("Starting");
        Observable<String> obs = simple();
        log("Created");
        Observable<String> obs2 = obs.map(x -> x).filter(x -> true);
        log("Transformed");
        obs2.subscribe(
                x -> log("Got " + x),
                Throwable::printStackTrace,
                () -> log("Completed")
        );
        log("Exit");
    }

    @Test
    public void scheduler() {
        log("Starting");
        Observable<String> obs = simple();
        log("Created");
        Observable<String> obs2 = obs.map(x -> x).filter(x -> true).subscribeOn(schedulerA)
                .subscribeOn(schedulerB);
        log("Transformed");
        obs2.subscribe(
                x -> log("Got " + x),
                Throwable::printStackTrace,
                () -> log("Completed")
        );
        log("Exit");
        sleepForMillis(2000);
    }

    @Test
    public void schedulerWithSeq() {
        log("Starting");
        Observable<String> obs = simple();
        log("Created");
        obs
                .doOnNext(LogUtil::log)
                .map(x -> x + '1')
                .doOnNext(LogUtil::log)
                .map(x -> x + '2')
                .subscribeOn(schedulerA)
                .doOnNext(LogUtil::log)
                .subscribe(
                        x -> log("Got " + x),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );
        log("Exiting");
        sleepForMillis(2000);
    }

    @Test
    public void concurrentTry() {
        RxGroceries rxGroceries = new RxGroceries();
        Single<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(schedulerA)
                .map(p -> rxGroceries.doPurchase(p, 1))
                .reduce(BigDecimal::add)
                .toSingle();
        totalPrice.subscribe(System.out::println);
        sleepForMillis(10000);

    }

    @Test
    public void concurrentTry2() {
        RxGroceries rxGroceries = new RxGroceries();
        Single<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(schedulerA)
                .flatMap(p -> rxGroceries.purchase(p, 1))
                .reduce(BigDecimal::add)
                .toSingle();
        totalPrice.subscribe(System.out::println);
        sleepForMillis(10000);
    }

    @Test
    public void concurrentSolution() {
        RxGroceries rxGroceries = new RxGroceries();
        Single<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .flatMap(p ->
                        rxGroceries
                                .purchase(p, 1)
                                .subscribeOn(schedulerA)
                )
                .reduce(BigDecimal::add)
                .toSingle();
        totalPrice.subscribe(System.out::println);
        sleepForMillis(10000);
    }

    @Test
    public void groupByBatchProcess() {
        RxGroceries rxGroceries = new RxGroceries();
        Single<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese", "tomato", "egg", "egg")
                .groupBy(prod -> prod)
                .flatMap(grouped -> grouped.count().toObservable()
                        .map(qty -> Pair.of(grouped.getKey(), qty))
                )
                .flatMap(prod -> rxGroceries
                        .purchase(prod.getLeft(), prod.getRight().intValue())
                        .subscribeOn(schedulerA))
                .reduce(BigDecimal::add)
                .toSingle();
        totalPrice.subscribe(System.out::println);
        sleepForMillis(10000);
    }

    @Test
    public void observeOn() {
        log("Staring");
        Observable<String> obs = simple();
        log("Created");
        obs
                .doOnNext(x -> log("Found 1: " + x))
                .observeOn(schedulerA)
                .doOnNext(x -> log("Found 2: " + x))
                .subscribe(
                        x -> log("Got 1 : " + x),
                        Throwable::printStackTrace,
                        () -> log("Complted")
                );
        log("Exiting");
        sleepForMillis(1000);
    }

    @Test
    public void subscribeOnWithObserveOn() {
        log("Staring");
        Observable<String> obs = simple();
        log("Created");

        obs
                .doOnNext(x -> log("Found 1: " + x))
                .observeOn(schedulerB)
                .doOnNext(x -> log("Found 2: " + x))
                .observeOn(schedulerC)
                .doOnNext(x -> log("Found 3: " + x))
                .subscribeOn(schedulerA)
                .subscribe(
                        x -> log("Got 1 : " + x),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );

        log("Exiting");
        sleepForMillis(1000);
    }

    @Test
    public void advancedExample() {
        log("Staring");

        Observable<String> obs = Observable.create( subscriber -> {
            log("subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onNext("C");
            subscriber.onNext("D");
            subscriber.onComplete();
        });

        log("Created");
        obs
                .subscribeOn(schedulerA)
                .flatMap(record -> store(record).subscribeOn(schedulerB))
                .observeOn(schedulerC)
                .subscribe(
                        x -> log("Got 1 : " + x),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );

        log("Exiting ");
        sleepForMillis(5000);
    }

    private Observable<UUID> store(String record) {
        return Observable.create(subscriber -> {
            log("Storing " + record);
            Thread.sleep(500);
            subscriber.onNext(UUID.randomUUID());
            subscriber.onComplete();
        });
    }

    public static void sleepForMillis(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Observable<String> simple() {
        return Observable.create(subscriber -> {
            log("Subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onComplete();
        });
    }

    private ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .build();
    }

}



