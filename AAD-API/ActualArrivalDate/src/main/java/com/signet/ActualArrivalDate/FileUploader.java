package com.signet.ActualArrivalDate;

import com.signet.ActualArrivalDate.models.Product;
import com.signet.ActualArrivalDate.models.Vendor;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class FileUploader {

    private static String csvFileLocation = "AAD-API/ActualArrivalDate/src/main/java/com/signet/ActualArrivalDate/dataset/";
    private static AmazonDynamoDB dybClient = DependencyFactory.dynamoDbClient("us-east-1");
    private static DynamoDB db = new DynamoDB(dybClient);

    public static void main(String[] args) throws CsvException {

        List<String> tableNames = new ArrayList<String>() {
            {
                add("Signet-Product-Table"); // 0
                add("Signet-Vendor-Table"); // 1
            }
        };

        System.out.println("Reading Files");
        List<Item> products = toProduct("SignetProductAAD");
        List<Item> vendors = toVendor("AADVendorData");

        System.out.println(products.size());

        System.out.println("Uploading to DB");

        threadedBatchUpload(products, tableNames.get(0), 40);
        threadedBatchUpload(vendors, tableNames.get(1), 40);

    }

    public static void threadedBatchUpload(List<Item> items, String tableName, int threadCount) {

        List<List<Item>> threadChunks = new ArrayList<List<Item>>();
        int chunkSize = items.size() / threadCount;

        for (int i = 0; i < items.size(); i += chunkSize) { // pre-processing the data into chunks for each thread to
                                                            // handle
            threadChunks.add(items.subList(i, Math.min(items.size(), i + chunkSize)));
        }

        for (List<Item> chunk : threadChunks) { // printing the size of each chunk
            System.out.println("Chunk Size: " + chunk.size());
        }

        List<Thread> threads = new ArrayList<Thread>(); // creating the threads

        for (List<Item> chunk : threadChunks) { // starting the threads and adding them to the list of threads
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(chunk, tableName, threadChunks.indexOf(chunk));
                }
            });
            threads.add(thread); // adding the thread to the list of threads
            thread.start(); // starting the thread to run the batchUpload operation on the chunk of data
        }

        for (Thread thread : threads) { // joining the threads to the main thread so that the main thread waits for all
                                        // the threads to finish
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread Interrupted");
            }
        }

    }

    public static void upload(List<Item> items, String tableName, int ThreadNum) {

        TableWriteItems table = new TableWriteItems(tableName);
        int uploaded = 0;
        int size = items.size();

        for (Item item : items) {
            table.withItemsToPut(item);
            db.batchWriteItem(table);
            System.out.println("Thread: " + ThreadNum + "\nUploading Chunk Progress: " + uploaded + "/" + size
                    + "\nPercentage Completed: " + (uploaded / size) * 100 + "%\n\n");
            uploaded++;
        }

    }

    public static void batchUpload(List<Item> items, String tableName, int ThreadNum) {

        List<List<Item>> chunks = batchSizing(items);
        TableWriteItems table = new TableWriteItems(tableName);

        int size = chunks.size();

        int uploaded = 0;
        for (List<Item> chunk : chunks) {
            table.withItemsToPut(chunk);
            db.batchWriteItem(table);
            uploaded++;
            System.out.println("Thread: " + ThreadNum + "\nUploading Chunk Progress: " + uploaded + "/" + size
                    + "\nPercentage Completed: " + (uploaded / size) * 100 + "%\n\n");
        }

        System.out.println("Upload Complete");

    }

    public static void batchUpload(List<Item> items, String tableName) {

        List<List<Item>> chunks = batchSizing(items);
        TableWriteItems table = new TableWriteItems(tableName);

        int size = chunks.size();

        int uploaded = 0;
        for (List<Item> chunk : chunks) {
            table.withItemsToPut(chunk);
            db.batchWriteItem(table);
            uploaded++;
            System.out.println("Uploading Chunk Progress: " + uploaded + "/" + size + "\nPercentage Completed: "
                    + (uploaded / size) * 100 + "%\n\n");
        }

        System.out.println("Upload Complete");

    }

    public static ArrayList<List<Item>> batchSizing(List<Item> items) { // for the batchupload operation into dynamoDB

        ArrayList<List<Item>> chunks = new ArrayList<List<Item>>();
        int chunkSize = 25;

        for (int i = 0; i < items.size(); i += chunkSize) {
            chunks.add(items.subList(i, Math.min(items.size(), i + chunkSize)));
        }

        return chunks;

    }

    public static List<Item> toVendor(String file) {
        List<Item> vendors = new ArrayList<Item>();
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFileLocation + file + ".csv"));
            String[] lines;

            int size = 0;
            while ((lines = reader.readNext()) != null) {
                Vendor vendor = new Vendor(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6],
                        lines[7], lines[8], lines[9], lines[10], lines[11], lines[12], lines[13], lines[14]);
                vendors.add(vendor.toItem());
                size++;
            }

            reader.close();

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return vendors;

    }

    public static List<Item> toProduct(String file) {
        List<Item> products = new ArrayList<Item>();
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFileLocation + file + ".csv"));
            String[] lines;
            while ((lines = reader.readNext()) != null) {

                Product prod = new Product(lines[0], lines[1], lines[2], lines[3]);
                products.add(prod.toItem());

            }
            reader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return products;
    }

}
