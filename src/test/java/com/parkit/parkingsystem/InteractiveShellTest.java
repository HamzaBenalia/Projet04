package com.parkit.parkingsystem;


import com.parkit.parkingsystem.service.InteractiveShell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

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


}







