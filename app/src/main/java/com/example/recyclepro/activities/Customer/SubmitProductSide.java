package com.example.recyclepro.activities.Customer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SubmitProductSide extends AppCompatActivity {
    private EditText etCustomerName;
    private EditText etPhone;
    private EditText etProductName;
    private EditText etCase;
    private TextView etPurchasedDate;
    private EditText etBattery;
    private EditText etScreen;
    private Button btnSend;
    private ImageButton btnBack;
    private DynamoDBManager dynamoDBManager;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_product_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.submitProductSide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etCustomerName =findViewById(R.id.etCustomerName);
        etPhone=findViewById(R.id.etPhone);
        etProductName=findViewById(R.id.etProductName);
        etCase=findViewById(R.id.etCase);
        etPurchasedDate=findViewById(R.id.etPurchasedDate);
        etPurchasedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        etBattery=findViewById(R.id.etBattery);
        etScreen =findViewById(R.id.etScreen);
        btnSend=findViewById(R.id.btnSave);
        btnBack =findViewById(R.id.btnBack);
        dynamoDBManager=new DynamoDBManager(this);
        dynamoDBManager.checkDynamoDBConnection();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", ""); // Lấy email từ SharedPreferences

        btnSend.setOnClickListener(v -> {
            Random rand = new Random();
            int number = rand.nextInt(1000000);
            String id = String.format("%06d", number);
            String customerName = etCustomerName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String productName=etProductName.getText().toString().trim();
            String caseDescribe = etCase.getText().toString().trim();
            String purchasedDate = etPurchasedDate.getText().toString().trim();
            String battery = etBattery.getText().toString().trim();
            String screen = etScreen.getText().toString().trim();
            String currentTime=getCurrentDateTime();
            Log.d("check464","id"+id+"customerName"+customerName+"phone"+phone+"case"+caseDescribe+"purchasedDate"+
                    purchasedDate+"battery"+battery+"screen"+screen);
            dynamoDBManager.SubmitProductInformationforRecycling(id, customerName, phone, productName,caseDescribe,purchasedDate, battery,screen, "not assessed yet", email, currentTime);
            Toast.makeText(this, "Submit successful", Toast.LENGTH_LONG).show(); // Added show() method
        });
        btnBack.setOnClickListener(v->{
                // Nếu người dùng đồng ý, thực hiện chuyển đổi sang activity đăng nhập
                Intent intent = new Intent(this, CustomerMenuSide.class);
                startActivity(intent);
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
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        etPurchasedDate.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
}