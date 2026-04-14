package com.ejemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class SolicitudTest {

    @Test
    public void testSetAndGetCantidadesIniciales() {
        Solicitud solicitud = new Solicitud();
        List<Integer> cantidades = Arrays.asList(1, 2, 3);
        solicitud.setCantidadesIniciales(cantidades);
        assertEquals(cantidades, solicitud.getCantidadesIniciales());
    }

    @Test
    public void testSetCantidadesInicialesToNull() {
        Solicitud solicitud = new Solicitud();
        solicitud.setCantidadesIniciales(null);
        assertNull(solicitud.getCantidadesIniciales());
    }

    @Test
    public void testSetCantidadesInicialesToEmptyList() {
        Solicitud solicitud = new Solicitud();
        List<Integer> emptyList = Arrays.asList();
        solicitud.setCantidadesIniciales(emptyList);
        assertEquals(emptyList, solicitud.getCantidadesIniciales());
    }

    @Test
    public void testSetAndGetNombreEntidades() {
        Solicitud solicitud = new Solicitud();
        List<String> nombres = Arrays.asList("Entidad1", "Entidad2");
        solicitud.setNombreEntidades(nombres);
        assertEquals(nombres, solicitud.getNombreEntidades());
    }

    @Test
    public void testSetNombreEntidadesToNull() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(null);
        assertNull(solicitud.getNombreEntidades());
    }

    @Test
    public void testSetNombreEntidadesToEmptyList() {
        Solicitud solicitud = new Solicitud();
        List<String> emptyList = Arrays.asList();
        solicitud.setNombreEntidades(emptyList);
        assertEquals(emptyList, solicitud.getNombreEntidades());
    }
}