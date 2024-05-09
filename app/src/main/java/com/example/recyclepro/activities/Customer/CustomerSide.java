package com.example.recyclepro.activities.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.activities.SignIn;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.R;
import com.example.recyclepro.Validation.Regex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CustomerSide extends AppCompatActivity {
    private EditText etCustomerName;
    private EditText etPhone;
    private EditText etProductName;
    private EditText etCase;
    private EditText etPurchasedDate;
    private EditText etBattery;
    private EditText etScreen;
    private Button btnSend;
    private ImageButton btnExit;
    private DynamoDBManager dynamoDBManager;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customerSide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etCustomerName =findViewById(R.id.etCustomerName);
        etPhone=findViewById(R.id.etPhone);
        etProductName=findViewById(R.id.etProductName);
        etCase=findViewById(R.id.etCase);
        etPurchasedDate=findViewById(R.id.etPurchasedDate);
        etBattery=findViewById(R.id.etBattery);
        etScreen =findViewById(R.id.etScreen);
        btnSend=findViewById(R.id.btnSave);
        btnExit=findViewById(R.id.btnExit);
        dynamoDBManager=new DynamoDBManager(this);
        dynamoDBManager.checkDynamoDBConnection();
        Bundle bundle=getIntent().getExtras(); // Lấy Bundle từ Intent
        if(bundle != null) {
            email = bundle.getString("email"); // Sử dụng key "email" để lấy dữ liệu
            Log.d("CheckEmail", email);
        }

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
            Log.d("check464","id"+id+"customerName"+customerName+"phone"+phone+"case"+caseDescribe+"purchasedDate"+
                    purchasedDate+"battery"+battery+"screen"+screen);
            dynamoDBManager.SubmitProductInformationforRecycling(id, customerName, phone, productName,caseDescribe,purchasedDate, battery,screen, "not assessed yet", email, currentTime);
            Toast.makeText(this, "Submit successful", Toast.LENGTH_LONG).show(); // Added show() method
        });
        btnExit.setOnClickListener(v->{
                // Nếu người dùng đồng ý, thực hiện chuyển đổi sang activity đăng nhập
                Intent intent = new Intent(this, CustomerMenuSide.class);
                startActivity(intent);
        });
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
}