package com.app.regform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    //const
    private static final int OTP_LENGTH = 2;

    //var
    SQLiteHelper dbHelper;
    String username, indicator;

    //widgets
    TextInputEditText username_ET;
    MaterialCardView login_CV, loginIndicator_CV;
    TextView loginIndicator_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_ET = findViewById(R.id.loginUsername_ET);
        login_CV = findViewById(R.id.login_CV);
        loginIndicator_CV = findViewById(R.id.loginIndicator_CV);
        loginIndicator_TV = findViewById(R.id.loginIndicator_TV);

        dbHelper = new SQLiteHelper(this);
    }

    static char[] OTP()
    {
        // Using numeric values
        String numbers = "0123";
        String values = "ABCD";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[OTP_LENGTH];

        // Use of charAt() method : to get character value
        // Use of nextInt() as it is scanning the value as int
        otp[0] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        otp[1] = values.charAt(rndm_method.nextInt(values.length()));
        return otp;
    }

    public void login(View view) {
        username = String.valueOf(username_ET.getText());
        if (username.equals("")){
            Toast.makeText(this,"Enter username to login", Toast.LENGTH_LONG).show();
        }else {
            if (dbHelper.checkUser(username)){
                showLoginIndicator();
            }else
                Toast.makeText(this,"User does not exist", Toast.LENGTH_LONG).show();
        }

    }

    private void showLoginIndicator() {
        indicator = new String(OTP());
        loginIndicator_TV.setText(indicator);

        login_CV.setVisibility(View.GONE);
        loginIndicator_CV.setVisibility(View.VISIBLE);
    }

    public void gotoPassMatrix(View view) {
        Intent intent = new Intent(LoginActivity.this,PassMatrixActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("indicator",indicator);
        startActivity(intent);
    }
}
