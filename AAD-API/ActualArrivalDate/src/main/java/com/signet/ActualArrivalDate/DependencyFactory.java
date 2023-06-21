
package com.signet.ActualArrivalDate;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * The module containing all dependencies required by the {@link App}.
 */
public class DependencyFactory {

    private DependencyFactory() {}

    /**
     * @return an instance of AmazonDynamoDB
     */
    public static AmazonDynamoDB dynamoDbClient(String region) {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        builder.withCredentials(new EnvironmentVariableCredentialsProvider());
        builder.setRegion(region.toLowerCase());

        return builder.build();
    }

}
