package com.ejemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SolicitudResponseTest {

    @Test
    public void testConstructor() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Error occurred", false);
        assertTrue(sr.isDone());
        assertEquals(123, sr.getTokenSolicitud());
        assertEquals("Error occurred", sr.getErrorMessage());
        assertFalse(sr.isData());
    }

    @Test
    public void testConstructorWithNulls() {
        SolicitudResponse sr = new SolicitudResponse(false, null, null, true);
        assertFalse(sr.isDone());
        assertNull(sr.getTokenSolicitud());
        assertNull(sr.getErrorMessage());
        assertTrue(sr.isData());
    }

    @Test
    public void testSetDone() {
        SolicitudResponse sr = new SolicitudResponse(false, 123, "Error", false);
        sr.setDone(true);
        assertTrue(sr.isDone());
    }

    @Test
    public void testSetTokenSolicitud() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Error", false);
        sr.setTokenSolicitud(456);
        assertEquals(456, sr.getTokenSolicitud());
    }

    @Test
    public void testSetTokenSolicitudToNull() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Error", false);
        sr.setTokenSolicitud(null);
        assertNull(sr.getTokenSolicitud());
    }

    @Test
    public void testSetErrorMessage() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Old error", false);
        sr.setErrorMessage("New error");
        assertEquals("New error", sr.getErrorMessage());
    }

    @Test
    public void testSetErrorMessageToNull() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Error", false);
        sr.setErrorMessage(null);
        assertNull(sr.getErrorMessage());
    }

    @Test
    public void testSetData() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Error", false);
        sr.setData(true);
        assertTrue(sr.isData());
    }

    @Test
    public void testSetDataToFalse() {
        SolicitudResponse sr = new SolicitudResponse(true, 123, "Error", true);
        sr.setData(false);
        assertFalse(sr.isData());
    }
}