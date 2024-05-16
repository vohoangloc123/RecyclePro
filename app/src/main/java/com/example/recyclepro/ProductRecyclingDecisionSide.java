package com.example.recyclepro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.activities.Assessment.RecyclingAssessmentFragment;
import com.example.recyclepro.adapter.ProductAdapter;
import com.example.recyclepro.adapter.ProductRecyclingDecisionAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.Product;
import com.example.recyclepro.models.ProductRecyclingDecision;

import java.util.ArrayList;

public class ProductRecyclingDecisionSide extends AppCompatActivity {
    private RecyclerView rcvProductRecyclingDecision;
    private DynamoDBManager dynamoDBManager;
    private ProductRecyclingDecisionAdapter adapter;
    private ArrayList<ProductRecyclingDecision> productRecyclingDecisionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_recycling_decision_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rcvProductRecyclingDecision=findViewById(R.id.rcvRecyclingDecision);
        dynamoDBManager=new DynamoDBManager(this);
        productRecyclingDecisionArrayList=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvProductRecyclingDecision.setLayoutManager(layoutManager);
        adapter= new ProductRecyclingDecisionAdapter(productRecyclingDecisionArrayList);
        rcvProductRecyclingDecision.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductRecyclingDecisionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String productID, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("productID", productID);
                editor.putString("customerName", customerName);
                editor.putString("phone", phone);
                editor.putString("productName", productName);
                editor.putString("battery", battery);
                editor.putString("caseDescribe", caseDescribe);
                editor.putString("purchasedDate", purchasedDate);
                editor.putString("screen", screen);
                editor.putString("time", time);
                editor.apply();
                Intent intent=new Intent(ProductRecyclingDecisionSide.this, DetailProductRecyclingDecision.class);
                startActivity(intent);
            }
        });
        dynamoDBManager.loadProductRecyclingDecision(new DynamoDBManager.loadProductRecyclingDecisionListListener() {
            @Override
            public void onFound(String id, String customerName, String productName, String email, String phoneNumber, String customerAddress, String branchAddress, String description, String time) {
                ProductRecyclingDecision productRecyclingDecision = new ProductRecyclingDecision(id, customerName, productName, email, phoneNumber, customerAddress, branchAddress, description, time);
                productRecyclingDecisionArrayList.add(productRecyclingDecision);
                adapter.notifyDataSetChanged();
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
    public void goToDetailProductRecycleDecisionFragment(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RecyclingAssessmentFragment recyclingAssessmentFragment = new RecyclingAssessmentFragment();
        // Thêm ChatHistoryFragment
        fragmentTransaction.add(R.id.main,recyclingAssessmentFragment, recyclingAssessmentFragment.TAG);
        recyclingAssessmentFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(recyclingAssessmentFragment.TAG);
        fragmentTransaction.commit();
    }
}