package com.example.recyclepro.dynamoDB;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void SubmitProductInformationforRecycling(String productID, String customerName,String phone,String productName, String caseDescribe, String purchasedDate, String battery, String screen, String state) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một mục mới
                        Map<String, AttributeValue> item = new HashMap<>();
                        item.put("_id", new AttributeValue().withS(productID));
                        item.put("customerName", new AttributeValue().withS(customerName));
                        item.put("phone", new AttributeValue().withS(phone));
                        item.put("productName", new AttributeValue().withS(productName));
                        item.put("caseDescribe", new AttributeValue().withS(caseDescribe));
                        item.put("purchasedDate", new AttributeValue().withS(purchasedDate));
                        item.put("battery", new AttributeValue().withS(battery));
                        item.put("screen", new AttributeValue().withS(screen));
                        item.put("state", new AttributeValue().withS(state));
                        // Tạo yêu cầu chèn mục vào bảng
                        PutItemRequest putItemRequest = new PutItemRequest()
                                .withTableName("RecyclingProducts")
                                .withItem(item);

                        // Thực hiện yêu cầu chèn mục và nhận kết quả
                        PutItemResult result = ddbClient.putItem(putItemRequest);

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
    public void loadProduct(String state, LoadRecyclingProductListListener listener) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                        Condition condition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(state));
                        scanFilter.put("state", condition);

                        ScanRequest scanRequest = new ScanRequest("RecyclingProducts").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Xử lý kết quả
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String id = item.get("_id").getS();
                            String customerName = item.get("customerName").getS();
                            String productName = item.get("productName").getS();
                            String battery=item.get("battery").getS();
                            String caseDescribe=item.get("caseDescribe").getS();
                            String screen=item.get("screen").getS();
                            String phone=item.get("phone").getS();
                            String purchasedDate=item.get("purchasedDate").getS();
                            // Cập nhật giao diện
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("sequence506", "stage 1 in dynamoDB"+id+customerName+phone+productName+battery+caseDescribe+purchasedDate+screen);
                                    listener.onFound(id, customerName,phone, productName,  battery, caseDescribe, purchasedDate,  screen);
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadRecyclingProductListListener {
        void onFound(String id,String customerName,String phone, String productName, String battery, String caseDescribe,String purchasedDate,String screen);
    }
    public void createAccount(String email, String password, String userName,String role) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một mục mới
                        Map<String, AttributeValue> item = new HashMap<>();
                        item.put("email", new AttributeValue().withS(email));
                        item.put("password", new AttributeValue().withS(password));
                        item.put("username", new AttributeValue().withS(userName));
                        item.put("role", new AttributeValue().withS(role));
                        // Tạo yêu cầu chèn mục vào bảng
                        PutItemRequest putItemRequest = new PutItemRequest()
                                .withTableName("Users")
                                .withItem(item);

                        // Thực hiện yêu cầu chèn mục và nhận kết quả
                        PutItemResult result = ddbClient.putItem(putItemRequest);

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
    public boolean checkExistedAccount(String email) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }

            // Tạo một biến boolean để lưu kết quả kiểm tra
            AtomicBoolean isExisted = new AtomicBoolean(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                        Condition condition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(email));
                        scanFilter.put("email", condition);

                        ScanRequest scanRequest = new ScanRequest("Users").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Kiểm tra xem có kết quả từ scan hay không
                        if (!scanResult.getItems().isEmpty()) {
                            // Nếu có, đánh dấu là email đã tồn tại và thoát khỏi thread
                            isExisted.set(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread

            // Chờ cho thread kết thúc
            Thread.sleep(1000); // Đợi 1 giây

            // Trả về kết quả kiểm tra
            return isExisted.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
    public interface LoadUsersCallback {
        void onLoginSuccess(String role);
        void onLoginFailure();
    }
    public boolean loadUsers(String email, String password, LoadUsersCallback callback) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }

            // Tạo một biến boolean để lưu kết quả kiểm tra
            AtomicBoolean isAuthenticated = new AtomicBoolean(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                        Condition condition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(email));
                        scanFilter.put("email", condition);

                        ScanRequest scanRequest = new ScanRequest("Users").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String storedPassword = item.get("password").getS(); // Lấy mật khẩu đã lưu
                            Log.d("Check464", storedPassword);
                            // Kiểm tra mật khẩu
                            if (storedPassword.equals(password)) {
                                // Mật khẩu đúng, đánh dấu xác thực thành công và thoát khỏi vòng lặp
                                String role = item.get("role").getS();
                                callback.onLoginSuccess(role);
                                isAuthenticated.set(true);
                                return;
                            } else {
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Thread.sleep(1000);


            return isAuthenticated.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
