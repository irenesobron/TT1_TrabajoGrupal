package com.ejemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailResponseTest {

    @Test
    public void testConstructor() {
        EmailResponse response = new EmailResponse(true, "Error occurred");
        assertTrue(response.isDone());
        assertEquals("Error occurred", response.getErrorMessage());
    }

    @Test
    public void testConstructorWithFalseAndNull() {
        EmailResponse response = new EmailResponse(false, null);
        assertFalse(response.isDone());
        assertNull(response.getErrorMessage());
    }

    @Test
    public void testSetDone() {
        EmailResponse response = new EmailResponse(false, "Error");
        response.isDone(true); // Note: method name is isDone, but it's a setter
        assertTrue(response.isDone());
    }

    @Test
    public void testSetErrorMessage() {
        EmailResponse response = new EmailResponse(true, "Old error");
        response.setErrorMessage("New error");
        assertEquals("New error", response.getErrorMessage());
    }

    @Test
    public void testSetErrorMessageToNull() {
        EmailResponse response = new EmailResponse(true, "Error");
        response.setErrorMessage(null);
        assertNull(response.getErrorMessage());
    }
}