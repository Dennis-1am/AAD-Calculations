package com.signet.ActualArrivalDate.models;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonProperty("AAD_Product_Code")
    private String AAD_Product_Code; // in our case this will be SKU
    @JsonProperty("AAD_SKU_LeadTime")
    private String AAD_SKU_LeadTime;
    @JsonProperty("AAD_SKU_HolidayShippingMessage")
    private String AAD_SKU_HolidayShippingMessage;
    @JsonProperty("AAD_SKU_HolidaySKUOverrideDate")
    private String AAD_SKU_HolidaySKUOverrideDate;

    public Product() {

    }

    public Product(String SKU) {

        this.AAD_Product_Code = SKU;

    }

    public Product(String SKU, String AAD_SKU_LeadTime) {

        this.AAD_Product_Code = SKU;
        this.AAD_SKU_LeadTime = AAD_SKU_LeadTime;

    }

    public Product(String SKU, String AAD_SKU_LeadTime, String AAD_SKU_HolidayShippingMessage) {

        this.AAD_Product_Code = SKU;
        this.AAD_SKU_LeadTime = AAD_SKU_LeadTime;
        this.AAD_SKU_HolidayShippingMessage = AAD_SKU_HolidayShippingMessage;

    }

    public Product(String SKU, String AAD_SKU_LeadTime, String AAD_SKU_HolidayShippingMessage, 
                   String AAD_SKU_HolidaySKUOverrideDate) {

        this.AAD_Product_Code = SKU;
        this.AAD_SKU_LeadTime = AAD_SKU_LeadTime;
        this.AAD_SKU_HolidayShippingMessage = AAD_SKU_HolidayShippingMessage;
        this.AAD_SKU_HolidaySKUOverrideDate = AAD_SKU_HolidaySKUOverrideDate;

    }

    public Object getSKU() {
        return AAD_Product_Code;
    }

    public Item toItem() {

        Item item = new Item().withPrimaryKey("SKU", AAD_Product_Code)
                              .withString("AAD_SKU_LeadTime", AAD_SKU_LeadTime)
                              .withString("AAD_SKU_HolidayShippingMessage", AAD_SKU_HolidayShippingMessage)
                              .withString("AAD_SKU_HolidaySKUOverrideDate", AAD_SKU_HolidaySKUOverrideDate);

        return item;

    }

    public String toString() {
        return "{\n" + "Product SKU: " + AAD_Product_Code + "\n}";
    }

}
