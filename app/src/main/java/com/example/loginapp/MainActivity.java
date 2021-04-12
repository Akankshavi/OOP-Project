package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.Map;

import static com.example.loginapp.Registration.credentials;

public class MainActivity extends AppCompatActivity {
    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;
    private int counter = 3;
    boolean isValid = false;
    private TextView userRegistration;
    private CheckBox eRememberMe;
    private LoginButton facebook;
    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    private CallbackManager callbackManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eName = findViewById(R.id.etName);
        eLogin = findViewById(R.id.btnLogin);
        ePassword = findViewById(R.id.etPassword);
        eAttemptsInfo = findViewById(R.id.tvAttempts);
        userRegistration = (TextView)findViewById(R.id.tvRegister);
        eRememberMe = findViewById(R.id.cbRememberMe);
        credentials = new Credentials();
        facebook= findViewById(R.id.fb_login);
        callbackManager = CallbackManager.Factory.create();
        facebook.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(MainActivity.this, Dashboard.class));

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();




        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences != null) {
            Map<String, ?> preferencesMap = sharedPreferences.getAll();


            if(preferencesMap.size() != 0){
                credentials.loadCredentials(preferencesMap);

            }

            String savedUsername = sharedPreferences.getString("LastSavedUsername", "");
            String savedPassword = sharedPreferences.getString("LastSavedPassword", "");
            if(sharedPreferences.getBoolean("RememberMeCheckbox", false)){
                eName.setText(savedUsername);
                ePassword.setText(savedPassword);
                eRememberMe.setChecked(true);
            }
        }
        eRememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferencesEditor.putBoolean("RememberMeCheckbox", eRememberMe.isChecked());

                sharedPreferencesEditor.apply();
            }
        });

            eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Obtain user inputs */
                String userName = eName.getText().toString();
                String userPassword = ePassword.getText().toString();

                /* Check if the user inputs are empty */
                if (userName.isEmpty() || userPassword.isEmpty()) {
                    /* Display a message toast to user to enter the details */
                    Toast.makeText(MainActivity.this, "Please enter name and password!", Toast.LENGTH_LONG).show();

                } else {

                    /* Validate the user inputs */
                    isValid = validate(userName, userPassword);

                    /* Validate the user inputs */

                    /* If not valid */
                    if (!isValid) {

                        /* Decrement the counter */
                        counter--;

                        /* Show the attempts remaining */
                        eAttemptsInfo.setText("Attempts Remaining: " + String.valueOf(counter));

                        /* Disable the login button if there are no attempts left */
                        if (counter == 0) {
                            eLogin.setEnabled(false);
                            Toast.makeText(MainActivity.this, "You have used all your attempts try again later!", Toast.LENGTH_LONG).show();
                        }
                        /* Display error message */
                        else {
                            Toast.makeText(MainActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_LONG).show();

                        }
                    }
                    /* If valid */
                    else {

                        /* Allow the user in to your app by going into the next activity */
                        startActivity(new Intent(MainActivity.this, Dashboard.class));

                    }
                }

            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Registration.class));
            }
        });

    }
    private boolean validate(String userName, String userPassword)
    {
        return credentials.checkCredentials(userName,userPassword);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void checkLoginStatus(){
        if(AccessToken.getCurrentAccessToken()!=null)
            Toast.makeText(MainActivity.this, "You're already signed in with facebook", Toast.LENGTH_SHORT).show();
    }


}
