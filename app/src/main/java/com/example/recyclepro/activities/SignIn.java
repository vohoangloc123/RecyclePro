package com.example.recyclepro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.Assessment.AssessmentMenuSide;
import com.example.recyclepro.activities.Customer.CustomerMenuSide;
import com.example.recyclepro.dynamoDB.DynamoDBManager;

public class SignIn extends AppCompatActivity {
    private Button btnSignInAsCustomer;
    private Button btnSignUp;
    private Button btnSignIn;
    private EditText etEmail;
    private EditText etPassword;
    private DynamoDBManager dynamoDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signIn), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        dynamoDBManager=new DynamoDBManager(this);
        btnSignInAsCustomer=findViewById(R.id.btnSignInForCustomer);
        btnSignInAsCustomer.setOnClickListener(v->{
            Intent intent=new Intent(this, AssessmentMenuSide.class);
            startActivity(intent);
        });
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v->{
            Intent intent=new Intent(this, SignUp.class);
            startActivity(intent);
        });
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                dynamoDBManager.loadUsers(email, password, new DynamoDBManager.LoadUsersCallback() {
                    @Override
                    public void onLoginSuccess(String role, String name) {
                        // Lưu thông tin người dùng vào SharedPreferences khi đăng nhập thành công
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("name", name);
                        editor.putString("role", role);
                        editor.apply();

                        // Chuyển qua Activity tiếp theo dựa trên vai trò của người dùng
                        Intent intent;
                        if (role.equals("customer")) {
                            intent = new Intent(SignIn.this, CustomerMenuSide.class);
                        } else {
                            intent = new Intent(SignIn.this, AssessmentMenuSide.class);
                        }
                        startActivity(intent);
                        Toast.makeText(SignIn.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLoginFailure() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Login failed, check your email and passwords", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }
}