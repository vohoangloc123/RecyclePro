package com.example.recyclepro.models;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    public void SubmitProductInformationforRecycling(String productID, String customerName,String phone,String productName, String caseDescribe, String purchasedDate, String battery, String screen, String state, String email, String time, String frontOfDevice, String backOfDevice) {
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
                        item.put("email", new AttributeValue().withS(email));
                        item.put("time", new AttributeValue().withS(time));
                        item.put("frontOfDevice", new AttributeValue().withS(frontOfDevice));
                        item.put("backOfDevice", new AttributeValue().withS(backOfDevice));
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
                            String time=item.get("time").getS();
                            String email=item.get("email").getS();
                            // Cập nhật giao diện
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("sequence506", "stage 1 in dynamoDB"+id+customerName+phone+productName+battery+caseDescribe+purchasedDate+screen);
                                    listener.onFound(id, customerName,phone, productName,  battery, caseDescribe, purchasedDate,  screen, time, email);
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
        void onFound(String id,String customerName,String phone, String productName, String battery, String caseDescribe,String purchasedDate,String screen, String time, String email);
    }
    public void loadImagesOfProduct(String id, LoadImagesOfProductListener listener) {
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
                                .withAttributeValueList(new AttributeValue().withS(id));
                        scanFilter.put("_id", condition);

                        ScanRequest scanRequest = new ScanRequest("RecyclingProducts").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Xử lý kết quả
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String id = item.get("_id").getS();
                            String frontOfDevice=item.get("frontOfDevice").getS();
                            String backOfDevice=item.get("backOfDevice").getS();
                            // Cập nhật giao diện
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onFound(id, frontOfDevice, backOfDevice);
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
    public interface LoadImagesOfProductListener {
        void onFound(String id, String frontOfDevice, String backOfDevice);
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
        void onLoginSuccess(String role, String name);
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
                                String name=item.get("username").getS();
                                callback.onLoginSuccess(role, name);
                                isAuthenticated.set(true);
                                return;
                            } else {
                                callback.onLoginFailure();
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
    public void SubmitProductPrice(String reviewID, String productID, String customerName, String phone, String productName,
                                   String caseDescribe, String uptime, String battery,
                                   String screen, String state,
                                   String batteryRating, String batteryCondition,
                                   String caseRating, String caseCondition,
                                   String uptimeRating, String uptimeCondition,
                                   String screenRating, String screenCondition,
                                   String finalPrice, String time, String avgRating, String email,
                                   String typeOfRecycle) {
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
                                        item.put("_id", new AttributeValue().withS(reviewID));
                                        //product
                                        Map<String, AttributeValue> product = new HashMap<>();
                                        product.put("_id", new AttributeValue().withS(productID));
                                        product.put("battery", new AttributeValue().withS(battery));
                                        product.put("caseDescribe", new AttributeValue().withS(caseDescribe));
                                        product.put("customerName", new AttributeValue().withS(customerName));
                                        product.put("phone", new AttributeValue().withS(phone));
                                        product.put("productName", new AttributeValue().withS(productName));
                                        product.put("uptime", new AttributeValue().withS(uptime));
                                        product.put("screen", new AttributeValue().withS(screen));
                                        product.put("state", new AttributeValue().withS(state));
                                        item.put("product", new AttributeValue().withM(product));
                                        //battery
                                        Map<String, AttributeValue> battery= new HashMap<>();
                                        battery.put("batteryRating", new AttributeValue().withN(batteryRating));
                                        battery.put("batteryCondition", new AttributeValue().withS(batteryCondition));
                                        item.put("battery", new AttributeValue().withM(battery));
                                        //case
                                        Map<String, AttributeValue> deviceCase= new HashMap<>();
                                        deviceCase.put("caseRating", new AttributeValue().withN(caseRating));
                                        deviceCase.put("caseCondition", new AttributeValue().withS(caseCondition));
                                        item.put("case", new AttributeValue().withM(deviceCase));
                                        //uptime
                                        Map<String, AttributeValue> uptime= new HashMap<>();
                                        uptime.put("uptimeRating", new AttributeValue().withN(uptimeRating));
                                        uptime.put("uptimeCondition", new AttributeValue().withS(uptimeCondition));
                                        item.put("uptime", new AttributeValue().withM(uptime));
                                        //screen
                                        Map<String, AttributeValue> screen= new HashMap<>();
                                        screen.put("screenRating", new AttributeValue().withN(screenRating));
                                        screen.put("screenCondition", new AttributeValue().withS(screenCondition));
                                        item.put("screen", new AttributeValue().withM(screen));
                                        //price
                                        item.put("finalPrice", new AttributeValue().withN(finalPrice));
                                        //time
                                        item.put("time", new AttributeValue().withS(time));
                                        //avgRating
                                        item.put("avgRating", new AttributeValue().withN(avgRating));
                                        //email
                                        item.put("email", new AttributeValue().withS(email));
                                        //typeOfRecycle
                                        item.put("typeOfRecycle", new AttributeValue().withS(typeOfRecycle));
                                        // Tạo yêu cầu chèn mục vào bảng
                                        PutItemRequest putItemRequest = new PutItemRequest()
                                                .withTableName("ProductPrices")
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
    public void loadAssessmentCompleted(LoadAssessmentCompletedListListener listener) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("CheckAssessmentCompleted", "RUN");
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                        // Thêm các điều kiện scan vào để truy vấn dữ liệu
                        Condition condition = new Condition().withComparisonOperator(ComparisonOperator.NOT_NULL);
                        scanFilter.put("_id", condition); // Đảm bảo "_id" tồn tại
                        ScanRequest scanRequest = new ScanRequest("ProductPrices").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Xử lý kết quả
                        List<Map<String, AttributeValue>> items = scanResult.getItems();
                        for (Map<String, AttributeValue> item : items) {
                            // Lấy giá trị các trường trong item
                            String id = item.get("_id").getS();
                            String time = item.get("time").getS();
                            Double finalPrice = Double.valueOf(item.get("finalPrice").getN());
                            Double avgRating = Double.valueOf(item.get("avgRating").getN());
                            Map<String, AttributeValue> productMap = item.get("product").getM();
                            // Lấy giá trị các trường trong productMap
                            String productId = productMap.get("_id").getS();
                            String customerName = productMap.get("customerName").getS();
                            String phone = productMap.get("phone").getS();
                            String productName = productMap.get("productName").getS();
                            String typeOfRecycle=item.get("typeOfRecycle").getS();

                            // Xử lý các trường khác tương tự
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override

                                public void run() {

                                    if(listener != null) {
                                        listener.onLoadCompleted(id,customerName, productName, finalPrice, time, avgRating, typeOfRecycle);
                                        Log.d("CheckAssessmentCompleted", typeOfRecycle);
                                    }
                                }
                            });
                            // Do something with the retrieved data

                        }

                        // Gọi listener để thông báo đã load xong

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface LoadAssessmentCompletedListListener {
        void onLoadCompleted(String id, String customerName,String productName, double finalPrice, String time, double avgRating, String typeOfRecycle);
    }
    public void updateStateOfProduct(String productID,String state) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một mục mới
                        Map<String, AttributeValue> key = new HashMap<>();
                        key.put("_id", new AttributeValue().withS(productID));

                        Map<String, AttributeValueUpdate> updates = new HashMap<>();
                        updates.put("state", new AttributeValueUpdate().withValue(new AttributeValue().withS(state)).withAction(AttributeAction.PUT));
                        // Tạo yêu cầu cập nhật
                        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                                .withTableName("RecyclingProducts")
                                .withKey(key)
                                .withAttributeUpdates(updates);

                        // Thực hiện yêu cầu cập nhật và nhận kết quả
                        UpdateItemResult result = ddbClient.updateItem(updateItemRequest);

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
    public void loadRecyclingSubmission(String email, LoadRecyclingSubmissionListListener listener) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Log.d("PayAttention", "run"+email);
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                        Condition condition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(email));
                        scanFilter.put("email", condition);
                        ScanRequest scanRequest = new ScanRequest("RecyclingProducts").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Xử lý kết quả
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String id = item.get("_id").getS();
                            String productName = item.get("productName").getS();
                            String time = item.get("time").getS();
                            String state = item.get("state").getS();
                            // Cập nhật giao diện
                            Handler handler = new Handler(Looper.getMainLooper());
                            Log.d("PayAttention", id+productName+time+state);
                            handler.post(new Runnable() {
                                @Override

                                public void run() {
                                  listener.onFound(id, productName,time,state);
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
    public interface LoadRecyclingSubmissionListListener {
        void onFound(String id,String productName,String time, String state);
    }
    public void loadAProductPrices(String id, LoadAProductPriceListener listener) {
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("CheckAssessmentCompleted", "RUN"+id);
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                        Condition condition = new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ.toString())
                                .withAttributeValueList(new AttributeValue().withS(id));
                        scanFilter.put("_id", condition);
                        ScanRequest scanRequest = new ScanRequest("ProductPrices").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Xử lý kết quả
                        List<Map<String, AttributeValue>> items = scanResult.getItems();
                        if (items.isEmpty()) {
                            // Nếu không tìm thấy sản phẩm, gọi listener để thông báo
                            Log.d("khong tim thay", "yes");
                            if (listener != null) {
                                listener.onNotFound("This product still not assessed yet");
                            }
                        } else {
                            for (Map<String, AttributeValue> item : items) {
                                // Lấy giá trị các trường trong item
                                String id = item.get("_id").getS();
                                String time = item.get("time").getS();
                                Double finalPrice = Double.valueOf(item.get("finalPrice").getN());
                                Double avgRating = Double.valueOf(item.get("avgRating").getN());
                                String typeOfRecycle = item.get("typeOfRecycle").getS();
                                // Lấy giá trị các trường trong productMap
                                Map<String, AttributeValue> productMap = item.get("product").getM();
                                String productId = productMap.get("_id").getS();
                                String customerName = productMap.get("customerName").getS();
                                String phone = productMap.get("phone").getS();
                                String productName = productMap.get("productName").getS();
                                String battery = productMap.get("battery").getS();
                                String caseDescribe = productMap.get("caseDescribe").getS();
                                String screen = productMap.get("screen").getS();
                                String state = productMap.get("state").getS();
                                String uptime = productMap.get("uptime").getS();

                                // Lấy giá trị các trường trong screenMap
                                Map<String, AttributeValue> screenMap = item.get("screen").getM();
                                String screenCondition = screenMap.get("screenCondition").getS();
                                Double screenRating = Double.valueOf(screenMap.get("screenRating").getN());

                                // Lấy giá trị các trường trong uptimeMap
                                Map<String, AttributeValue> uptimeMap = item.get("uptime").getM();
                                String uptimeCondition = uptimeMap.get("uptimeCondition").getS();
                                Double uptimeRating = Double.valueOf(uptimeMap.get("uptimeRating").getN());

                                // Lấy giá trị các trường trong caseMap
                                Map<String, AttributeValue> caseMap = item.get("case").getM();
                                String caseCondition = caseMap.get("caseCondition").getS();
                                Double caseRating = Double.valueOf(caseMap.get("caseRating").getN());

                                // Lấy giá trị các trường trong batteryMap
                                Map<String, AttributeValue> batteryMap = item.get("battery").getM();
                                String batteryCondition = batteryMap.get("batteryCondition").getS();
                                Double batteryRating = Double.valueOf(batteryMap.get("batteryRating").getN());
                                // Gọi listener để thông báo đã load xong
                                if (listener != null) {
                                    listener.onLoadCompleted(id, customerName, productName, finalPrice, time, avgRating, typeOfRecycle,
                                            phone, battery, caseDescribe, screen, uptime, state,
                                            batteryCondition, caseCondition, uptimeCondition, screenCondition,
                                            batteryRating, caseRating, uptimeRating, screenRating
                                    );
                                }
                            }
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

    public interface LoadAProductPriceListener {
        void onLoadCompleted(String id, String customerName,String productName, double finalPrice, String time, double avgRating, String typeOfRecycle,
                             String phone, String battery, String caseDescribe, String screen, String uptime,String state,
                             String batteryCondition, String caseCondition, String uptimeCondition, String screenCondition,
                             Double batteryRating,Double caseRating, Double uptimeRating,  Double screenRating);
        void onNotFound(String error);
    }
    //Customer analysis
    public void calculatePercentageDeviceStatus(String email, CalculatePercentageDeviceStatusListener listener) {
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
                                .withAttributeValueList(new AttributeValue().withS(email));
                        scanFilter.put("email", condition);

                        ScanRequest scanRequest = new ScanRequest("RecyclingProducts").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Đếm số lượng trạng thái thiết bị
                        int totalCount = 0;
                        int notAssessedCount = 0;
                        int assessedCount = 0;

                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            totalCount++;
                            String state = item.get("state").getS();
                            if (state.equals("not assessed yet")) {
                                notAssessedCount++;
                            } else {
                                assessedCount++;
                            }
                        }

                        // Tính phần trăm
                        double notAssessedPercentage = (totalCount > 0) ? (notAssessedCount * 100.0 / totalCount) : 0;
                        double assessedPercentage = (totalCount > 0) ? (assessedCount * 100.0 / totalCount) : 0;

                        // Cập nhật giao diện
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onPercentageCalculated(notAssessedPercentage, assessedPercentage);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface CalculatePercentageDeviceStatusListener {
        void onPercentageCalculated(double notAssessedPercentage, double assessedPercentage);
    }
    public void NumberOfReviewOverTimeByMonth(NumberOfReviewOverTimeByMonthListener listener) {
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
                        ScanRequest scanRequest = new ScanRequest("RecyclingProducts").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Sử dụng TreeMap để sắp xếp các cặp key-value theo thứ tự của key
                        TreeMap<String, Integer> reviewCountMap = new TreeMap<>();

                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String time = item.get("time").getS(); // Lấy thời gian
                            String[] parts = time.split(" "); // Tách ngày và giờ
                            String[] dateParts = parts[0].split("/"); // Tách ngày tháng năm
                            String monthYear = dateParts[1] + "/" + dateParts[2]; // Lấy tháng và năm
                            // Kiểm tra xem reviewCountMap đã chứa key này chưa
                            if (reviewCountMap.containsKey(monthYear)) {
                                int count = reviewCountMap.get(monthYear);
                                reviewCountMap.put(monthYear, count + 1);
                            } else {
                                reviewCountMap.put(monthYear, 1);
                            }
                        }

                        // Cập nhật giao diện
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFound(reviewCountMap);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface NumberOfReviewOverTimeByMonthListener {
        void onFound(TreeMap<String, Integer> reviewCountMap);
    }
    //Assessment analysis
    public void AverageRatingOfEachTypeOfReview(AverageRatingOfEachTypeOfReviewListener listener) {
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
                        ScanRequest scanRequest = new ScanRequest("ProductPrices").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Biến để tính tổng rating và số lượng sản phẩm cho mỗi loại
                        int batteryTotalRating = 0;
                        int batteryCount = 0;
                        int caseTotalRating = 0;
                        int caseCount = 0;
                        int screenTotalRating = 0;
                        int screenCount = 0;
                        int uptimeTotalRating = 0;
                        int uptimeCount = 0;

                        // Lặp qua các sản phẩm để tính tổng rating
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            // Tính tổng rating của từng loại
                            if (item.containsKey("battery")) {
                                Map<String, AttributeValue> battery = item.get("battery").getM();
                                if (battery.containsKey("batteryRating")) {
                                    batteryTotalRating += Integer.parseInt(battery.get("batteryRating").getN());
                                    batteryCount++;
                                }
                            }
                            if (item.containsKey("case")) {
                                Map<String, AttributeValue> caseItem = item.get("case").getM();
                                if (caseItem.containsKey("caseRating")) {
                                    caseTotalRating += Integer.parseInt(caseItem.get("caseRating").getN());
                                    caseCount++;
                                }
                            }
                            if (item.containsKey("screen")) {
                                Map<String, AttributeValue> screen = item.get("screen").getM();
                                if (screen.containsKey("screenRating")) {
                                    screenTotalRating += Integer.parseInt(screen.get("screenRating").getN());
                                    screenCount++;
                                }
                            }
                            if (item.containsKey("uptime")) {
                                Map<String, AttributeValue> uptime = item.get("uptime").getM();
                                if (uptime.containsKey("uptimeRating")) {
                                    uptimeTotalRating += Integer.parseInt(uptime.get("uptimeRating").getN());
                                    uptimeCount++;
                                }
                            }
                        }

                        // Tính trung bình rating cho từng loại
                        double avgBatteryRating = batteryCount > 0 ? (double) batteryTotalRating / batteryCount : 0;
                        double avgCaseRating = caseCount > 0 ? (double) caseTotalRating / caseCount : 0;
                        double avgScreenRating = screenCount > 0 ? (double) screenTotalRating / screenCount : 0;
                        double avgUptimeRating = uptimeCount > 0 ? (double) uptimeTotalRating / uptimeCount : 0;

                        // Cập nhật giao diện
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFound(avgBatteryRating, avgCaseRating, avgScreenRating, avgUptimeRating);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AverageRatingOfEachTypeOfReviewListener {
        void onFound(double avgBatteryRating, double avgCaseRating, double avgScreenRating, double avgUptimeRating);
    }
    public void CalculatePercentageTypeOfRecycle(CalculatePercentageTypeOfRecycleListener listener) {
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

                        ScanRequest scanRequest = new ScanRequest("ProductPrices").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Đếm số lượng trạng thái thiết bị
                        int totalCount = 0;
                        int resaleCount = 0;
                        int recycleCount = 0;

                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            totalCount++;
                            String state = item.get("typeOfRecycle").getS();
                            if (state.equals("Resale")) {
                                resaleCount++;
                            } else {
                                recycleCount++;
                            }
                        }

                        // Tính phần trăm
                        double resale = (totalCount > 0) ? (resaleCount * 100.0 / totalCount) : 0;
                        double recycle= (totalCount > 0) ? (recycleCount * 100.0 / totalCount) : 0;

                        // Cập nhật giao diện
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onPercentageCalculated(resale, recycle);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface CalculatePercentageTypeOfRecycleListener {
        void onPercentageCalculated(double resale, double recycle);
    }
    public void NumberOfHaveEvaluatedOverTimeByMonth(NumberOfHaveEvaluatedOverTimeByMonthListener listener) {
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
                        ScanRequest scanRequest = new ScanRequest("ProductPrices").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Sử dụng TreeMap để sắp xếp các cặp key-value theo thứ tự của key
                        TreeMap<String, Integer> reviewCountMap = new TreeMap<>();

                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String time = item.get("time").getS(); // Lấy thời gian
                            String[] parts = time.split(" "); // Tách ngày và giờ
                            String[] dateParts = parts[0].split("/"); // Tách ngày tháng năm
                            String monthYear = dateParts[1] + "/" + dateParts[2]; // Lấy tháng và năm
                            // Kiểm tra xem reviewCountMap đã chứa key này chưa
                            if (reviewCountMap.containsKey(monthYear)) {
                                int count = reviewCountMap.get(monthYear);
                                reviewCountMap.put(monthYear, count + 1);
                            } else {
                                reviewCountMap.put(monthYear, 1);
                            }
                        }

                        // Cập nhật giao diện
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFound(reviewCountMap);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface NumberOfHaveEvaluatedOverTimeByMonthListener {
        void onFound(TreeMap<String, Integer> reviewCountMap);
    }
    public void FinalPriceDistribution(FinalPriceDistributionListener listener) {
        Log.d("CheckBoxPlot","Run");
        try {
            if (ddbClient == null) {
                initializeDynamoDB();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tạo một yêu cầu truy vấn
                        HashMap<String, Condition> scanFilter = new HashMap<>();
                        ScanRequest scanRequest = new ScanRequest("ProductPrices").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);
                        Log.d("CheckBoxPlot",scanResult.toString());
                        List<Float> finalPrices = new ArrayList<>();

                        // Trích xuất finalPrice từ mỗi mục
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            AttributeValue value = item.get("finalPrice");
                            if (value != null) {
                                finalPrices.add(Float.parseFloat(value.getN()));
                            }
                        }

                        // Cập nhật giao diện
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("CheckBoxPlot", finalPrices.toString());
                                listener.onFound(finalPrices);
                            }
                        });

                    } catch (Exception e) {
                        // Xử lý ngoại lệ khi truy vấn dữ liệu
                        e.printStackTrace();
                    }
                }
            }).start(); // Khởi chạy thread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface FinalPriceDistributionListener {
        void onFound(List<Float> finalPrices);
    }
    //product price
    public void loadProductPrice(loadProductPriceListener listener) {
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

                        ScanRequest scanRequest = new ScanRequest("Products").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);

                        // Xử lý kết quả
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String id = item.get("_id").getS();
                            String productName = item.get("productName").getS();
                            Double price = Double.valueOf(item.get("msrp").getN());
                            String brand=item.get("brand").getS();
                            // Cập nhật giao diện
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                        listener.onFound(id, productName, price, brand);
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

    public interface loadProductPriceListener {
        void onFound(String id, String productName, Double price, String brandName);
    }
    public void SubmitProductRecyclingDecision(String productID,String customerName, String productName,String  email ,String phoneNumber, String customerAddress, String branchAddress, String description, String time, String status) {
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
                        item.put("productName", new AttributeValue().withS(productName));
                        item.put("email", new AttributeValue().withS(email));
                        item.put("phoneNumber", new AttributeValue().withS(phoneNumber));
                        item.put("customerAddress", new AttributeValue().withS(customerAddress));
                        item.put("branchAddress", new AttributeValue().withS(branchAddress));
                        item.put("description", new AttributeValue().withS(description));
                        item.put("time", new AttributeValue().withS(time));
                        item.put("status", new AttributeValue().withS(status));
                        // Tạo yêu cầu chèn mục vào bảng
                        PutItemRequest putItemRequest = new PutItemRequest()
                                .withTableName("ProductRecyclingDecisions")
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
    public void loadProductRecyclingDecision(loadProductRecyclingDecisionListListener listener) {
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
                        ScanRequest scanRequest = new ScanRequest("ProductRecyclingDecisions").withScanFilter(scanFilter);
                        ScanResult scanResult = ddbClient.scan(scanRequest);
                        // Xử lý kết quả
                        for (Map<String, AttributeValue> item : scanResult.getItems()) {
                            String id = item.get("_id").getS();
                            String customerName=item.get("customerName").getS();
                            String productName=item.get("productName").getS();
                            String email=item.get("email").getS();
                            String phoneNumber = item.get("phoneNumber").getS();
                            String customerAddress = item.get("customerAddress").getS();
                            String branchAddress = item.get("branchAddress").getS();
                            String description = item.get("description").getS();
                            String time = item.get("time").getS();
                            String status = item.get("status").getS();
                            // Cập nhật giao diện
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onFound(id, customerName, productName, email,phoneNumber, customerAddress,branchAddress, description, time, status);
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

    public interface loadProductRecyclingDecisionListListener {
        void onFound(String id, String customerName, String productName, String email, String phoneNumber, String customerAddress, String branchAddress, String description, String time, String status);
    }
}
