package com.ejemplo;

public class EmailResponse {
    private boolean done;
    private String errorMessage;

    public EmailResponse(boolean done, String errorMessage)
    {
        this.done = done;
        this.errorMessage = errorMessage;
    }

    public boolean isDone()
    {
        return this.done;
    }

    public void isDone(boolean done)
    {
        this.done = done;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
