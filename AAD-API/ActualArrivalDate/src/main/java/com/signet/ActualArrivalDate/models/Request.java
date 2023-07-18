package com.signet.ActualArrivalDate.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    @JsonProperty("SKU")
    String SKU;
    @JsonProperty("Vendor_ID")
    String Vendor_ID;
    @JsonProperty("Shipping_Method")
    String Shipping_Method;

    public Request() {

    }

    public Request(String SKU, String Vendor_ID, String Shipping_Method) {

        this.SKU = SKU;
        this.Vendor_ID = Vendor_ID;
        this.Shipping_Method = Shipping_Method;

    }

    public String getSKU() {
        return SKU;
    }

    public String getVendor() {
        return Vendor_ID;
    }

    public String getShipping_Method() {
        return Shipping_Method;
    }
    
}
