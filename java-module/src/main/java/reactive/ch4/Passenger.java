package reactive.ch4;

class Passenger {

    private Long id;

    public Passenger(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                '}';
    }
}
