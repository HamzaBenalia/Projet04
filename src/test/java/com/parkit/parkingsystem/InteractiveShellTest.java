package com.parkit.parkingsystem;


import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import groovy.transform.TimedInterrupt;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;

import static com.parkit.parkingsystem.service.InteractiveShell.loadInterface;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InteractiveShellTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOutput;

    @Test
    void testLoadMenu() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        InteractiveShell.loadMenu();

        String expectedOutput = "Please select an option. Simply enter the number to choose an action\n" +
                "1 New Vehicle Entering - Allocate Parking Space\n" +
                "2 Vehicle Exiting - Generate Ticket Price\n" +
                "3 Shutdown System\n";


        expectedOutput = expectedOutput.trim().replaceAll("\\s+", " ");
        String actualOutput = outContent.toString().trim().replaceAll("\\s+", " ");

        assertEquals(expectedOutput, actualOutput);
    }


    /*@Test
    public void testLoadInterface() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String simulatedUserInput = "3\n"; // sélectionne l'option 3 pour sortir de la boucle
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        InteractiveShell.loadInterface();

        String consoleOutput = outContent.toString();

        assertTrue(consoleOutput.contains("Welcome to Parking System!"));
        assertTrue(consoleOutput.contains("Exiting from the system!"));

        System.setIn(savedStandardInputStream);
    }*/

}


















