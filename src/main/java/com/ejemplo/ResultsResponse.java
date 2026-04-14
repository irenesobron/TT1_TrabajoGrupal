package com.ejemplo;
/**
 * Clase que representa la respuesta de un proceso o solicitud.
 * Contiene información sobre el estado de la operación, un identificador
 * de la solicitud, posibles mensajes de error y los datos resultantes.
 */

public class ResultsResponse {
    /**
     * Indica si la operación se ha completado correctamente.
     */
    private boolean done;
    /**
     * Identificador único de la solicitud (token).
     */
    private Integer tokenSolicitud;
    /**
     * Mensaje de error en caso de que la operación falle.
     */
    private String errorMessage;
    /**
     * Datos devueltos como resultado de la operación.
     */
    private String data;

    /**
     * Constructor de la clase ResultsResponse.
     *
     * @param done indica si la operación fue exitosa
     * @param tokenSolicitud identificador de la solicitud
     * @param errorMessage mensaje de error en caso de fallo
     * @param data datos resultantes de la operación
     */
    public ResultsResponse(boolean done, Integer tokenSolicitud, String errorMessage, String data) {
        this.done = done;
        this.tokenSolicitud = tokenSolicitud;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    /**
     * Obtiene el resultado de la operación
     * @return true si la operación fue exitosa y false en caso contario
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Establece el estado de la operación.
     *
     * @param done nuevo estado de la operación
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Obtiene el identificador de la solicitud.
     *
     * @return token de la solicitud
     */
    public Integer getTokenSolicitud() {
        return tokenSolicitud;
    }
    /**
     * Establece el identificador de la solicitud.
     *
     * @param tokenSolicitud nuevo token de la solicitud
     */
    public void setTokenSolicitud(Integer tokenSolicitud) {
        this.tokenSolicitud = tokenSolicitud;
    }

    /**
     * Obtiene el mensaje de error.
     *
     * @return mensaje de error o null si no hubo errores
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Establece el mensaje de error.
     *
     * @param errorMessage mensaje de error a asignar
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Obtiene los datos resultantes de la operación.
     *
     * @return datos en formato String
     */
    public String getData() {
        return data;
    }

    /**
     * Establece los datos resultantes de la operación.
     *
     * @param data datos a asignar
     */
    public void setData(String data) {
        this.data = data;
    }

}