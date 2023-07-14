package com.signet.ActualArrivalDate.models;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vendor {

    @JsonProperty("Vendor_ID")
    private String Vendor_ID;
    @JsonProperty("Vendor_Name")
    private String Vendor_Name;
    @JsonProperty("AAD_Vendor_Lead_Time")
    private String AAD_Vendor_Lead_Time;
    @JsonProperty("AAD_Vendor_Cutoff_Time")
    private String AAD_Vendor_Cutoff_Time;
    @JsonProperty("AAD_Vendor_Override_Date")
    private String AAD_Vendor_Override_Date;
    @JsonProperty("AAD_Vendor_Outage_Start")
    private String AAD_Vendor_Outage_Start;
    @JsonProperty("AAD_Vendor_Outage_End")
    private String AAD_Vendor_Outage_End;
    @JsonProperty("Address_Line_1")
    private String Address_Line_1;
    @JsonProperty("Address_Line_2")
    private String Address_Line_2;
    @JsonProperty("City")
    private String City;
    @JsonProperty("State")
    private String State;
    @JsonProperty("Zip")
    private String Zip;
    @JsonProperty("ISO_Code")
    private String ISO_Code;
    @JsonProperty("Vendor_Catalog_Name")
    private String Vendor_Catalog_Name;
    @JsonProperty("Model_Type")
    private String Model_Type;

    public Vendor(String vendor_ID, String vendor_Name, String aAD_Vendor_Lead_Time, String aAD_Vendor_Cutoff_Time,
            String aAD_Vendor_Override_Date, String aAD_Vendor_Outage_Start, String aAD_Vendor_Outage_End,
            String address_Line_1, String address_Line_2, String city, String state, String zip, String iSO_Code,
            String vendor_Catalog_Name, String model_Type) {

        this.Vendor_ID = vendor_ID;
        this.Vendor_Name = vendor_Name;
        this.AAD_Vendor_Lead_Time = aAD_Vendor_Lead_Time;
        this.AAD_Vendor_Cutoff_Time = aAD_Vendor_Cutoff_Time;
        this.AAD_Vendor_Override_Date = aAD_Vendor_Override_Date;
        this.AAD_Vendor_Outage_Start = aAD_Vendor_Outage_Start;
        this.AAD_Vendor_Outage_End = aAD_Vendor_Outage_End;
        this.Address_Line_1 = address_Line_1;
        this.Address_Line_2 = address_Line_2;
        this.City = city;
        this.State = state;
        this.Zip = zip;
        this.ISO_Code = iSO_Code;
        this.Vendor_Catalog_Name = vendor_Catalog_Name;
        this.Model_Type = model_Type;

    }

    public String getVendor_ID() {
        return Vendor_ID;
    }

    public String getVendor_Name() {
        return Vendor_Name;
    }

    public String getAAD_Vendor_Lead_Time() {
        return AAD_Vendor_Lead_Time;
    }

    public String getAAD_Vendor_Cutoff_Time() {
        return AAD_Vendor_Cutoff_Time;
    }

    public String getAAD_Vendor_Override_Date() {
        return AAD_Vendor_Override_Date;
    }

    public String getAAD_Vendor_Outage_Start() {
        return AAD_Vendor_Outage_Start;
    }

    public String getAAD_Vendor_Outage_End() {
        return AAD_Vendor_Outage_End;
    }

    public String getAddress_Line_1() {
        return Address_Line_1;
    }

    public String getAddress_Line_2() {
        return Address_Line_2;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getZip() {
        return Zip;
    }

    public String getISO_Code() {
        return ISO_Code;
    }

    public String getVendor_Catalog_Name() {
        return Vendor_Catalog_Name;
    }

    public String getModel_Type() {
        return Model_Type;
    }

    public String toString(){
        return "{\n" + "Vendor ID: " + Vendor_ID + "\n}";
    }

    public Item toItem() {
            Item item = new Item().withPrimaryKey("Vendor_ID", Vendor_ID)
                                .withString("Vendor_Name", Vendor_Name)
                                .withString("AAD_Vendor_Lead_Time", AAD_Vendor_Lead_Time)
                                .withString("AAD_Vendor_Cutoff_Time", AAD_Vendor_Cutoff_Time)
                                .withString("AAD_Vendor_Override_Date", AAD_Vendor_Override_Date)
                                .withString("AAD_Vendor_Outage_Start", AAD_Vendor_Outage_Start)
                                .withString("AAD_Vendor_Outage_End", AAD_Vendor_Outage_End)
                                .withString("Address_Line_1", Address_Line_1)
                                .withString("Address_Line_2", Address_Line_2)
                                .withString("City", City)
                                .withString("State", State)
                                .withString("Zip", Zip)
                                .withString("ISO_Code", ISO_Code)
                                .withString("Vendor_Catalog_Name", Vendor_Catalog_Name)
                                .withString("Model_Type", Model_Type);
            return item;
    }

}
