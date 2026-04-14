package com.ejemplo;

/**
 * Clase que representa la respuesta de una operación de envío de email.
 * Contiene información sobre si la operación se completó correctamente
 * y un posible mensaje de error en caso de fallo.
 */
public class EmailResponse {
    /**
     * Indica si la operación se ha completado correctamente
     */
    private boolean done;
    /**
     * Mensaje de error en caso de que la operación falle
     */
    private String errorMessage;

    /**
     * Constructor de la clase EmailResponse.
     *
     * @param done indica si la operación fue exitosa
     * @param errorMessage mensaje de error en caso de fallo
     */
    public EmailResponse(boolean done, String errorMessage)
    {
        this.done = done;
        this.errorMessage = errorMessage;
    }

    /**
     * Devuelve el estado de la operación.
     *
     * @return true si la operación se completó correctamente, false en caso contrario
     */
    public boolean isDone()
    {
        return this.done;
    }

    /**
     * Establece el estado de la operación.
     *
     * @param done nuevo estado de la operación
     */
    public void isDone(boolean done)
    {
        this.done = done;
    }

    /**
     * Obtiene el mensaje de error.
     *
     * @return mensaje de error o null si no hubo errores
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    /**
     * Establece el mensaje de error.
     *
     * @param errorMessage mensaje de error a asignar
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
