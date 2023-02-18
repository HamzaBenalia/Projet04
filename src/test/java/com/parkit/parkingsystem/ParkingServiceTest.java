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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @Mock
    private static ParkingSpot parkingSpot;

    @Mock
    private static Ticket ticket;

    @Mock
    private static FareCalculatorService fareCalculatorService;


    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);


            ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    //@Test
    public void processExitingVehicleTest() {
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
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
    public void processIncomingBikeTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("CDEFG");
            when(inputReaderUtil.readSelection()).thenReturn(2);

            ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("CDEFG");
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
            //when(ticketDAO.getTicket(regNumber)).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            // Call method under test
            parkingService.processExitingVehicle();

            // Verify method calls
            verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
            verify(ticketDAO, times(1)).getTicket(regNumber);
            verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
            verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            verify(fareCalculatorService, times(1)).calculateFare(any(Ticket.class));
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
           // verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
            // a tester dans les deux testes

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}












