package com.ejemplo;

public class ResultsResponse {

    private boolean done;
    private Integer tokenSolicitud;
    private String errorMessage;
    private String data;

    public ResultsResponse(boolean done, Integer tokenSolicitud, String errorMessage, String data) {
        this.done = done;
        this.tokenSolicitud = tokenSolicitud;
        this.errorMessage = errorMessage;
        this.data = data;
    }

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



    public String getData() {

        return data;

    }



    public void setData(String data) {

        this.data = data;

    }

}