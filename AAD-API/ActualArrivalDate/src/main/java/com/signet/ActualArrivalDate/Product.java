package com.signet.ActualArrivalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonProperty("sku")
    private Object sku; // in our case this will be SKU

    public Product(){
        
    }

    public Product(String sku) {
        this.sku = sku;
    }

    public Object getReqData() {
        return sku;
    }

    public String toString() {
        return "{\n" + "Product SKU: " + sku + "\n}";
    }

}
