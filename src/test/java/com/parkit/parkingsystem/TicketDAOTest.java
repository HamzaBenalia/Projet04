package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {

    private static final String TEST_VEHICLE_REG_NUMBER = "ABCDEF";
    private static final double TEST_TICKET_PRICE = 1.125;

    private TicketDAO ticketDAO;
    private Connection connection;

    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException {
        ticketDAO = new TicketDAO();
        connection = ticketDAO.dataBaseConfig.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    public void saveTicket() throws SQLException {
        // Arrange
        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(TEST_VEHICLE_REG_NUMBER);
        ticket.setPrice(TEST_TICKET_PRICE);
        ticket.setInTime(new Timestamp(System.currentTimeMillis()));

        // Act
        boolean result = ticketDAO.saveTicket(ticket);

        // Assert
        assertFalse(result, "Expected saveTicket to return true");

    }

    @Test
    public void getTicket(){

        String vehicleRegNumber = "ABCDEF";

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);

        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setPrice(1.125);

        ticket.setInTime(new Timestamp(System.currentTimeMillis()));

        boolean saveTicket = ticketDAO.saveTicket(ticket);

        Assertions.assertFalse(saveTicket);

        Ticket retrieveTicket = ticketDAO.getTicket(vehicleRegNumber);

        Assertions.assertNotNull(retrieveTicket);
        Assertions.assertEquals(vehicleRegNumber, retrieveTicket.getVehicleRegNumber());
        Assertions.assertEquals(ticket.getPrice(), retrieveTicket.getPrice());



    }



@Test
void testUpdateTicket() {
    Ticket ticket = new Ticket();
    ticket.setPrice(1);
    ticket.setInTime(new Timestamp(new Date().getTime()));
    ticket.setVehicleRegNumber("ABCDEF");
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setParkingSpot(parkingSpot);
    assertFalse(ticketDAO.saveTicket(ticket));

    ticket.setOutTime(new Timestamp(new Date().getTime()));
    ticket.setPrice(1.125);
    assertTrue(ticketDAO.updateTicket(ticket));

    Ticket updatedTicket = ticketDAO.getTicket("ABCDEF");
    assertEquals(1.125, updatedTicket.getPrice());
}



}