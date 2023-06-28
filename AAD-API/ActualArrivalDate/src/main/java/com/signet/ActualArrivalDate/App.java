package com.signet.ActualArrivalDate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

/**
 * Lambda function entry point. You can change to use other pojo type or
 * implement
 * a different RequestHandler.
 *
 * @see <a
 *      href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda
 *      Java Handler</a> for more information
 */
public class App implements RequestHandler<Object, Object> {
    private AmazonDynamoDB dybClient;
    private DynamoDB db;
    private ObjectMapper objectMapper;

    public App() {

        dybClient = DependencyFactory.dynamoDbClient("us-east-1");
        db = new DynamoDB(dybClient);
        objectMapper = new ObjectMapper();

    }

    @Override
    public Object handleRequest(final Object req, final Context context) {
        context.getLogger().log("Input: " + req.toString() + "\n");

        String json = "";
        Response response = null;
        String ActualArrivalDate = "01/01/1900";

        try {

            json = objectMapper.writeValueAsString(req);

            Product request = objectMapper.readValue(json, Product.class);
            context.getLogger().log("Request: " + request.toString() + "\n");

            GetItemSpec spec = new GetItemSpec().withPrimaryKey("SKU", request.getSKU());
            Item item = queryDB(db, spec);

            if (item == null) {
                context.getLogger().log("Item not found" + "\n");
                return null;
            }

            Map<String, Object> itemMap = item.asMap();

            itemMap.put("Actual Arrival Date", ActualArrivalDate);

            String SKU = (String) itemMap.get("SKU");
            String AAD = (String) itemMap.get("Actual Arrival Date");

            Map<String, Object> responseMap = Map.of("SKU", SKU, "Actual Arrival Date", AAD);

            item = Item.fromMap(responseMap);

            context.getLogger().log("Item: " + item.toJSON() + "\n");
            json = item.toJSON();
            

            Map<String, String> headers = Map.of("Content-Type", "application/json");
            response = new Response(200, headers, json);

            json = objectMapper.writeValueAsString(response);

            context.getLogger().log("Response: " + json + "\n");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public Item queryDB(DynamoDB db, GetItemSpec spec) {
        try {
            Table table = db.getTable("Signet-Product-Table");
            return table.getItem(spec);
        } catch (Exception e) {
            System.err.println("Unable to read item");
            System.err.println(e.getMessage());
            return null;
        }
    }

}
