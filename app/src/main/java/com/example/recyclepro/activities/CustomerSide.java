package com.example.recyclepro.activities;

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

import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.R;
import com.example.recyclepro.Validation.Regex;

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

            Log.d("check464","id"+id+"customerName"+customerName+"phone"+phone+"case"+caseDescribe+"purchasedDate"+
                    purchasedDate+"battery"+battery+"screen"+screen);
            dynamoDBManager.SubmitProductInformationforRecycling(id, customerName, phone, productName,caseDescribe,purchasedDate, battery,screen, "1");
            Toast.makeText(this, "Submit successful", Toast.LENGTH_LONG).show(); // Added show() method
        });
        btnExit.setOnClickListener(v->{
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Nếu người dùng đồng ý, thực hiện chuyển đổi sang activity đăng nhập
                Intent intent = new Intent(this, SignIn.class);
                startActivity(intent);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Nếu người dùng hủy bỏ, đóng dialog và không thực hiện hành động gì
                dialog.dismiss();
            });
            builder.show();

        });
    }
}