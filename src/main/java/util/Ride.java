package util;

public class Ride {
    private int id;
    private String location, destination, clientName, driverName;

    public Ride(int id, String location, String destination) {
        this.id = id;
        this.location = location;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return this.id +" "+ this.location + " " + this.destination;
    }
}
