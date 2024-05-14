package com.example.recyclepro.activities.Assessment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.LiveData.MyViewModel;
import com.example.recyclepro.adapter.ProductAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.Product;
import androidx.lifecycle.ViewModelProvider;
import com.example.recyclepro.activities.LiveData.MyViewModel;
import java.util.ArrayList;

public class WaitingProductListSide extends AppCompatActivity {
    private RecyclerView rcvCustomer;
    private ArrayList<Product> listProducts;
    private DynamoDBManager dynamoDBManager;
    private ProductAdapter adapter;
    private Button btnLoad;
    private ImageButton btnBack;
    private MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting_product_list_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
        loadData();
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        viewModel.getData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newData) {
                Log.d("Detach", "onChanged: Yes");
                listProducts.clear();
                dynamoDBManager.loadProduct("not assessed yet", new DynamoDBManager.LoadRecyclingProductListListener() {
                    @Override
                    public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time, String email) {
                        Log.d("Detach", "onChanged: inside load product");
                        Product product = new Product(id, customerName, phone, productName, battery, caseDescribe, purchasedDate, screen, time, email);
                        listProducts.add(product);
                        Log.d("Detach", listProducts.toString());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        btnLoad=findViewById(R.id.btnReview);
        btnLoad.setOnClickListener(v->{
            listProducts.clear();
            dynamoDBManager.loadProduct("not assessed yet", new DynamoDBManager.LoadRecyclingProductListListener() {
                @Override
                public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time, String email) {
                            Log.d("Detach", "onChanged: inside load product");
                            Product product = new Product(id, customerName, phone, productName, battery, caseDescribe, purchasedDate, screen, time, email);
                            listProducts.add(product);
                            Log.d("Detach", listProducts.toString());
                            adapter.notifyDataSetChanged();
                        }
                    });
        });
        btnBack =findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
                Intent intent = new Intent(this, AssessmentMenuSide.class);
                startActivity(intent);
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
        fragmentTransaction.add(R.id.main,recyclingAssessmentFragment, recyclingAssessmentFragment.TAG);
        recyclingAssessmentFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(recyclingAssessmentFragment.TAG);
        fragmentTransaction.commit();
    }

    private void loadData() {
        Log.d("Detach", "onChanged: Loaded data");
        listProducts.clear();
        dynamoDBManager.loadProduct("not assessed yet", new DynamoDBManager.LoadRecyclingProductListListener() {
            @Override
            public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time, String email) {
                        Log.d("Detach", "onChanged: inside load product");
                        Product product = new Product(id, customerName, phone, productName, battery, caseDescribe, purchasedDate, screen, time, email);
                        listProducts.add(product);
                        Log.d("Detach", listProducts.toString());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
}