package com.example.recyclepro.activities.Assessor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;

public class DetailProductRecyclingDecision extends AppCompatActivity {
    private ImageButton btnBack;
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
        btnBack=findViewById(R.id.btnBack);
        // Retrieve the shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

        // Get the stored data
        String id = sharedPreferences.getString("id", "");
        String customerName = sharedPreferences.getString("customerName", "");
        String productName = sharedPreferences.getString("productName", "");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phone", "");
        String customerAddress = sharedPreferences.getString("customerAddress", "");
        String branchAddress = sharedPreferences.getString("branchAddress", "");
        String description = sharedPreferences.getString("description", "");
        String time = sharedPreferences.getString("time", "");

        // Combine all product information into one string
        String productInformation = "ID: " + id + "\n" +
                "Customer Name: " + customerName + "\n" +
                "Product Name: " + productName + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone + "\n" +
                "Customer Address: " + customerAddress + "\n" +
                "Branch Address: " + branchAddress + "\n" +
                "Description: " + description + "\n" +
                "Time: " + time;
        TextView tvInformation = findViewById(R.id.tvInformation);
        tvInformation.setText(productInformation);
        btnBack.setOnClickListener(v->{
            Intent intent=new Intent(this, ProductRecyclingDecisionSide.class);
            startActivity(intent);
        });
    }

}