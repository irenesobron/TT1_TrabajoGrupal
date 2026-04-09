package java.com.ejemplo;

public class SolicitudResponse {
    private boolean done;

    private Integer tokenSolicitud;

    private String errorMessage;

    private boolean data;



    public SolicitudResponse(boolean done, Integer tokenSolicitud, String errorMessage, boolean data) {

        this.done = done;

        this.tokenSolicitud = tokenSolicitud;

        this.errorMessage = errorMessage;

        this.data = data;

    }



    // Getters y setters...

    public boolean isDone() {

        return done;

    }



    public void setDone(boolean done) {

        this.done = done;

    }



    public Integer getTokenSolicitud() {

        return tokenSolicitud;

    }



    public void setTokenSolicitud(Integer tokenSolicitud) {

        this.tokenSolicitud = tokenSolicitud;

    }



    public String getErrorMessage() {

        return errorMessage;

    }



    public void setErrorMessage(String errorMessage) {

        this.errorMessage = errorMessage;

    }



    public boolean isData() {

        return data;

    }



    public void setData(boolean data) {

        this.data = data;

    }

}
}
