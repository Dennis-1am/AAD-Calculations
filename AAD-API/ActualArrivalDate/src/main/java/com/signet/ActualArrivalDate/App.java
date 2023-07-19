package com.signet.ActualArrivalDate;

import com.signet.ActualArrivalDate.models.*; // import all models from models package in ActualArrivalDate package

import java.util.Map;
import java.time.*; // import all classes from java.time package
import java.time.temporal.ChronoUnit; // import ChronoUnit class from java.time.temporal package to get number of days elapsed between two dates

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class App implements RequestHandler<Object, Object> {
    private AmazonDynamoDB dybClient;
    private DynamoDB db;
    private ObjectMapper objectMapper;

    public App() { // constructor for App class to initialize dybClient, db, and objectMapper these are initialized when the lambda function is invoked and the constructor is called by the lambda function

        dybClient = DependencyFactory.dynamoDbClient("us-east-1"); 
        db = new DynamoDB(dybClient);
        objectMapper = new ObjectMapper();

    }

    @Override
    public Object handleRequest(final Object req, final Context context) {

        context.getLogger().log("Input: " + req.toString() + "\n");

        /** 
         * 1. Convert request to json string
         * 2. Convert json string to Request object
         * 3. Query DB for item using the SKU
         * 4. If item is not found, return null return a response to user saying SKU DNE ~ Does Not Exist in DynamoDB
         * 5. If item is found, extract lead time from item if it exist else use the shipping method and vendor to calculate lead time
         * 6. Calculate Actual Arrival Date using lead time, shipping method, and current date
         * 7. Then create a response object and return it to the user with the Actual Arrival Date
         * 8. Convert response object to json string
         * 9. Return json string
        */

        String json = ""; 
        Response response = null; 
        String ActualArrivalDate = "";

        try {
            json = objectMapper.writeValueAsString(req); // convert request to json string≠
            
            Request request = objectMapper.readValue(json, Request.class); // convert json string to Request object

            String SKU = request.getSKU(); // extract SKU from request
            String Vendor_ID = request.getVendor(); // extract Vendor from request
            String Shipping_Method = request.getShipping_Method(); // extract Shipping Method from request

            int days = 0; // initialize days to 0 to calculate lead time

            int Product_LeadTime = 0; // initialize Product Lead Time to 0 to calculate lead time
            int Vendor_LeadTime = 0; // initialize Vendor Lead Time to 0 to calculate lead time
            String[] Vendor_Outage_Dates_Start = null; // initialize Vendor Outage to null to calculate lead time
            String[] Vendor_Outage_Dates_End = null; // initialize Vendor Outage to null to calculate lead time
            long Outage_LeadTime = 0; // initialize Outage Lead Time to 0 to calculate lead time
            int Shipping_Method_LeadTime = 0; // initialize Shipping Method Lead Time to 0 to calculate lead time


            if(SKU != null){
                GetItemSpec spec = new GetItemSpec().withPrimaryKey("SKU", SKU); // create GetItemSpec object to query DB
                Item item = queryDB(db, spec, "Signet-Product-Table"); // query DB for item

                try { // try to extract Product Lead Time from item if it exist catch exception if it does not exist
                    Product_LeadTime = Integer.parseInt(item.getString("AAD_SKU_LeadTime")); // extract Product Lead Time from item
                    context.getLogger().log("Product Lead Time: " + Product_LeadTime + "\n" ); // log Product Lead Time
                } catch (Exception e) {
                    Product_LeadTime = 0; // if Product Lead Time is not found, set it to 0
                }

            }

            if(Vendor_ID != null){
                GetItemSpec spec = new GetItemSpec().withPrimaryKey("Vendor_ID", Vendor_ID); // create GetItemSpec object to query DB
                Item item = queryDB(db, spec, "Signet-Vendor-Table"); // query DB for item

                try { // try to extract Vendor Lead Time from item if it exist catch exception if it does not exist

                    Vendor_LeadTime = Integer.parseInt(item.getString("AAD_Vendor_Lead_Time")); // extract Vendor Lead Time from item
                    Vendor_Outage_Dates_Start = item.getString("AAD_Vendor_Outage_Start").split(";"); // extract Vendor Outage Dates from item
                    Vendor_Outage_Dates_End = item.getString("AAD_Vendor_Outage_End").split(";"); // extract Vendor Outage Dates from item
                    context.getLogger().log("Vendor Lead Time: " + Vendor_LeadTime + "\n" ); // log Vendor Lead Time
                    context.getLogger().log("Vendor Outage Dates Start: " + Vendor_Outage_Dates_Start + "\n" ); // log Vendor Outage Dates
                    context.getLogger().log("Vendor Outage Dates End: " + Vendor_Outage_Dates_End + "\n" ); // log Vendor Outage Dates

                } catch (Exception e) {
                    Vendor_LeadTime = 0; // if Vendor Lead Time is not found, set it to 0
                }

            }
            
            if(Shipping_Method != null){
                Shipping_Method_LeadTime = new ShippingMethods().getLeadTime(Shipping_Method); // get Shipping Method Lead Time from ShippingMethods class
                context.getLogger().log("Shipping Method Lead Time: " + Shipping_Method_LeadTime + "\n" ); // log Shipping Method Lead Time
            }

            days = Product_LeadTime + Vendor_LeadTime + Shipping_Method_LeadTime; // calculate lead time

            // get current date and add lead time to it to get Actual Arrival Date check if it is a weekend if it is add 2 days to it and if it the vendor has a outage date add the number of days elapsed for the outage date to the Actual Arrival Date

            LocalDate date = LocalDate.now(); // get current date6j
            LocalDate ActualArrivalDateLocalDate = date; // add lead time to current date to get Actual Arrival Date

            context.getLogger().log("Vendor Outage Dates {\n");

            for(String s : Vendor_Outage_Dates_Start){
                context.getLogger().log("Vendor Outage Date Start: " + s + "\n"); // log Vendor Outage Date
            }

            for(String s : Vendor_Outage_Dates_End){
                context.getLogger().log("Vendor Outage Date End: " + s + "\n"); // log Vendor Outage Date
            }

            context.getLogger().log("}\n");

            // if the vendor has a outage date add the number of days elapsed for the outage date to the Actual Arrival Date

            if(Vendor_Outage_Dates_Start != null && Vendor_Outage_Dates_End != null){ // check if Vendor Outage Dates are not null // assumes that there is not overlap between Vendor Outage Dates Start and Vendor Outage Dates End

                for(int i = 0; i < Vendor_Outage_Dates_Start.length; i++){ // loop through Vendor Outage Dates

                    LocalDate Vendor_Outage_Date_Start = LocalDate.parse(Vendor_Outage_Dates_Start[i]); // convert Vendor Outage Date to LocalDate
                    LocalDate Vendor_Outage_Date_End = LocalDate.parse(Vendor_Outage_Dates_End[i]); // convert Vendor Outage Date to LocalDate

                    context.getLogger().log("Vendor Outage Date Start: " + Vendor_Outage_Date_Start + "\n"); // log Vendor Outage Date
                    context.getLogger().log("Vendor Outage Date End: " + Vendor_Outage_Date_End + "\n"); // log Vendor Outage Date

                    context.getLogger().log("ActualArrivalDateLocalDate.isAfter " + ActualArrivalDateLocalDate.isAfter(Vendor_Outage_Date_Start)+ "\n"); // log Actual Arrival Date
                    context.getLogger().log("ActualArrivalDateLocalDate.isBefore " + ActualArrivalDateLocalDate.isBefore(Vendor_Outage_Date_End)+ "\n"); // log Actual Arrival Date
                    context.getLogger().log("ActualArrivalDateLocalDate " + ActualArrivalDateLocalDate + "\n"); // log Actual Arrival Date

                    

                    if(ActualArrivalDateLocalDate.isAfter(Vendor_Outage_Date_Start) && ActualArrivalDateLocalDate.isBefore(Vendor_Outage_Date_End)){ // check if Actual Arrival Date is between Vendor Outage Dates

                        Outage_LeadTime = ActualArrivalDateLocalDate.until(Vendor_Outage_Date_End, ChronoUnit.DAYS); // get number of days elapsed for the outage date
                        context.getLogger().log("Made it to Calculating Outage Date "); // log Outage Lead Time
                        break;
                    }

                }
            }

            days += Outage_LeadTime; // add number of days elapsed for the outage date to the lead time

            ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(days); // add number of days elapsed for the outage date to the Actual Arrival Date

            DayOfWeek dayOfWeek = ActualArrivalDateLocalDate.getDayOfWeek(); // get day of week from Actual Arrival Date

            if(dayOfWeek == DayOfWeek.SATURDAY){ // if Actual Arrival Date is a Saturday add 2 days to it
                ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(2);
            }
            else if(dayOfWeek == DayOfWeek.SUNDAY){ // if Actual Arrival Date is a Sunday add 1 day to it
                ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(1);
            }


            ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(Outage_LeadTime); // add number of days elapsed for the outage date to the Actual Arrival Date

            context.getLogger().log("Outage Lead Time: " + Outage_LeadTime + "\n"); // log Outage Lead Time

            ActualArrivalDate = ActualArrivalDateLocalDate.toString(); // convert Actual Arrival Date to string

            Map<String, String> headers = Map.of("Content-Type", "application/json"); // create headers map for response object

            Map<String, Object> responseMap = Map.of("SKU", SKU,"Actual Arrival Date", ActualArrivalDate); // create response map for response object

            Item responseItem = Item.fromMap(responseMap); // create response item for response object
            String jsonResponse = responseItem.toJSON(); // convert response item to json string

            response = new Response(200, headers, ActualArrivalDate); // create response object

            context.getLogger().log("Response: " + response.toString() + "\n"); // log response

            return jsonResponse; // return json string


        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Item queryDB(DynamoDB db, GetItemSpec spec, String tableName) {
        try {
            Table table = db.getTable(tableName);
            return table.getItem(spec);
        } catch (Exception e) {
            System.err.println("Unable to read item");
            System.err.println(e.getMessage());
            return null;
        }
    }

}