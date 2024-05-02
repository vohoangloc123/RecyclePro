package com.example.recyclepro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;

public class SignIn extends AppCompatActivity {
    private Button btnSignInAsCustomer;
    private Button btnSignUp;
    private Button btnSignIn;
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
        btnSignInAsCustomer=findViewById(R.id.btnSignInForCustomer);
        btnSignInAsCustomer.setOnClickListener(v->{
            Intent intent=new Intent(this, CustomerSide.class);
            startActivity(intent);
        });
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v->{
            Intent intent=new Intent(this, SignUp.class);
            startActivity(intent);
        });
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v->{
            Intent intent=new Intent(this, RecyclingAssessorSide.class);
            startActivity(intent);
        });

    }
}