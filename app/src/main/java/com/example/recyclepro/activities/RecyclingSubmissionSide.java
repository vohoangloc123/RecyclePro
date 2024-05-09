package com.example.recyclepro.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.adapter.ProductAdapter;
import com.example.recyclepro.adapter.RecyclingSubmissionAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.Product;
import com.example.recyclepro.models.RecyclingSubmission;

import java.util.ArrayList;

public class RecyclingSubmissionSide extends AppCompatActivity {
    private RecyclerView rcvRecyclingSubmission;
    private ArrayList<RecyclingSubmission> listRecyclingSubmission;
    private DynamoDBManager dynamoDBManager;
    private RecyclingSubmissionAdapter adapter;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycling_submission_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclingSubmissionSide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle=getIntent().getExtras(); // Lấy Bundle từ Intent
        if(bundle != null) {
            email = bundle.getString("email"); // Sử dụng key "email" để lấy dữ liệu
        }
        rcvRecyclingSubmission=findViewById(R.id.rcvRecyclingSubmission);
        dynamoDBManager=new DynamoDBManager(this);
        listRecyclingSubmission=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvRecyclingSubmission.setLayoutManager(linearLayoutManager);
        adapter= new RecyclingSubmissionAdapter(listRecyclingSubmission);
        rcvRecyclingSubmission.setAdapter(adapter);
        listRecyclingSubmission.clear();
        dynamoDBManager.loadRecyclingSubmission(email, new DynamoDBManager.LoadRecyclingSubmissionListListener() {
            @Override
            public void onFound(String id, String productName, String time, String state) {
                RecyclingSubmission recyclingSubmission=new RecyclingSubmission(id, productName, time, state);
                listRecyclingSubmission.add(recyclingSubmission);
                adapter.notifyDataSetChanged();

            }
        });
        Button btnLoad=findViewById(R.id.btnReload);
        btnLoad.setOnClickListener(v->{
            dynamoDBManager.loadRecyclingSubmission(email, new DynamoDBManager.LoadRecyclingSubmissionListListener() {
                @Override
                public void onFound(String id, String productName, String time, String state) {
                    listRecyclingSubmission.clear();
                    RecyclingSubmission recyclingSubmission=new RecyclingSubmission(id, productName, time, state);
                    listRecyclingSubmission.add(recyclingSubmission);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}