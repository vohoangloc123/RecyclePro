package com.example.recyclepro.activities.Customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.RecyclingSubmissionSide;
import com.example.recyclepro.activities.SignIn;

public class CustomerMenuSide extends AppCompatActivity {
    private Button btnSubmitProduct,btnRecyclingSubmissionHistory;
    private String email;
    private ImageButton btnExit;
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
        Bundle bundle=getIntent().getExtras(); // Lấy Bundle từ Intent
        if(bundle != null) {
            email = bundle.getString("email"); // Sử dụng key "email" để lấy dữ liệu
        }
        btnSubmitProduct=findViewById(R.id.btnSubmitProduct);
        btnRecyclingSubmissionHistory=findViewById(R.id.btnRecyclingSubmissionHistory);
        btnSubmitProduct.setOnClickListener(v->{
            bundle.putString("email", email); // Thay "customerEmail" thành "email"
            Intent intent=new Intent(this, CustomerSide.class);
            intent.putExtras(bundle); // Sử dụng putExtras() để chuyển Bundle qua Intent
            startActivity(intent);
        });
        btnRecyclingSubmissionHistory.setOnClickListener(v->{
            bundle.putString("email", email); // Thay "customerEmail" thành "email"
            Intent intent=new Intent(this, RecyclingSubmissionSide.class);
            intent.putExtras(bundle); // Sử dụng putExtras() để chuyển Bundle qua Intent
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