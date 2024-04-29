package com.example.recyclepro;

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

import com.example.recyclepro.Validation.Regex;

import java.util.Random;

public class CustomerSide extends AppCompatActivity {
    private EditText etName;
    private EditText etPhone;
    private EditText etProductName;
    private EditText etCase;
    private EditText etPurchasedDate;
    private EditText etBattery;
    private EditText etDescribe;
    private Button btnSend;
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
        etName=findViewById(R.id.etName);
        etPhone=findViewById(R.id.etPhone);
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
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String caseDescribe = etCase.getText().toString().trim();
            String purchasedDate = etPurchasedDate.getText().toString().trim();
            String battery = etBattery.getText().toString().trim();
            String describe = etDescribe.getText().toString().trim();

            Log.d("check464","id"+id+"name"+name+"phone"+phone+"case"+caseDescribe+"purchasedDate"+
                    purchasedDate+"battery"+battery+"describe"+describe);
            dynamoDBManager.SubmitProductInformationforRecycling(id, name, phone, caseDescribe, purchasedDate, battery, describe, "not yet rated");
            Toast.makeText(this, "Submit successful", Toast.LENGTH_LONG).show(); // Added show() method
        });
    }
}