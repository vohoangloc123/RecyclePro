package com.example.recyclepro.activities.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.adapter.RecyclingSubmissionAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.RecyclingSubmission;

import java.util.ArrayList;

public class RecyclingSubmissionHistorySide extends AppCompatActivity {
    private RecyclerView rcvRecyclingSubmission;
    private ArrayList<RecyclingSubmission> listRecyclingSubmission;
    private DynamoDBManager dynamoDBManager;
    private RecyclingSubmissionAdapter adapter;
    private String email;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycling_submission_history_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclingSubmissionHistorySide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", ""); // Lấy email từ SharedPreferences

        rcvRecyclingSubmission=findViewById(R.id.rcvRecyclingSubmission);
        dynamoDBManager=new DynamoDBManager(this);
        listRecyclingSubmission=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvRecyclingSubmission.setLayoutManager(linearLayoutManager);
        adapter= new RecyclingSubmissionAdapter(listRecyclingSubmission, dynamoDBManager, getApplicationContext());
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
            listRecyclingSubmission.clear();
            dynamoDBManager.loadRecyclingSubmission(email, new DynamoDBManager.LoadRecyclingSubmissionListListener() {
                @Override
                public void onFound(String id, String productName, String time, String state) {

                    RecyclingSubmission recyclingSubmission=new RecyclingSubmission(id, productName, time, state);
                    listRecyclingSubmission.add(recyclingSubmission);
                    adapter.notifyDataSetChanged();
                }
            });
        });
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            Intent intent = new Intent(RecyclingSubmissionHistorySide.this, CustomerMenuSide.class);
            startActivity(intent);
        });
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}