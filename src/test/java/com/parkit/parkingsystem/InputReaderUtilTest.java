package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.*;


public class InputReaderUtilTest {



    @Test
    public void testReadVehicleRegistrationNumber() throws Exception {
        String input = "ABCDEF";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        InputReaderUtil inputReaderUtil = new InputReaderUtil();

        String registrationNumber = inputReaderUtil.readVehicleRegistrationNumber();
        assertEquals(input, registrationNumber);
    }


    @Test
    public void testReadVehicleRegistrationNumberWithInvalidInput() {
        String input = "   ";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inputReaderUtil.readVehicleRegistrationNumber();
        });


        assertEquals("Invalid input provided", exception.getMessage());
    }




    @Test
    public void testReadSelection() throws Exception {
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        int selection = inputReaderUtil.readSelection();
        assertEquals(1, selection);
    }

    @Test
    public void ReadSelectionWithInvalidInput () throws Exception {

        String input = "invalid input";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputReaderUtil inputReaderUtil = new InputReaderUtil();

        int selection = inputReaderUtil.readSelection();
        assertEquals(-1, selection);
    }

}




