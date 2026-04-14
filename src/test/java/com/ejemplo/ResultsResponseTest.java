package com.ejemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResultsResponseTest {

    @Test
    public void testConstructor() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Error occurred", "Some data");
        assertTrue(rr.isDone());
        assertEquals(123, rr.getTokenSolicitud());
        assertEquals("Error occurred", rr.getErrorMessage());
        assertEquals("Some data", rr.getData());
    }

    @Test
    public void testConstructorWithNulls() {
        ResultsResponse rr = new ResultsResponse(false, null, null, null);
        assertFalse(rr.isDone());
        assertNull(rr.getTokenSolicitud());
        assertNull(rr.getErrorMessage());
        assertNull(rr.getData());
    }

    @Test
    public void testSetDone() {
        ResultsResponse rr = new ResultsResponse(false, 123, "Error", "Data");
        rr.setDone(true);
        assertTrue(rr.isDone());
    }

    @Test
    public void testSetTokenSolicitud() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Error", "Data");
        rr.setTokenSolicitud(456);
        assertEquals(456, rr.getTokenSolicitud());
    }

    @Test
    public void testSetTokenSolicitudToNull() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Error", "Data");
        rr.setTokenSolicitud(null);
        assertNull(rr.getTokenSolicitud());
    }

    @Test
    public void testSetErrorMessage() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Old error", "Data");
        rr.setErrorMessage("New error");
        assertEquals("New error", rr.getErrorMessage());
    }

    @Test
    public void testSetErrorMessageToNull() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Error", "Data");
        rr.setErrorMessage(null);
        assertNull(rr.getErrorMessage());
    }

    @Test
    public void testSetData() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Error", "Old data");
        rr.setData("New data");
        assertEquals("New data", rr.getData());
    }

    @Test
    public void testSetDataToNull() {
        ResultsResponse rr = new ResultsResponse(true, 123, "Error", "Data");
        rr.setData(null);
        assertNull(rr.getData());
    }
}