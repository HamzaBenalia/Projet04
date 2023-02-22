package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class ParkingSpotDaoTest {

    public static ParkingSpotDAO parkingSpotDAO;


    @BeforeAll
    public static void setup() {
        parkingSpotDAO = new ParkingSpotDAO();
    }

    @Test
    public void testGetNextAvailableSlotCar() {
        // Création d'un objet "ParkingSpotDAO"

        // Appel de la méthode "getNextAvailableSlot" avec un type de stationnement valide
        int actualResult = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        // Vérification du résultat
        assertEquals(1, actualResult);
    }

    @Test
    public void testGetNextAvailableSlotBike() {
        // Création d'un objet "ParkingSpotDAO"

        // Appel de la méthode "getNextAvailableSlot" avec un type de stationnement valide
        int actualResult = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        // Vérification du résultat
        assertEquals(4, actualResult);
    }


    // tester la méthode updateParking ();

    @Test
    public void testUpdateParkingCar() {
        // create a ParkingSpot object with initial availability set to true
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        // call the updateParking method with the ParkingSpot object
        boolean actualResult = parkingSpotDAO.updateParking(parkingSpot);

        // verify that the update was successful
        Assertions.assertTrue(actualResult, "Parking spot update failed");
    }
    @Test
    public void testUpdateParkingBike() {
        // create a ParkingSpot object with initial availability set to true
        ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE, true);

        // call the updateParking method with the ParkingSpot object
        boolean actualResult = parkingSpotDAO.updateParking(parkingSpot);

        // verify that the update was successful
        Assertions.assertTrue(actualResult, "Parking spot update failed");
    }

    @Test
    public void testUpdateParkingWithInvalidId(){

        ParkingSpot parkingSpot = new ParkingSpot(-1, ParkingType.CAR, true);

        boolean actualResult = parkingSpotDAO.updateParking(parkingSpot);

        Assertions.assertFalse(actualResult, "Parking spot update successfully");
    }
}




