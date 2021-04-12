package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private EditText userName, userPassword, userEmail, userPhone,userRePassword, userAge;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;


    public Registration(EditText userName) {
        this.userName = userName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth = FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,MainActivity.class));
            }
        });
    }
    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userRePassword=(EditText)findViewById(R.id.etUserRePassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        userPhone =(EditText)findViewById(R.id.etUserPhone);
        //userAge = (EditText)findViewById(R.id.etAge);
        //userProfilePic = (ImageView)findViewById(R.id.ivProfile);
    }
    private Boolean validate(){
        Boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();
        String repassword=userRePassword.getText().toString();
        String phone=userPhone.getText().toString();
        //age = userAge.getText().toString();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || repassword.isEmpty() || phone.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show();

        }else if(password!=repassword){
            Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_LONG).show();
        }
        else{
            result=true;
        }

        return result;
    }

}