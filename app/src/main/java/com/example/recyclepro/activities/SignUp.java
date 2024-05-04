package com.example.recyclepro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.Validation.Regex;
import com.example.recyclepro.dynamoDB.DynamoDBManager;

public class SignUp extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepassword;
    private EditText etUserName;
    private Button btnSignUp;
    private DynamoDBManager dynamoDBManager;
    private Regex regex;

    private Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        regex=new Regex();
        dynamoDBManager=new DynamoDBManager(this);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etRepassword=findViewById(R.id.etRepassword);
        etUserName=findViewById(R.id.etUserName);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        });

        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String rePassword = etRepassword.getText().toString().trim();
            String userName = etUserName.getText().toString();

            if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty() || userName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(rePassword)) {
                Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                if (!Regex.checkEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!Regex.checkPass(password)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                } else if (!Regex.checkUserName(userName)) {
                    Toast.makeText(getApplicationContext(), "Tên người dùng bao gồm chữ", Toast.LENGTH_SHORT).show();
                } else {
                    if (dynamoDBManager.checkExistedAccount(email)) {
                        Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
                        dynamoDBManager.createAccount(email, password, userName, "customer");
                        Intent intent = new Intent(this, SignIn.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}