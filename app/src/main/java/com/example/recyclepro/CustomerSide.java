package com.example.recyclepro;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomerSide extends AppCompatActivity {
    private EditText etName;
    private EditText etPhone;
    private EditText etProductName;
    private EditText etCase;
    private EditText etPurchasedDate;
    private EditText etBattery;
    private EditText etDescribe;
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

    }
}