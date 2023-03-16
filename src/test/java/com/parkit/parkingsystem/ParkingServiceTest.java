package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Logger;

import static com.mysql.cj.conf.PropertyKey.logger;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    static TicketDAO ticketDAO;

    @Mock
    private static ParkingSpot parkingSpot;

    @Mock
    static Ticket ticket;

    @Mock
    private static FareCalculatorService fareCalculatorService;


    @BeforeEach
    private void setUpPerTest() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    public void processIncomingCarTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(inputReaderUtil.readSelection()).thenReturn(1);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parkingService.processIncomingVehicle();
        // vérifiez si la méthode updateParking a été appelée exactement une fois
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }


    @Test
    public void testGetVehicleRegNumber() throws Exception {
        // Arrange
        String vehicleRegNumber = "ABCDEF";
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // Act
        String result = parkingService.getVehichleRegNumber();

        // Assert
        assertEquals(vehicleRegNumber, result);
    }


    @Test
    public void processIncomingBikeTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(inputReaderUtil.readSelection()).thenReturn(2);

            ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(inputReaderUtil.readSelection()).thenReturn(2);
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testProcessExitingCar() {
        try {   // Mock input
            String regNumber = "ABCDEF";
            Ticket ticket = new Ticket();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setVehicleRegNumber(regNumber);
            ticket.setParkingSpot(parkingSpot);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000))); // 1 hour ago


            // Stub method calls
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(regNumber);
            when(ticketDAO.getTicket(regNumber)).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            // Call method under test
            parkingService.processExitingVehicle();

            // Verify method calls
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(regNumber);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            //verify(fareCalculatorService, times(1)).calculateFare(any(Ticket.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessExitingBike() {
        try {   // Mock input
            String regNumber = "ABCDEF";
            Ticket ticket = new Ticket();
            ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE, false);
            ticket.setVehicleRegNumber(regNumber);
            ticket.setParkingSpot(parkingSpot);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000))); // 1 hour ago

            // Stub method calls
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(regNumber);
            when(ticketDAO.getTicket(regNumber)).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            // Call method under test
            parkingService.processExitingVehicle();

            // Verify method calls
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(regNumber);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            // a tester dans les deux testes

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testProcessExitingVehicleWithTicketUpdateFailure() throws Exception {

        try {   // Mock input
            String regNumber = "ABCDEF";
            Ticket ticket = new Ticket();
            ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.CAR, false);
            ticket.setVehicleRegNumber(regNumber);
            ticket.setParkingSpot(parkingSpot);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000))); // 1 hour ago

            // Stub method calls
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(regNumber);
            when(ticketDAO.getTicket(regNumber)).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

            // Call method under test
            parkingService.processExitingVehicle();

            // Verify method calls
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(regNumber);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(0)).updateParking(parkingSpot);
            // a tester dans les deux testes

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testGetNextParkingNumberIfAvailable() {
        // GIVEN

        when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);


        // Mock the ParkingSpotDAO to return the expected parking number
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);



        // WHEN
        ParkingSpot parkingSpot1 = parkingService.getNextParkingNumberIfAvailable();
        Assertions.assertNotNull(parkingSpot1);
        Assertions.assertEquals(parkingSpot1.getParkingType(), ParkingType.CAR);

        // THEN
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));

    }


    @Test
    @DisplayName("\"Error fetching parking number from DB. Parking slots might be full\"")
    public void testGetNextParkingNumberIfAvailableThrowException() {
        // GIVEN

        when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        // Mock the ParkingSpotDAO to return the expected parking number
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);

        // WHEN
        ParkingSpot parkingSpot1 = parkingService.getNextParkingNumberIfAvailable();

        Assertions.assertNull(parkingSpot1);
        // THEN
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));

    }


    @Test
    @DisplayName("\"Incorrect input provided\"")
    public void testGetNextParkingNumberIfAvailableThrowExceptionInputInvalid() {
        // GIVEN

        when(inputReaderUtil.readSelection()).thenReturn(3);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        // WHEN
        parkingService.getNextParkingNumberIfAvailable();

        // THEN
        verify(parkingSpotDAO, never()).getNextAvailableSlot(any(ParkingType.class)); // assurez-vous que la méthode DAO n'est jamais appelée

    }
    @Test
    public void testProcessIncomingVehicle_catchBlock() throws Exception {
        // GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        doThrow(new RuntimeException()).when(parkingSpotDAO).updateParking(any(ParkingSpot.class));

        // WHEN
        parkingService.processIncomingVehicle();

        // THEN
        // No exceptions are thrown and the program should continue executing
        // without logging any error messages.
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(0)).saveTicket(any(Ticket.class));
    }



    @Test
    @DisplayName("Unable to process exiting vehicle\"")
    public void testProcessExitingCarThrowException() {
        try {   // Mock input
            String regNumber = "ABCDEF";
            Ticket ticket = new Ticket();
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket.setVehicleRegNumber(regNumber);
            ticket.setParkingSpot(parkingSpot);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000))); // 1 hour ago


            // Stub method calls
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(null);

            // Call method under test
            parkingService.processExitingVehicle();

            // Verify method calls
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, never()).getTicket(regNumber);
            verify(ticketDAO, never()).updateTicket(any(Ticket.class));

            //verify(fareCalculatorService, times(1)).calculateFare(any(Ticket.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*@Test
    public void testGetVehicleTypeWithInvalidInput(){
        // Set up input reader util to return an invalid input
        InputReaderUtil inputReaderUtil = mock(InputReaderUtil.class);
        when(inputReaderUtil.readSelection()).thenReturn(3);

        // Call the method and capture the exception
        try {
            ParkingService parkingType = parkingService;
            parkingType.
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Entered input is invalid", e.getMessage());
        }*/
    }
























