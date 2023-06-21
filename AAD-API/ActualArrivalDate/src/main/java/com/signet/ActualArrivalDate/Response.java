package com.signet.ActualArrivalDate;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    @JsonProperty("isBase64Encoded")
    private boolean isBase64Encoded;
    @JsonProperty("statusCode")
    private int statusCode;
    @JsonProperty("headers")
    private Map<String, String> headers;
    @JsonProperty("body")
    private String body;

    public Response() {

    }

    /**
     * {
    "isBase64Encoded": true|false,
    "statusCode": httpStatusCode,
    "headers": { "headerName": "headerValue", ... },
    "multiValueHeaders": { "headerName": ["headerValue", "headerValue2", ...], ... },
    "body": "..."
    }
     */

    public Response(int statusCode, Map<String, String> headers, String body, boolean isBase64Encoded) {
        this.isBase64Encoded = isBase64Encoded;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}
