package com.signet.ActualArrivalDate.models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    @JsonProperty("statusCode")
    private int statusCode;
    @JsonProperty("headers")
    private Map<String, String> headers;
    @JsonProperty("body")
    private String body;

    public Response() {

    }

    public Response(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}
