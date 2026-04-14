package com.ejemplo;
/**
 * Clase que representa la respuesta de una solicitud.
 *
 * Incluye información sobre si la operación fue exitosa,
 * el identificador de la solicitud, un posible mensaje de error
 * y un resultado booleano asociado a la operación.
 */
public class SolicitudResponse {
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
     * Resultado de la operación en forma de valor booleano.
     */
    private boolean data;

    /**
     * Constructor de la clase SolicitudResponse.
     *
     * @param done indica si la operación fue exitosa
     * @param tokenSolicitud identificador de la solicitud
     * @param errorMessage mensaje de error en caso de fallo
     * @param data resultado booleano de la operación
     */
    public SolicitudResponse(boolean done, Integer tokenSolicitud, String errorMessage, boolean data) {
        this.done = done;
        this.tokenSolicitud = tokenSolicitud;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    /**
     * Obtiene el estado de la operación.
     *
     * @return true si la operación fue exitosa, false en caso contrario
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
     * Obtiene el resultado de la operación.
     *
     * @return valor booleano del resultado
     */
    public boolean isData() {
        return data;
    }

    /**
     * Establece el resultado de la operación.
     *
     * @param data valor booleano del resultado
     */
    public void setData(boolean data) {
        this.data = data;
    }

}

