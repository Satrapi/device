package com.artronics.sdwn.device.exception;

public class SdwnControllerNotFound extends RuntimeException
{
    public SdwnControllerNotFound()
    {
    }

    public SdwnControllerNotFound(String message)
    {
        super(message);
    }

    public SdwnControllerNotFound(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SdwnControllerNotFound(Throwable cause)
    {
        super(cause);
    }

    public SdwnControllerNotFound(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
