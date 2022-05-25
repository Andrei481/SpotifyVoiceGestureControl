import org.junit.Before;
import util.Ride;

import javax.print.attribute.standard.Destination;

import static org.junit.jupiter.api.Assertions.*;

class RideTest {
    private Ride ride;

    @Before
    public void validSetUp()
    {
        ride = new Ride(5, "Location A", "Destination B");
    }
    @Before
    public void invalidSetUp()
    {
        try {
            ride = new Ride(-2, "Location A", "Destination B");
            fail("id must be greater than 0");
        }catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String expected = "5 Location A Destination B";
        if(ride != null)
            assertEquals(expected, ride.toString());
    }
}