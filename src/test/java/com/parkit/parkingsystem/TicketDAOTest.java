package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketDAOTest {
    private static final Logger logger = LogManager.getLogger("TicketDAO");


    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    static void setUp(){
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    void setUpPerTest(){
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService.clearDataBaseEntries();
    }


    @Test
    public void testSaveTicket() {
        // Créer un ticket avec des valeurs de test
        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1.125);
        ticket.setInTime(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000));
        ticket.setOutTime(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000));

        // Appeler la méthode saveTicket pour enregistrer le ticket dans la base de données
        boolean result = ticketDAO.saveTicket(ticket);

        // Vérifier que la méthode renvoie true
        assertTrue(result);
    }

    @Test
    public void TestSaveTicketThrowExceptionErrorFetchingNextAvailableSlot() {
        try {
            Ticket ticket = new Ticket();
            ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, false);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(1.125);
            ticket.setInTime(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000));
            ticket.setOutTime(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000));
            boolean result = ticketDAO.saveTicket(ticket);
            Assertions.assertFalse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void TestGetTicket() {
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setPrice(1.125);

        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        boolean saveTicket = ticketDAO.saveTicket(ticket);

        Assertions.assertTrue(saveTicket);

        Ticket retrieveTicket = ticketDAO.getTicket(vehicleRegNumber);

        Assertions.assertNotNull(retrieveTicket);
        Assertions.assertEquals(vehicleRegNumber, retrieveTicket.getVehicleRegNumber());
        Assertions.assertEquals(ticket.getPrice(), retrieveTicket.getPrice());

    }

    @Test
    public void testUpdateTicket() {
        // Créer un ticket avec des valeurs de test
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setPrice(1.125);
        ticket.setOutTime(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000));
        // Appeler la méthode updateTicket pour mettre à jour le ticket dans la base de données
        boolean result = ticketDAO.updateTicket(ticket);

        // Vérifier que la méthode renvoie true
        assertTrue(result);
    }

    @Test
    @DisplayName("Error saving ticket info")
    public void testUpdateTicketFailed() throws SQLException {
        try {
            Ticket ticket = new Ticket();
            ticket.setId(1);
            ticket.setPrice(1.125);

            boolean result = ticketDAO.updateTicket(ticket);
            assertFalse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void checkIfRegVehicleNumberAlreadyExistTest() throws Exception {

        Date date = new Date();
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setPrice(1.125);
        ticket.setOutTime(date);
        ticket.setInTime(date);
        ticketDAO.saveTicket(ticket);
        boolean result = ticketDAO.checkIfRegVehicleNumberAlreadyExist("xxxxx");

        // Vérifier le result est true (existe déjà dans la BD) si false on aura un message "vehicle does not existe"
        Assertions.assertFalse(result, "vehicle does exist ");

    }

    @Test
    public void checkIfRegVehicleNumberAlreadyExistTestPasses() throws Exception {
        Date date = new Date();
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setPrice(1.125);
        ticket.setOutTime(date);
        ticket.setInTime(date);
        ticketDAO.saveTicket(ticket);
        boolean result = ticketDAO.checkIfRegVehicleNumberAlreadyExist(vehicleRegNumber);
        Assertions.assertTrue(result);

    }


    @Test
    public void testGetTicketException() throws SQLException, ClassNotFoundException {

        Date date = new Date();
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setPrice(1.125);
        ticket.setOutTime(date);
        ticket.setInTime(date);
        ticketDAO.saveTicket(ticket);
        ticketDAO.dataBaseConfig = null;

        Assertions.assertThrows(Exception.class,()-> ticketDAO.checkIfRegVehicleNumberAlreadyExist(vehicleRegNumber));

        }


    }





