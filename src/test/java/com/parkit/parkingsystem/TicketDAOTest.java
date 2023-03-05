package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.Date;

//import static com.parkit.parkingsystem.ParkingServiceTest.ticketDAO;
import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {
    private static final Logger logger = LogManager.getLogger("TicketDAO");
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();


    @Test
    public void testSaveTicket() {
        // Créer un ticket avec des valeurs de test
        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1);
        ticket.setInTime(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000));
        ticket.setOutTime(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000));

        // Créer une instance de la classe à tester
        TicketDAO ticketDAO = new TicketDAO();

        // Appeler la méthode saveTicket pour enregistrer le ticket dans la base de données
        boolean result = ticketDAO.saveTicket(ticket);

        // Vérifier que la méthode renvoie true
        assertFalse(result);
    }



    @Test
    public void getTicket(){
        TicketDAO ticketDAO = new TicketDAO();
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setPrice(1.125);

        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());

        boolean saveTicket = ticketDAO.saveTicket(ticket);

        Assertions.assertFalse(saveTicket);

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

        // Créer une instance de la classe à tester
        TicketDAO ticketDAO = new TicketDAO();

        // Appeler la méthode updateTicket pour mettre à jour le ticket dans la base de données
        boolean result = ticketDAO.updateTicket(ticket);

        // Vérifier que la méthode renvoie true
        assertTrue(result);
    }

    @Test
    public void checkIfRegVehicleNumberAlreadyExistTest (){
        String vehicleRegNumber = "ABCDEF";
        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        // créer une instance de la classe à tester
        TicketDAO  ticketDAO = new TicketDAO();

        // Appeler la méthode checkIfRegVehicleNumberAlreadyExist pour checker si le regNumber est déja dans la BD
         boolean result = ticketDAO.checkIfRegVehicleNumberAlreadyExist(vehicleRegNumber);

         // Vérifier le result est true (existe déjà dans la BD) si false on aura un message "vehicle does not existe"
         Assertions.assertTrue(result, "vehicle does not existe");

    }

}
