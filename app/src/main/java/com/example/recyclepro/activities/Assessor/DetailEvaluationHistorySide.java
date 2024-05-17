package com.example.recyclepro.activities.Assessor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.squareup.picasso.Picasso;

public class DetailEvaluationHistorySide extends AppCompatActivity {
    private TextView tvProductInformation, tvRatingInformation, tvConditionInformation, tvPriceAndTypeInformation;
    private String id;
    private EditText etPhone;
    private ImageButton btnBack;
    private ImageView frontOfDevice, backOfDevice;
    private DynamoDBManager dynamoDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_evaluation_history_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        frontOfDevice=findViewById(R.id.ivFrontOfDevice);
        backOfDevice=findViewById(R.id.ivBackOfDevice);
        tvProductInformation=findViewById(R.id.tvProductInformation);
        tvRatingInformation=findViewById(R.id.tvRatingInformation);
        tvConditionInformation=findViewById(R.id.tvInformationCondition);
        tvPriceAndTypeInformation=findViewById(R.id.tvPriceAndTypeInformation);
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent=new Intent(this, EvaluationHistorySide.class);
            startActivity(intent);
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString("id");
            String customerName = bundle.getString("customerName");
            String productName = bundle.getString("productName");
            double finalPrice = bundle.getDouble("finalPrice");
            String time = bundle.getString("time");
            double avgRating = bundle.getDouble("avgRating");
            String typeOfRecycle = bundle.getString("typeOfRecycle");
            String phone = bundle.getString("phone");
            String battery = bundle.getString("battery");
            String caseDescribe = bundle.getString("caseDescribe");
            String screen = bundle.getString("screen");
            String uptime = bundle.getString("uptime");
            String state = bundle.getString("state");
            String batteryCondition = bundle.getString("batteryCondition");
            String caseCondition = bundle.getString("caseCondition");
            String uptimeCondition = bundle.getString("uptimeCondition");
            String screenCondition = bundle.getString("screenCondition");
            double batteryRating = bundle.getDouble("batteryRating");
            double caseRating = bundle.getDouble("caseRating");
            double uptimeRating = bundle.getDouble("uptimeRating");
            double screenRating = bundle.getDouble("screenRating");
            String urlFrontOfDevice = bundle.getString("frontOfDevice");
            String urlBackOfDevice = bundle.getString("backOfDevice");
            if (urlFrontOfDevice != null) {
                Picasso.get()
                        .load(urlFrontOfDevice)
                        .into(frontOfDevice);
            }

            if (urlBackOfDevice != null) {
                Picasso.get()
                        .load(urlBackOfDevice)
                        .into(backOfDevice);
            }

            tvProductInformation.setText("Customer name:"+customerName+"\n"+
                    "Product name: "+productName+"\n"+
                    "Time: "+time+"\n");
            tvConditionInformation.setText("Battery: "+batteryCondition+"\n"+
                            "Case: "+caseCondition+"\n"+
                            "Up time: "+uptimeCondition+"\n"+
                            "Screen: "+screenCondition+"\n"
                    );
            tvRatingInformation.setText("Battery: "+batteryRating+"\n"+
                    "Case: "+caseRating+"\n"+
                    "Up time: "+uptimeRating+"\n"+
                    "Screen: "+screenRating+"\n"+
                    "Avg rating: "+avgRating+"\n"
            );
            tvPriceAndTypeInformation.setText( "Final price: "+finalPrice+"\n"+
                    "State: "+state+"\n"+
                    "Type of recycle: "+typeOfRecycle
                    );
        } else {
            Log.d("ReceivedData", "Bundle is null");
        }

    }

}