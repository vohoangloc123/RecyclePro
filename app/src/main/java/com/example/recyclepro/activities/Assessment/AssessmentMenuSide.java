package com.example.recyclepro.activities.Assessment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class AssessmentMenuSide extends AppCompatActivity {
    private Button btnWaitingProductList, btnEvaluationHistory,btnAnalyst;
    private TextView tvEmail, tvUserName, tvRole;
    private String email, name, role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assessment_menu_side);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle=getIntent().getExtras(); // Lấy Bundle từ Intent
        if(bundle != null) {
            email = bundle.getString("email"); // Sử dụng key "email" để lấy dữ liệu
            name = bundle.getString("name");
            role = bundle.getString("role");
        }
        tvEmail=findViewById(R.id.tvEmail);
        tvUserName=findViewById(R.id.tvUserName);
        tvRole=findViewById(R.id.tvRole);
        tvEmail.setText("Email: "+ email);
        tvUserName.setText("User: "+name);
        tvRole.setText("Role: "+ role);
        btnEvaluationHistory=findViewById(R.id.btnEvaluationHistory);
        btnWaitingProductList=findViewById(R.id.btnWaitingProductList);
        btnAnalyst=findViewById(R.id.btnAnalyst);
        btnWaitingProductList.setOnClickListener(v -> {
            Intent intent=new Intent(this, RecyclingAssessorSide.class);
            startActivity(intent);
        });
        btnEvaluationHistory.setOnClickListener(v -> {
            Intent intent=new Intent(this, AssessmentCompletedSide.class);
            startActivity(intent);
        });
        ImageButton btnExit=findViewById(R.id.btnExit);
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