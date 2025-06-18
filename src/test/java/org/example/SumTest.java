package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SumTest {

    /**
     * Test class for the main method in the Sum class.
     * The main method calculates the sum of an array of integers
     * and prints the result to the console.
     */
    @Test
    public void testMainMethodOutput() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        String expectedOutput = "The sum is: 15" + System.lineSeparator();

        // Act
        Sum.main(new String[]{});

        // Assert
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testMainMethodWithDefaultArray() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        String expectedOutput = "The sum is: 15" + System.lineSeparator();

        // Act
        Sum.main(new String[]{});

        // Assert
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testMainMethodWithModifiedInput() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        System.setIn(new java.io.ByteArrayInputStream("6".getBytes()));
        String expectedOutput = "The sum is: 20" + System.lineSeparator();

        // Act
        Sum.main(new String[]{});

        // Assert
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testMainMethodWithEmptyInput() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        System.setIn(new java.io.ByteArrayInputStream("".getBytes()));
        String expectedOutput = "The sum is: 60" + System.lineSeparator();

        // Act
        Sum.main(new String[]{});

        // Assert
        assertEquals(expectedOutput, outputStream.toString());
    }
}