package com.signet.ActualArrivalDate;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    public Response(int i, Map<String, String> headers2, String json) {
    }
    @JsonProperty("statusCode")
    private int statusCode;
    @JsonProperty("headers")
    private Map<String, String> headers;
    @JsonProperty("body")
    private String body;

}
