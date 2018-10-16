package reactive.ch4;

import io.reactivex.Observable;
import java.time.LocalDateTime;

public class FlightService {

    SmtpResponse sendEmail(Ticket ticket) {
        System.out.println(Thread.currentThread().getName() + " " +ticket + " sent to mail " + LocalDateTime
                .now());
        return new SmtpResponse();
    }

    Observable<SmtpResponse> rxSendEmail(Ticket ticket) {
        return Observable.fromCallable(() -> sendEmail(ticket));
    }

    Flight lookupFlight(String flightNo) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ", " + flightNo);
        return new Flight(flightNo);
    }

    Observable<Flight> rxLookupFlight(String flightNo) {
        return Observable.defer(() -> Observable.just(lookupFlight(flightNo)));
    }

    Passenger findPassenger(long id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ", " + id);
        return new Passenger(id);
    }

    Observable<Passenger> rxFindPassenger(long id) {
        return Observable.defer(() -> Observable.just(findPassenger(id)));
    }

    Ticket bookTicket(Flight flight, Passenger passenger) {
        return new Ticket(flight, passenger);
    }

    Observable<Ticket> rxBookTicket(Flight flight, Passenger passenger) {
        return Observable.defer(() -> Observable.just(bookTicket(flight, passenger)));
    }
}
