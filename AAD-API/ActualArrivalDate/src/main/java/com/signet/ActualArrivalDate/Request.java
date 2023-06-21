package com.signet.ActualArrivalDate;

public class Request {
    private Object reqType; // in our case this will be "GET"
    private Object reqData; // in our case this will be SKU

    public Request(){
        
    }

    public Request(String reqType, String reqData) {
        this.reqType = reqType;
        this.reqData = reqData;
    }
    
    public Object getReqType() {
        return reqType;
    }

    public Object getReqData() {
        return reqData;
    }

    public String toString() {
        return "{\n" + "Request Type: " + reqType + "\n Request Data: " + reqData + "\n}";
    }

}
