package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest()
    {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar() throws Exception {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike() throws Exception {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnknownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }


    @Test
    @DisplayName("Calculate fare for unknown parking type should throw an exception")
    public void testCalculateFareForUnknownParkingTypeShouldThrowException() {
        // GIVEN
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKNOWN, true);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000))); // 2 hours ago
        ticket.setOutTime(new Date(System.currentTimeMillis()));

        // WHEN
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fareCalculatorService.calculateFare(ticket);
        });

        // THEN
        String expectedMessage = "Unkown Parking Type";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() throws Exception {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(( 0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() throws Exception {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() throws Exception {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }



        @Test
        public void testCalculateFare_recurrentUser() throws Exception {
            // Arrange
            FareCalculatorService fareCalculatorService = new FareCalculatorService();
            Ticket ticket = new Ticket();
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
            ticket.setInTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000))); // 2 hours ago
            ticket.setOutTime(new Date());
            TicketDAO ticketDAO = mock(TicketDAO.class); // Create a mock instance of TicketDAO
            when(ticketDAO.checkIfRegVehicleNumberAlreadyExist(ticket.getVehicleRegNumber())).thenReturn(true); // Set the mock to return true for recurrent user
            fareCalculatorService.ticketDAO = ticketDAO;
            double farePerHour = Fare.CAR_RATE_PER_HOUR;// Set the hourly rate to 1.125

            // Act
            fareCalculatorService.calculateFare(ticket);

            // Assert
            assertEquals(2 * farePerHour, ticket.getPrice(), 0.15); // 2 hours * 1.125 * 0.95 = 2.14
        }

    @Test
    public void testCalculateFareWithLessThan30MinParkingTime() throws Exception {
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABC123");
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false ));
        ticket.setInTime(new Date(System.currentTimeMillis() - (20 * 60 * 1000))); // 20 minutes ago
        ticket.setOutTime(new Date());
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice(), 0.01);
    }

    @Test
    public void testCalculateFareWith30MinParkingTime() throws Exception {
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABC123");
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false ));
        ticket.setInTime(new Date(System.currentTimeMillis() - (30 * 60 * 1000))); // 20 minutes ago
        ticket.setOutTime(new Date());
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice(), 0.01);
    }



    }




