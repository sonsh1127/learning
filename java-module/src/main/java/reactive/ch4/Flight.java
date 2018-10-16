package reactive.ch4;

class Flight {

    private String flightNo;

    public Flight(String flightNo) {
        this.flightNo = flightNo;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNo='" + flightNo + '\'' +
                '}';
    }
}
