package com.ruott.rpc.model;

import lombok.Getter;

@Getter
public enum HttpStatus {

    OK(200, "ok"),
    SERVICE_ERROR(500, "service error"),
    RPC_REQUEST_NULL(501, "rpc request is null"),
    RPC_SERVICE_NULL(502,"rpc service is null"),;

    private final Integer code;
    private final String message;

    HttpStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
