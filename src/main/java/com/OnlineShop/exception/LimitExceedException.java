package com.OnlineShop.exception;

public class LimitExceedException extends RuntimeException
{
    public LimitExceedException()
    {
        super();
    }

    public LimitExceedException(String message)
    {
        super(message);
    }

    public LimitExceedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public LimitExceedException(Throwable cause)
    {
        super(cause);
    }

    protected LimitExceedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
