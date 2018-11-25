package reactive.ch6;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

public class BackPressure {

    @Test
    public void rendezvous() {
        Observable
                .range(1, 1_000_0000)
                .map(Dish::new)
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + " Washing: " + x);
                    sleepMills(50);
                });
    }

    @Test
    public void produceConsumeWithFlowable() {
        Flowable<Dish> dishes = Flowable.range(1, 1_000_000)
                .map(Dish::new);
        dishes
                .observeOn(Schedulers.io())
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + " Washing: " + x);
                    sleepMills(50);
                });

       sleepMills(1_000_00);
    }

    @Test
    public void produceConsumeWithMyRange() {
        Observable
                .range(1, 1_000_0000)
                .map(Dish::new)
                .observeOn(Schedulers.io())
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + " Washing: " + x);
                    sleepMills(50);
                });
    }

    @Test
    public void create() {

    }

    private void sleepMills(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Dish {

    private final byte[] oneKb = new byte[1024];
    private final int id;

    Dish(int id) {
        this.id = id;
        System.out.println(Thread.currentThread().getName() + " Created: " + id);
    }

    @Override
    public String toString() {
        return "Dish{" +
                " id=" + id +
                '}';
    }
}
