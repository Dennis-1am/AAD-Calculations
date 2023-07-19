package com.signet.ActualArrivalDate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class AppTest {

    AmazonDynamoDB dybClient = DependencyFactory.dynamoDbClient("us-east-2");
    DynamoDB db = new DynamoDB(dybClient);

    public AppTest() {
        
    }

}
