package reactive.ch4;

public class Ticket {

    private Flight flight;
    private Passenger passenger;

    public Ticket(Flight flight, Passenger passenger) {
        this.passenger = passenger;
        this.flight = flight;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "passenger=" + passenger +
                ", flight=" + flight +
                '}';
    }
}
