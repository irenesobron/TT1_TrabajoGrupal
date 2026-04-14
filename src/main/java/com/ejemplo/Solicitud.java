package com.ejemplo;

import java.util.List;

/**
 * Clase que representa una solicitud de entrada para un proceso.
 * Contiene listas de cantidades iniciales y nombres de entidades
 * asociadas a dichas cantidades.
 */

public class Solicitud {
    /**
     * Lista de cantidades iniciales asociadas a cada entidad.
     */
    private List<Integer> cantidadesIniciales;
    /**
     * Lista de nombres de las entidades.
     */
    private List<String> nombreEntidades;

    /**
     * Obtiene la lista de cantidades iniciales.
     *
     * @return lista de cantidades iniciales
     */
    public List<Integer> getCantidadesIniciales() {
        return cantidadesIniciales;
    }

    /**
     * Establece la lista de cantidades iniciales.
     *
     * @param cantidadesIniciales lista de cantidades a asignar
     */
    public void setCantidadesIniciales(List<Integer> cantidadesIniciales) {
        this.cantidadesIniciales = cantidadesIniciales;
    }

    /**
     * Obtiene la lista de nombres de entidades.
     *
     * @return lista de nombres de entidades
     */
    public List<String> getNombreEntidades() {
        return nombreEntidades;
    }

    /**
     * Establece la lista de nombres de entidades.
     *
     * @param nombreEntidades lista de nombres a asignar
     */
    public void setNombreEntidades(List<String> nombreEntidades) {
        this.nombreEntidades = nombreEntidades;
    }
}
