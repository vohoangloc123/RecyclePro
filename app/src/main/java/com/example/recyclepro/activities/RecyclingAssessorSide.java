package com.example.recyclepro.activities;

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
    private ImageButton btnExit;

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
            public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen) {
                Log.d("sequence506", "stage 2 in load"+id+customerName+phone+productName+battery+caseDescribe+purchasedDate+ screen);
                Product product=new Product(id,customerName,phone, productName, battery, caseDescribe,purchasedDate, screen);
                listProducts.add(product);
                adapter.notifyDataSetChanged();
            }
        });
        btnLoad=findViewById(R.id.btnReview);
        btnLoad.setOnClickListener(v->{
            listProducts.clear();
            dynamoDBManager.loadProduct("1", new DynamoDBManager.LoadRecyclingProductListListener() {


                @Override
                public void onFound(String id, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen) {
                    Log.d("sequence506", "stage 2 in load"+id+customerName+phone+productName+battery+caseDescribe+purchasedDate+ screen);
                    Product product=new Product(id,customerName,phone, productName, battery, caseDescribe,purchasedDate, screen);
                    listProducts.add(product);
                    adapter.notifyDataSetChanged();
                }
            });
        });
        btnExit =findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v->{
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Nếu người dùng đồng ý, thực hiện chuyển đổi sang activity đăng nhập
                Intent intent = new Intent(this, SignIn.class);
                startActivity(intent);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Nếu người dùng hủy bỏ, đóng dialog và không thực hiện hành động gì
                dialog.dismiss();
            });
            builder.show();
        });
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(String productID, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String describe) {
                Log.d("sequence506", "stage 3 in click event"+productID+customerName+phone+productName+battery+caseDescribe+purchasedDate+describe);
                Bundle bundle = new Bundle();
                bundle.putString("productID", productID);
                bundle.putString("customerName", customerName);
                bundle.putString("phone", phone);
                bundle.putString("productName",productName);
                bundle.putString("productBattery", battery);
                bundle.putString("productCaseDescribe", caseDescribe);
                bundle.putString("productPurchasedDate", purchasedDate);
                bundle.putString("productDescribe", describe);
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