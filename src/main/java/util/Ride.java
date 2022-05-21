package util;

public class Ride {
    private int id;
    private String location, destination, driverName, clientName;

    public Ride(int id, String location, String destination) {
        this.id = id;
        this.location = location;
        this.destination = destination;
    }

    public Ride(int id, String location, String destination, String driverName, String clientName) {
        this.id = id;
        this.location = location;
        this.destination = destination;
        this.driverName = driverName;
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return this.id +" "+ this.location + " " + this.destination;
    }

    public String toString(String role) {
        if (role.equals("Driver"))
            return this.location + " ⟶ " + this.destination + ", " + this.driverName;
        else
            return this.location + " ⟶ " + this.destination + ", " + this.clientName;
    }
}