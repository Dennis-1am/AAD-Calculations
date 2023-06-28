package com.signet.ActualArrivalDate;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonProperty("SKU")
    private String SKU; // in our case this will be SKU
    @JsonProperty("AAD_SKU_LeadTime")
    private String AAD_SKU_LeadTime;
    @JsonProperty("AAD_SKU_HolidayShippingMessage")
    private String AAD_SKU_HolidayShippingMessage;
    @JsonProperty("AAD_SKU_HolidaySKUOverrideDate")
    private String AAD_SKU_HolidaySKUOverrideDate;

    public Product() {

    }

    public Product(String SKU, String AAD_SKU_LeadTime, String AAD_SKU_HolidayShippingMessage, 
                   String AAD_SKU_HolidaySKUOverrideDate) {

        this.SKU = SKU;
        this.AAD_SKU_LeadTime = AAD_SKU_LeadTime;
        this.AAD_SKU_HolidayShippingMessage = AAD_SKU_HolidayShippingMessage;
        this.AAD_SKU_HolidaySKUOverrideDate = AAD_SKU_HolidaySKUOverrideDate;

    }

    public Object getSKU() {
        return SKU;
    }

    public Item toItem() {

        Item item = new Item().withPrimaryKey("SKU", SKU)
                              .withString("AAD_SKU_LeadTime", AAD_SKU_LeadTime)
                              .withString("AAD_SKU_HolidayShippingMessage", AAD_SKU_HolidayShippingMessage)
                              .withString("AAD_SKU_HolidaySKUOverrideDate", AAD_SKU_HolidaySKUOverrideDate);

        return item;

    }

    public String toString() {
        return "{\n" + "Product SKU: " + SKU + "\n}";
    }

}
