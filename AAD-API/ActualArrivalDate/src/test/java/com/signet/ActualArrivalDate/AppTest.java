package com.signet.ActualArrivalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class AppTest {

    AmazonDynamoDB dybClient = DependencyFactory.dynamoDbClient("us-east-2");
    DynamoDB db = new DynamoDB(dybClient);

    public AppTest() {
        
    }

    @Test
    public void queryDB() {
        App app = new App();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("SKU", "001001");
        Item item = app.queryDB(db, spec, "ActualArrivalDate");
        Assertions.assertEquals(item, "00100");
    }

}
