package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
@ExtendWith(MockitoExtension.class)
public class ParkingSpotDaoTest {

    public static ParkingSpotDAO parkingSpotDAO;

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    void setUpPerTest() {
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testGetNextAvailableSlotCar() {
        int actualResult = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(1, actualResult);
    }

    @Test
    public void testGetNextAvailableSlotBike() {

        int actualResult = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(4, actualResult);
    }

    @Test
    public void nextAvailableShouldThrowException() {
        parkingSpotDAO.dataBaseConfig = null;
        Assertions.assertThrows(Exception.class, () -> parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void testUpdateParkingCar() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        boolean actualResult = parkingSpotDAO.updateParking(parkingSpot);
        Assertions.assertTrue(actualResult, "Parking spot update failed");
    }

    @Test
    public void testUpdateParkingBike() {
        ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE, true);

        // call the updateParking method with the ParkingSpot object
        boolean actualResult = parkingSpotDAO.updateParking(parkingSpot);

        // verify that the update was successful
        Assertions.assertTrue(actualResult, "Parking spot update failed");
    }

    @Test
    public void testUpdateParkingWithInvalidId() {

        ParkingSpot parkingSpot = new ParkingSpot(-1, ParkingType.CAR, true);

        boolean actualResult = parkingSpotDAO.updateParking(parkingSpot);

        Assertions.assertFalse(actualResult, "Parking spot update successfully");
    }

    @Test
    public void testUpdateShouldThrowException(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.dataBaseConfig = null;
        Assertions.assertThrows(Exception.class, ()-> parkingSpotDAO.updateParking(parkingSpot));
    }
}




