package com.ejemplo;
/**
 * Clase que representa los detalles de un problema en un una respuesta.
 * Contiene información descriptiva sobre un error ocurrido en una operación.
 */
public class ProblemDetails {
    /**
     * Cadena que identifica el tipo de problema.
     */
    private String type;
    /**
     * Título del problema.
     */
    private String title;
    /**
     * Código de estado asociado al problema.
     */
    private Integer status;
    /**
     * Descripción del problema de forma detallada
     */
    private String detail;
    /**
     * Instancia del problema
     */
    private String instance;

    /**
     * Constructor de la clase ProblemDetails.
     *
     * @param type Cadena que identifica el tipo de problema
     * @param title título breve del problema
     * @param status código de estado
     * @param detail descripción detallada del problema
     * @param instance instancia del problema
     */
    public ProblemDetails(String type, String title, Integer status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }

    /**
     * Obtiene el tipo de probelma
     *
     * @return cadena que identifica el tipo de problema
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo de problema.
     *
     * @param type cadena que identifica el tipo de problema
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el título del problema.
     *
     * @return título del problema
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del problema.
     *
     * @param title título del problema
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene el código de estado del problema.
     *
     * @return código de estado del propblema
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Establece el código de estado del problema.
     *
     * @param status código de estado del problema
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Obtiene la descripción detallada del problema.
     *
     * @return descripción del problema
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Establece la descripción detallada del problema.
     *
     * @param detail descripción del problema
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Obtiene la instancia del problema.
     *
     * @return instancia específica del problema
     */
    public String getInstance() {
        return instance;
    }

    /**
     * Establece la instancia del problema.
     *
     * @param instance instancia específica del problema
     */
    public void setInstance(String instance) {
        this.instance = instance;
    }
}
