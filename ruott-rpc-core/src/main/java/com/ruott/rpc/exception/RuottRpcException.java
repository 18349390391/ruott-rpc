package com.ruott.rpc.exception;


public class RuottRpcException extends RuntimeException {

    public RuottRpcException() {
        super();
    }

    public RuottRpcException(String message) {
        super(message);

    }

    public RuottRpcException(Throwable cause) {
        super(cause);

    }

    public RuottRpcException(String message, Throwable cause) {
        super(message, cause);
    }

}
