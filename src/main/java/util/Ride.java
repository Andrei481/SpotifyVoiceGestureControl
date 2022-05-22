package util;

public class Ride {
    private int id;
    private String location, destination, name;

    public Ride(int id, String location, String destination) {
        this.id = id;
        this.location = location;
        this.destination = destination;
    }

    public Ride(int id, String name, String location, String destination) {
        this.id = id;
        this.location = location;
        this.destination = destination;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id +" "+ this.location + " " + this.destination;
    }

    public String toString(boolean printName) {
        return this.location + " ⟶ " + this.destination + ", " + this.name;
    }
}