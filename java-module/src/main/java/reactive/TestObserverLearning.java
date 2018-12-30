package reactive;

import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;
import org.junit.Test;


/**
 * in rxjava2 testObserver testSubsr
 */
public class TestObserverLearning {

    @Test
    public void assertWith() {
         Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertResult(0L,1L,2L);
    }

}
