package com.example.recyclepro.activities.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.SignIn;

public class CustomerMenuSide extends AppCompatActivity {
    private Button btnSubmitProduct,btnRecyclingSubmissionHistory, btnAnalyst;
    private String email, name, role;
    private ImageButton btnExit;
    private TextView tvEmail, tvUserName, tvRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_menu_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", ""); // "" là giá trị mặc định nếu không tìm thấy dữ liệu
        String name = sharedPreferences.getString("name", "");
        String role = sharedPreferences.getString("role", "");
        tvEmail=findViewById(R.id.tvEmail);
        tvUserName=findViewById(R.id.tvUserName);
        tvRole=findViewById(R.id.tvRole);
        tvEmail.setText("Email: "+ email);
        tvUserName.setText("User: "+name);
        tvRole.setText("Role: "+ role);
        btnSubmitProduct=findViewById(R.id.btnSubmitProduct);
        btnRecyclingSubmissionHistory=findViewById(R.id.btnRecyclingSubmissionHistory);
        btnSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email", ""); // Lấy email từ SharedPreferences

                Intent intent = new Intent(CustomerMenuSide.this, SubmitProductSide.class);
                startActivity(intent);
            }
        });
        btnRecyclingSubmissionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email", ""); // Lấy email từ SharedPreferences
                Intent intent = new Intent(CustomerMenuSide.this, RecyclingSubmissionHistorySide.class);
                startActivity(intent);
            }
        });
        btnAnalyst=findViewById(R.id.btnAnalyst);
        btnAnalyst.setOnClickListener(v->{
            Intent intent = new Intent(CustomerMenuSide.this, CustomerAnalysis.class);
            startActivity(intent);
        });
        btnExit=findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v->{

            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Nếu người dùng đồng ý, thực hiện chuyển đổi sang activity đăng nhập
                Intent intent=new Intent(this, SignIn.class);

                startActivity(intent);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Nếu người dùng hủy bỏ, đóng dialog và không thực hiện hành động gì
                dialog.dismiss();
            });
            builder.show();
        });
    }
}