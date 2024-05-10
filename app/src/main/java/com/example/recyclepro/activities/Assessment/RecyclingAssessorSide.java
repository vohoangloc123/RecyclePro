package com.example.recyclepro.activities.Assessment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.SignIn;
import com.example.recyclepro.adapter.ProductAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.Product;

import java.util.ArrayList;

public class RecyclingAssessorSide extends AppCompatActivity {
    private RecyclerView rcvCustomer;
    private ArrayList<Product> listProducts;
    private DynamoDBManager dynamoDBManager;
    private ProductAdapter adapter;
    private Button btnLoad;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycling_assessor_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recycleAssessorSide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rcvCustomer=findViewById(R.id.rcvCustomer);
        dynamoDBManager=new DynamoDBManager(this);
        listProducts=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCustomer.setLayoutManager(linearLayoutManager);
        adapter= new ProductAdapter(listProducts);
        rcvCustomer.setAdapter(adapter);
        listProducts.clear();
        dynamoDBManager.loadProduct("not assessed yet", new DynamoDBManager.LoadRecyclingProductListListener() {


            @Override
            public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time, String email) {
                Log.d("sequence506", "stage 2 in load"+id+customerName+phone+productName+battery+caseDescribe+purchasedDate+ screen);
                Product product=new Product(id,customerName,phone, productName, battery, caseDescribe,purchasedDate, screen, time, email);
                listProducts.add(product);
                adapter.notifyDataSetChanged();
            }
        });
        btnLoad=findViewById(R.id.btnReview);
        btnLoad.setOnClickListener(v->{
            listProducts.clear();
            dynamoDBManager.loadProduct("not assessed yet", new DynamoDBManager.LoadRecyclingProductListListener() {
                @Override
                public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time, String email) {
                    Log.d("sequence506", "stage 2 in load"+id+customerName+phone+productName+battery+caseDescribe+purchasedDate+ screen);
                    Product product=new Product(id,customerName,phone, productName, battery, caseDescribe,purchasedDate, screen, time, email);
                    listProducts.add(product);
                    adapter.notifyDataSetChanged();
                }
            });
        });
        btnBack =findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            Bundle bundle=getIntent().getExtras(); // Lấy Bundle từ Intent
            String email = bundle.getString("email"); // Sử dụng key "email" để lấy dữ liệu
            String name = bundle.getString("name");
            String role = bundle.getString("role");
            if(bundle != null) {
                bundle.putString("email", email);
                bundle.putString("name", name);
                bundle.putString("role", role);
                Intent intent = new Intent(this, AssessmentMenuSide.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String productID, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time, String email) {
                Bundle bundle = new Bundle();
                bundle.putString("productID", productID);
                bundle.putString("customerName", customerName);
                bundle.putString("phone", phone);
                bundle.putString("productName",productName);
                bundle.putString("productBattery", battery);
                bundle.putString("productCaseDescribe", caseDescribe);
                bundle.putString("productPurchasedDate", purchasedDate);
                bundle.putString("productScreen", screen);
                bundle.putString("time", time);
                bundle.putString("email", email);
                goTorecyclingAssessmentFragment(bundle);
            }

        });
    }
    public void goTorecyclingAssessmentFragment(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RecyclingAssessmentFragment recyclingAssessmentFragment = new RecyclingAssessmentFragment();
        // Thêm ChatHistoryFragment
        fragmentTransaction.add(R.id.recycleAssessorSide,recyclingAssessmentFragment, recyclingAssessmentFragment.TAG);
        recyclingAssessmentFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(recyclingAssessmentFragment.TAG);
        fragmentTransaction.commit();
    }
}