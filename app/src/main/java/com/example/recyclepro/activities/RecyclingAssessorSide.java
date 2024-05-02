package com.example.recyclepro.activities;

import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
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
        dynamoDBManager.loadProduct("1", new DynamoDBManager.LoadRecyclingProductListListener() {
            @Override
            public void onFound1(String id, String customerName, String productName) {
                Product product=new Product(id,customerName, productName);
                listProducts.add(product);
                adapter.notifyDataSetChanged();
            }
        });
        btnLoad=findViewById(R.id.btnReview);
        btnLoad.setOnClickListener(v->{
            listProducts.clear();
            dynamoDBManager.loadProduct("1", new DynamoDBManager.LoadRecyclingProductListListener() {
                @Override
                public void onFound1(String id, String customerName, String productName) {
                    Product product=new Product(id,customerName, productName);
                    listProducts.add(product);
                    adapter.notifyDataSetChanged();
                }
            });
        });
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{

        });
    }
}