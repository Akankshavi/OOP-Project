package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private EditText userName, userPassword, userEmail, userPhone,userRePassword, userAge;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    public static Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    String user_name = userName.getText().toString().trim();
                    if (credentials.checkUsername(user_name)) {
                        Toast.makeText(Registration.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //sendEmailVerification();
                                    //sendUserData();
                                    firebaseAuth.signOut();
                                    Toast.makeText(Registration.this, "Successfully Registered, Upload complete!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    credentials.addCredentials(user_name, user_password);

                                    /* Store the credentials */
                                    sharedPreferencesEditor.putString(user_name, user_password);
                                    sharedPreferencesEditor.putString("LastSavedUsername", "");
                                    sharedPreferencesEditor.putString("LastSavedPassword", "");

                                    /* Commits the changes and adds them to the file */
                                    sharedPreferencesEditor.apply();



                                    startActivity(new Intent(Registration.this, MainActivity.class));
                                } else {
                                    Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }


                        });

                    }
                }
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
    private boolean validate(){
        boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString().trim();
        String email = userEmail.getText().toString();
        String repassword=userRePassword.getText().toString().trim();
        String phone=userPhone.getText().toString();
        //age = userAge.getText().toString();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || repassword.isEmpty() || phone.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();

        }else if(!password.equals(repassword)){
            Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
        }
        else{
            result= true;
        }
        return result;
    }

}