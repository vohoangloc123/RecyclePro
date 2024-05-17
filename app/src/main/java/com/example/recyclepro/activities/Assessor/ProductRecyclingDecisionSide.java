package com.example.recyclepro.activities.Assessor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.adapter.ProductRecyclingDecisionAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.ProductRecyclingDecision;

import java.util.ArrayList;

public class ProductRecyclingDecisionSide extends AppCompatActivity {
    private RecyclerView rcvProductRecyclingDecision;
    private DynamoDBManager dynamoDBManager;
    private ProductRecyclingDecisionAdapter adapter;
    private ImageButton btnBack;
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
        btnBack=findViewById(R.id.btnBack);
        rcvProductRecyclingDecision=findViewById(R.id.rcvRecyclingDecision);
        dynamoDBManager=new DynamoDBManager(this);
        productRecyclingDecisionArrayList=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvProductRecyclingDecision.setLayoutManager(layoutManager);
        adapter= new ProductRecyclingDecisionAdapter(productRecyclingDecisionArrayList);
        rcvProductRecyclingDecision.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductRecyclingDecisionAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String id, String customerName, String productName, String email, String phone, String customerAddress, String branchAddress, String description, String time) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", id);
                editor.putString("customerName", customerName);
                editor.putString("productName", productName);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("customerAddress", customerAddress);
                editor.putString("branchAddress", branchAddress);
                editor.putString("description", description);
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
        btnBack.setOnClickListener(v->{
            Intent intent=new Intent(this, AssessmentMenuSide.class);
            startActivity(intent);
        });
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