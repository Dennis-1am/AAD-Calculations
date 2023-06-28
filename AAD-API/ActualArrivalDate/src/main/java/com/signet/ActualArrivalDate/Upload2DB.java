package com.signet.ActualArrivalDate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Upload2DB {

    public static void main(String[] args) throws CsvException {

        AmazonDynamoDB dybClient = DependencyFactory.dynamoDbClient("us-east-1");
        DynamoDB db = new DynamoDB(dybClient);

        String tableName = "Signet-Product-Table";
        String csvFile = "AAD-API/ActualArrivalDate/src/main/datasets/AAD_Product_SkuData-26062023020601.csv";

        List<Product> products = new ArrayList<Product>();

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] lines;

            System.out.println("Reading Lines");

            while((lines = reader.readNext()) != null) {
                
                String[] line = lines[0].split("\\|");

                if(lines[0].split("\\|").length == 1){
                    Product product = new Product(line[0], "", "", "");
                    products.add(product);
                } else {
                    Product product = new Product(line[0], line[1], "", "");
                    products.add(product);
                }
            }
            reader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        for (Product product : products) {
           db.getTable(tableName).putItem(product.toItem());
        }

        System.out.println("Products: " + products.size());

    }
    
}
