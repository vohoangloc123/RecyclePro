package com.example.recyclepro.activities.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.dynamoDB.DynamoDBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailRecycleSubmissionHistorySide extends AppCompatActivity {
    private TextView tvProductInformation, tvRatingInformation, tvConditionInformation, tvPriceAndTypeInformation;
    private String id, productName;
    private EditText etPhone, etBrandAddress, etAddress, etDescription, etEmail;
    private ImageButton btnBack;
    private DynamoDBManager dynamoDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_recycle_submission_history_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dynamoDBManager=new DynamoDBManager(this);
        tvProductInformation=findViewById(R.id.tvProductInformation);
        tvRatingInformation=findViewById(R.id.tvRatingInformation);
        tvConditionInformation=findViewById(R.id.tvInformationCondition);
        tvPriceAndTypeInformation=findViewById(R.id.tvPriceAndTypeInformation);
        etAddress=findViewById(R.id.etAddress);
        etPhone=findViewById(R.id.etPhone);
        etDescription=findViewById(R.id.etDescription);
        etEmail=findViewById(R.id.etEmail);

        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent=new Intent(this, RecyclingSubmissionHistorySide.class);
            startActivity(intent);
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            String customerName = bundle.getString("customerName");
            productName = bundle.getString("productName");
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
            etPhone.setText(phone);
        } else {
            Log.d("ReceivedData", "Bundle is null");
        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", ""); // "" là giá trị mặc định nếu không tìm thấy dữ liệu
        String name = sharedPreferences.getString("name", "");
        etEmail.setText(email);
        etBrandAddress=findViewById(R.id.etBranchAddress);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.address_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý khi một mục được chọn
                String selectedAddress = parent.getItemAtPosition(position).toString();

                // Kiểm tra nếu địa chỉ được chọn là "Go Vap district"
                if (selectedAddress.equals("Go Vap district")) {
                    // Thiết lập text cho etBranch là "12 Nguyễn Văn Bảo, Gò Vấp"
                    etBrandAddress.setText("12 Nguyen Van Bao, ward 4, Go Vap district");
                }else if (selectedAddress.equals("District 1")) {
                    // Thiết lập text cho etBranch là "12 Nguyễn Văn Bảo, Gò Vấp"
                    etBrandAddress.setText("2 D. Hai Trieu, Ben Nghe, District 1");
                }else if (selectedAddress.equals("District 10")) {
                    // Thiết lập text cho etBranch là "12 Nguyễn Văn Bảo, Gò Vấp"
                    etBrandAddress.setText("240 D. February 3, Ward 12, District 10");
                }
                else if (selectedAddress.equals("Thu Duc district")) {
                    // Thiết lập text cho etBranch là "12 Nguyễn Văn Bảo, Gò Vấp"
                    etBrandAddress.setText("242 D. Pham Van Dong, Thu Duc district");
                }else if(selectedAddress.equals("Binh Thanh district"))
                {
                    etBrandAddress.setText("720A D. Dien Bien Phu, Vinhomes Tan Cang, Binh Thanh");
                }else
                {
                    etBrandAddress.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn (nếu cần)
            }
        });

        Button btnSendEmail=findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(v -> {
            String customerAddress=etAddress.getText().toString();
            String phoneNumber=etPhone.getText().toString();
            String brandAddress=etBrandAddress.getText().toString();
            String currentDateTime=getCurrentDateTime();
            String description=etDescription.getText().toString();
            if(email.isEmpty()||phoneNumber.isEmpty()||customerAddress.isEmpty()||brandAddress.isEmpty()||description.isEmpty())
            {
                Toast.makeText(this, "You have not entered enough information", Toast.LENGTH_SHORT).show();
            }else
            {
                dynamoDBManager.SubmitProductRecyclingDecision(id, productName, name ,email,phoneNumber,customerAddress, brandAddress, description,currentDateTime);
                Toast.makeText(this, "Your information has been sent to the branch", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this, RecyclingSubmissionHistorySide.class);
                startActivity(intent);
            }
        });

    }
    public static String getCurrentDateTime() {
        // Định dạng cho ngày tháng năm và giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Lấy thời gian hiện tại
        Date currentTime = new Date();
        // Định dạng thời gian hiện tại thành chuỗi
        String formattedDateTime = dateFormat.format(currentTime);
        return formattedDateTime;
    }
}