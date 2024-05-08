package com.example.recyclepro.activities;

import android.content.Intent;
import android.health.connect.datatypes.units.Length;
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
            Intent intent=new Intent(this, AssessmentCompletedSide.class);
            startActivity(intent);
        });
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v->{
            Intent intent=new Intent(this, SignUp.class);
            startActivity(intent);
        });
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v-> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean loggedIn = dynamoDBManager.loadUsers(email, password, new DynamoDBManager.LoadUsersCallback() {
                @Override
                public void onLoginSuccess(String role) {
                    if(role.equals("customer"))
                    {
                        Intent intent=new Intent(SignIn.this, CustomerSide.class);
                        startActivity(intent);
                    }else
                    {
                        Intent intent=new Intent(SignIn.this, RecyclingAssessorSide.class);
                        startActivity(intent);
                    }
                    Toast.makeText(SignIn.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLoginFailure() {
                    Toast.makeText(SignIn.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                }
            });
        });

    }
}