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

    public App() { // constructor for App class to initialize dybClient, db, and objectMapper these
                   // are initialized when the lambda function is invoked and the constructor is
                   // called by the lambda function

        dybClient = DependencyFactory.dynamoDbClient("us-east-1");
        db = new DynamoDB(dybClient);
        objectMapper = new ObjectMapper();

    }

    @Override
    public Object handleRequest(final Object req, final Context context) {

        context.getLogger().log("Input: " + req.toString() + "\n");

        String json = "";
        Response response = null;
        String ActualArrivalDate = "";

        try {

            json = objectMapper.writeValueAsString(req);
            Request request = objectMapper.readValue(json, Request.class);

            String SKU = request.getSKU();
            String Vendor_ID = request.getVendor();
            String Shipping_Method = request.getShipping_Method();

            int days = 0; // needed to calculate lead time

            int Product_LeadTime = 0;
            int Vendor_LeadTime = 0;
            String[] Vendor_Outage_Dates_Start = null;
            String[] Vendor_Outage_Dates_End = null;
            String Shipping_Message = "";
            long Outage_LeadTime = 0;
            int Shipping_Method_LeadTime = 0;

            if (SKU != null) {
                GetItemSpec spec = new GetItemSpec().withPrimaryKey("SKU", SKU);
                Item item = queryDB(db, spec, "Signet-Product-Table");

                try {
                    Product_LeadTime = Integer.parseInt(item.getString("AAD_SKU_LeadTime"));
                    if(item.getString("AAD_SKU_Shipping_Message") != null){
                        Shipping_Message = item.getString("AAD_SKU_Shipping_Message");
                    }
                    context.getLogger().log("Product Lead Time: " + Product_LeadTime + "\n");
                } catch (Exception e) {
                    Product_LeadTime = 0;
                }

            }

            if (Vendor_ID != null) {
                GetItemSpec spec = new GetItemSpec().withPrimaryKey("Vendor_ID", Vendor_ID);
                Item item = queryDB(db, spec, "Signet-Vendor-Table");

                try {

                    Vendor_LeadTime = Integer.parseInt(item.getString("AAD_Vendor_Lead_Time"));
                    Vendor_Outage_Dates_Start = item.getString("AAD_Vendor_Outage_Start").split(";");
                    Vendor_Outage_Dates_End = item.getString("AAD_Vendor_Outage_End").split(";");

                    context.getLogger().log("Vendor Lead Time: " + Vendor_LeadTime + "\n");

                    context.getLogger().log("Vendor Outage Dates {\n");

                    for (String s : Vendor_Outage_Dates_Start) {
                        context.getLogger().log("Vendor Outage Date Start: " + s + "\n"); // log Vendor Outage Date Start
                    }

                    for (String s : Vendor_Outage_Dates_End) {
                        context.getLogger().log("Vendor Outage Date End: " + s + "\n"); // log Vendor Outage Date End
                    }

                    context.getLogger().log("}\n");

                } catch (Exception e) {
                    Vendor_LeadTime = 0; // if Vendor Lead Time is not found, set it to 0
                }

            }

            if (Shipping_Method != null) {
                Shipping_Method_LeadTime = new ShippingMethods().getLeadTime(Shipping_Method);
                context.getLogger().log("Shipping Method Lead Time: " + Shipping_Method_LeadTime + "\n"); 
            }

            days = Product_LeadTime + Vendor_LeadTime + Shipping_Method_LeadTime; // calculate lead time

            context.getLogger().log("Lead Time: " + days + "\n"); // log lead time

            LocalDate date = LocalDate.now(); // get current date
            LocalDate ActualArrivalDateLocalDate = date; // add lead time to current date to get Actual Arrival Date

            DayOfWeek dayOfWeek = ActualArrivalDateLocalDate.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY) { // if Actual Arrival Date is a Saturday add 2 days to it
                ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(2);
            } else if (dayOfWeek == DayOfWeek.SUNDAY) { // if Actual Arrival Date is a Sunday add 1 day to it
                ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(1);
            }

            // if the vendor has a outage date add the number of days elapsed for the outage
            // date to the Actual Arrival Date

            if (Vendor_Outage_Dates_Start != null && Vendor_Outage_Dates_End != null) { // check if Vendor Outage Dates
                                                                                        // are not null // assumes that
                                                                                        // there is not overlap between
                                                                                        // Vendor Outage Dates Start and
                                                                                        // Vendor Outage Dates End

                for (int i = 0; i < Vendor_Outage_Dates_Start.length; i++) { // loop through Vendor Outage Dates

                    LocalDate Vendor_Outage_Date_Start = LocalDate.parse(Vendor_Outage_Dates_Start[i]); 
                    LocalDate Vendor_Outage_Date_End = LocalDate.parse(Vendor_Outage_Dates_End[i]); 

                    if (ActualArrivalDateLocalDate.isAfter(Vendor_Outage_Date_Start)
                            && ActualArrivalDateLocalDate.isBefore(Vendor_Outage_Date_End)) { 

                        Outage_LeadTime = ActualArrivalDateLocalDate.until(Vendor_Outage_Date_End, ChronoUnit.DAYS);
                        context.getLogger().log("Made it to Calculating Outage Date "); // log Outage Lead Time
                        break;
                    }

                }
            }

            ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(days); // add number of days elapsed for
                                                                                    // the outage date to the Actual
                                                                                    // Arrival Date

            ActualArrivalDateLocalDate = ActualArrivalDateLocalDate.plusDays(Outage_LeadTime); // add number of days
                                                                                               // elapsed for the outage
                                                                                               // date to the Actual
                                                                                               // Arrival Date

            context.getLogger().log("Outage Lead Time: " + Outage_LeadTime + "\n"); // log Outage Lead Time

            ActualArrivalDate = ActualArrivalDateLocalDate.toString(); // convert Actual Arrival Date to string

            Map<String, String> headers = Map.of("Content-Type", "application/json"); // create headers map for response
                                                                                      // object

            Map<String, Object> responseMap = Map.of("SKU", SKU.toString(), "Actual Arrival Date", ActualArrivalDate.toString(), "Shipping Message", Shipping_Message.toString()); 
            
            Item responseItem = Item.fromMap(responseMap); // create response item for response object
            String jsonResponse = responseItem.toJSON(); // convert response item to json string

            response = new Response(200, headers, ActualArrivalDate); // create response object

            context.getLogger().log("Response: " + response.toString() + "\n"); // log response

            return jsonResponse; // return json string

        } catch (JsonProcessingException e) {
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