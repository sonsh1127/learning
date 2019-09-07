package selenium;

public class SrtTicketingMain {

    public static void main(String[] args) {
        ChromeDriverSrtTicketing ticketing = new ChromeDriverSrtTicketing();
        String id = "";
        String password = "";

        ticketing.login(id, password);
        ticketing.tryReservation("부산", "수서", "20190912","090000");
    }
}
