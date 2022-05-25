package util;

public class Ride {
    private int id;
    private String location, destination, name;

    public Ride(int id, String location, String destination) {
        if(id > 0 && location != null && destination != null) {
            this.id = id;
            this.location = location;
            this.destination = destination;
        }
        else
            throw new IllegalArgumentException("illegal argument in Ride constructor");
    }

    public Ride(int id, String name, String location, String destination) {
        if(id > 0 && name != null && location != null && destination != null) {
            this.id = id;
            this.location = location;
            this.destination = destination;
            this.name = name;
        }
        else
            throw new IllegalArgumentException("illegal argument in Ride constructor");
    }

    @Override
    public String toString() {
        return this.id +" "+ this.location + " " + this.destination;
    }

    public String toString(boolean printName) {
        return this.location + " ⟶ " + this.destination + ", " + this.name;
    }
}