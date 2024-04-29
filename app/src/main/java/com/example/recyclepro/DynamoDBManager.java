package com.example.recyclepro;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBManager {

    private Context context;
    public DynamoDBManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
    }
    public boolean checkDynamoDBConnection() {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }

            // Thực hiện công việc mạng trong một luồng mới
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ListTablesResult tables = ddbClient.listTables();
                        for (String tableName : tables.getTableNames()) {
                            Log.d("check464", "Table name: " + tableName);
                        }
                    } catch (Exception e) {
                        // Log exception for debugging
                        Log.e("check464", "Error checking DynamoDB connection: " + e.getMessage());
                    }
                }
            }).start();

            // Trả về true vì việc khởi động công việc mạng đã được bắt đầu
            return true;
        } catch (Exception e) {
            // Log exception for debugging
            Log.e("DynamoDBManager", "Error checking DynamoDB connection: " + e.getMessage());
            return false;
        }
    }
    private void initializeDynamoDB() {
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAZQ3DNSDFWIYOZ3WD", "I7d0DUQViEuX897VsULvlESCI8fb3zq7iTRxZHB/");

            ddbClient = new AmazonDynamoDBClient(credentials);
            ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1)); // Set the region

            ListTablesResult tables = ddbClient.listTables();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private AmazonDynamoDBClient ddbClient;
    public void SubmitProductInformationforRecycling(String productID, String phone,String productName, String caseDescribe, String purchasedDate, String battery, String describe, String state) {
        Log.d("Check464", "OnSubmitProduct");
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
                Log.d("Check464", "Connecting");
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một mục mới
                        Map<String, AttributeValue> item = new HashMap<>();
                        item.put("_id", new AttributeValue().withS(productID));
                        item.put("phone", new AttributeValue().withS(phone));
                        item.put("productName", new AttributeValue().withS(productName));
                        item.put("caseDescribe", new AttributeValue().withS(caseDescribe));
                        item.put("purchasedDate", new AttributeValue().withS(purchasedDate));
                        item.put("battery", new AttributeValue().withS(battery));
                        item.put("describe", new AttributeValue().withS(describe));
                        item.put("state", new AttributeValue().withS(state));
                        // Tạo yêu cầu chèn mục vào bảng
                        PutItemRequest putItemRequest = new PutItemRequest()
                                .withTableName("RecyclingProducts")
                                .withItem(item);

                        // Thực hiện yêu cầu chèn mục và nhận kết quả
                        PutItemResult result = ddbClient.putItem(putItemRequest);
                        Log.d("Check464", String.valueOf(result));

                    } catch (Exception e) {
                        Log.e("Error", "Exception occurred: ", e);
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            // Log exception for debugging
            Log.e("DynamoDBManager", "Error checking DynamoDB connection: " + e.getMessage());
        }
    }
}
