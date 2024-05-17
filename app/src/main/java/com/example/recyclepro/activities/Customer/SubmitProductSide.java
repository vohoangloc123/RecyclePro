package com.example.recyclepro.activities.Customer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class SubmitProductSide extends AppCompatActivity {
    private EditText etCustomerName;
    private EditText etPhone;
    private EditText etProductName;
    private EditText etCase;
    private Button etPurchasedDate;
    private EditText etBattery;
    private EditText etScreen;
    private Button btnSend;
    private ImageButton btnBack, btnBackOfDevice, btnFrontOfDevice;
    private DynamoDBManager dynamoDBManager;
    private String email;
    private String urlFrontOfDevice;
    private String urlBackOfDevice;
    private AmazonS3 s3Client;
    private PutObjectRequest request;
    private static final int PICK_IMAGE_REQUEST_FRONT = 1;
    private static final int PICK_IMAGE_REQUEST_BACK = 2;
    private static final String BUCKET_NAME = "recyclepro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_product_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.submitProductSide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAZQ3DNSDFWIYOZ3WD", "I7d0DUQViEuX897VsULvlESCI8fb3zq7iTRxZHB/");
        s3Client = new AmazonS3Client(credentials);
        s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        etCustomerName =findViewById(R.id.etCustomerName);
        etPhone=findViewById(R.id.etPhone);
        etProductName=findViewById(R.id.etProductName);
        etCase=findViewById(R.id.etCase);
        etPurchasedDate=findViewById(R.id.etPurchasedDate);
        etPurchasedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        etBattery=findViewById(R.id.etBattery);
        etScreen =findViewById(R.id.etScreen);
        btnSend=findViewById(R.id.btnSave);
        btnBack =findViewById(R.id.btnBack);
        btnFrontOfDevice=findViewById(R.id.btnFrontOfDevice);
        btnBackOfDevice=findViewById(R.id.btnBackOfDevice);
        dynamoDBManager=new DynamoDBManager(this);
        dynamoDBManager.checkDynamoDBConnection();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", ""); // Lấy email từ SharedPreferences
        urlFrontOfDevice = "https://recyclepro.s3.ap-southeast-1.amazonaws.com/mobile_screen.png";
        urlBackOfDevice = "https://recyclepro.s3.ap-southeast-1.amazonaws.com/mobile_back.png";
        String currentDate=getCurrentDate();
        etPurchasedDate.setText(currentDate);
        btnSend.setOnClickListener(v -> {
            Random rand = new Random();
            int number = rand.nextInt(1000000);
            String id = String.format("%06d", number);
            String customerName = etCustomerName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String productName=etProductName.getText().toString().trim();
            String caseDescribe = etCase.getText().toString().trim();
            String purchasedDate = etPurchasedDate.getText().toString().trim();
            String battery = etBattery.getText().toString().trim();
            String screen = etScreen.getText().toString().trim();
            String currentTime=getCurrentDateTime();
            if(id.isEmpty()||customerName.isEmpty()||phone.isEmpty()||productName.isEmpty()||caseDescribe.isEmpty()||purchasedDate.isEmpty()
            ||battery.isEmpty()||screen.isEmpty()||currentTime.isEmpty())
            {
                Toast.makeText(this, "You have not entered enough data", Toast.LENGTH_LONG).show(); // Added show() method
            }
            else
            {
                Log.d("CheckingPictures",urlBackOfDevice.toString()+urlFrontOfDevice.toString());
                dynamoDBManager.SubmitProductInformationforRecycling(id, customerName, phone, productName,caseDescribe,purchasedDate, battery,screen, "not assessed yet", email, currentTime, urlFrontOfDevice, urlBackOfDevice);
                Toast.makeText(this, "Submit successful", Toast.LENGTH_LONG).show(); // Added show() method
                Intent intent=new Intent(this, CustomerMenuSide.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(v->{
                // Nếu người dùng đồng ý, thực hiện chuyển đổi sang activity đăng nhập
                Intent intent = new Intent(this, CustomerMenuSide.class);
                startActivity(intent);
        });
        btnFrontOfDevice.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_FRONT);
        });

        btnBackOfDevice.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_BACK);
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                if (requestCode == PICK_IMAGE_REQUEST_FRONT) {
                    btnFrontOfDevice.setImageBitmap(bitmap);
                    uploadImageToS3(uri, "front");
                } else if (requestCode == PICK_IMAGE_REQUEST_BACK) {
                    btnBackOfDevice.setImageBitmap(bitmap);
                    uploadImageToS3(uri, "back");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToS3(Uri uri, String type) {
        new Thread(() -> {
            try {
                Log.d("IsImage", "OnUploadImage");
                InputStream inputStream = getContentResolver().openInputStream(uri);

                if (inputStream != null) {
                    String fileName = generateFileName();
                    List<PartETag> partETags = new ArrayList<>();
                    final int MB = 1024 * 1024;
                    final long partSize = 5 * MB;

                    InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest(BUCKET_NAME, fileName + ".jpg");
                    InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initiateRequest);

                    byte[] buffer = new byte[(int) partSize];
                    int bytesRead;
                    int partNumber = 1;

                    try {
                        while ((bytesRead = inputStream.read(buffer)) > 0) {
                            UploadPartRequest uploadRequest = new UploadPartRequest()
                                    .withBucketName(BUCKET_NAME)
                                    .withKey(fileName + ".jpg")
                                    .withUploadId(initResponse.getUploadId())
                                    .withPartNumber(partNumber++)
                                    .withInputStream(new ByteArrayInputStream(buffer, 0, bytesRead))
                                    .withPartSize(bytesRead);

                            UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
                            partETags.add(new PartETag(uploadResult.getPartNumber(), uploadResult.getETag()));
                        }

                        CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
                                BUCKET_NAME, fileName + ".jpg", initResponse.getUploadId(), partETags);
                        s3Client.completeMultipartUpload(completeRequest);

                        inputStream.close();

                        String imageUrl = "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName + ".jpg";
                        if (type.equals("front")) {
                            urlFrontOfDevice = imageUrl;
                        } else if (type.equals("back")) {
                            urlBackOfDevice = imageUrl;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("UploadImageToS3", "Error uploading image to S3: " + e.getMessage());
                        s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(BUCKET_NAME, fileName + ".jpg", initResponse.getUploadId()));
                    }
                } else {
                    Log.e("UploadImageToS3", "InputStream is null");
                }
            } catch (IOException | AmazonClientException e) {
                e.printStackTrace();
                Log.e("UploadImageToS3", "Error uploading image to S3: " + e.getMessage());
            }
        }).start();
    }

    private String generateFileName() {
        // Lấy ngày giờ hiện tại
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        // Tạo dãy số random
        int randomNumber = new Random().nextInt(10000);

        // Kết hợp ngày giờ và dãy số random để tạo tên file
        return "picture_" + timeStamp + "_" + randomNumber + ".jpg";
    }
    public static String getCurrentDateTime() {
        // Định dạng cho ngày tháng năm và giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Lấy thời gian hiện tại
        Date currentTime = new Date();
        // Định dạng thời gian hiện tại thành chuỗi
        String formattedDateTime = dateFormat.format(currentTime);
        return formattedDateTime;
    }
    public static String getCurrentDate() {
        // Định dạng cho ngày tháng năm và giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Lấy thời gian hiện tại
        Date currentTime = new Date();
        // Định dạng thời gian hiện tại thành chuỗi
        String formattedDateTime = dateFormat.format(currentTime);
        return formattedDateTime;
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        etPurchasedDate.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
}