package com.example.recyclepro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailProductRecyclingDecision extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_product_recycling_decision);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String productID = sharedPreferences.getString("productID", "");
        String customerName = sharedPreferences.getString("customerName", "");
        String phone = sharedPreferences.getString("phone", "");
        String productName = sharedPreferences.getString("productName", "");
        String battery = sharedPreferences.getString("battery", "");
        String caseDescribe = sharedPreferences.getString("caseDescribe", "");
        String purchasedDate = sharedPreferences.getString("purchasedDate", "");
        String screen = sharedPreferences.getString("screen", "");
        String time = sharedPreferences.getString("time", "");
        String productInformation = "Product ID: " + productID + "\n"
                + "Customer Name: " + customerName + "\n"
                + "Phone: " + phone + "\n"
                + "Product Name: " + productName + "\n"
                + "Battery: " + battery + "\n"
                + "Case Description: " + caseDescribe + "\n"
                + "Purchased Date: " + purchasedDate + "\n"
                + "Screen: " + screen + "\n"
                + "Time: " + time;

        TextView tvInformation = findViewById(R.id.tvInformation);
        tvInformation.setText(productInformation);
    }
}