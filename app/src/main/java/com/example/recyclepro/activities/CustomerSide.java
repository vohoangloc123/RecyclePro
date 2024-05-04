package com.example.recyclepro.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText etDescribe;
    private Button btnSend;
    private Button btnBack;
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
        etDescribe=findViewById(R.id.etDescribe);
        btnSend=findViewById(R.id.btnSave);
        dynamoDBManager=new DynamoDBManager(this);
        dynamoDBManager.checkDynamoDBConnection();

        Regex regex=new Regex();
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
            String describe = etDescribe.getText().toString().trim();

            Log.d("check464","id"+id+"customerName"+customerName+"phone"+phone+"case"+caseDescribe+"purchasedDate"+
                    purchasedDate+"battery"+battery+"describe"+describe);
            dynamoDBManager.SubmitProductInformationforRecycling(id, customerName, phone, productName,battery,caseDescribe,purchasedDate, describe, "1");
            Toast.makeText(this, "Submit successful", Toast.LENGTH_LONG).show(); // Added show() method
        });
    }
}