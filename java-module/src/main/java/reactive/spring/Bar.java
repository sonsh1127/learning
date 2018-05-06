package reactive.spring;

import io.reactivex.Observable;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class Bar {

    public Bar(Observable<Status> statusObservable){

    }
}
