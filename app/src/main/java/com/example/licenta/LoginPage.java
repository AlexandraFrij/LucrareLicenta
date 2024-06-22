package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.util.AlertDialogMessages;
import com.example.licenta.util.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPage extends AppCompatActivity {
    String email;

    private FirebaseHelper dbHelper;
    private FirebaseAuth firebaseAuth;
    private AlertDialogMessages alertDialogMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new FirebaseHelper();
        alertDialogMessages = new AlertDialogMessages();
        firebaseAuth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);
        Button forgotPassBtn = findViewById(R.id.forgotpassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        Button createAccountBtn = findViewById(R.id.createAccount);
        ImageView seePassword = findViewById(R.id.seePassword);

        AndroidUtil.seePassword(editTextPassword,seePassword );

        View.OnClickListener buttonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.loginBtn)
                {
                    email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    confirmAndLoginUser(email, password);

                }
                else
                if (v.getId() == R.id.forgotpassword)
                {
                    Intent forgotPassword = new Intent(LoginPage.this, ForgotPasswordPage.class);
                    startActivity(forgotPassword);
                }
                else
                if (v.getId() == R.id.createAccount)
                {
                    Intent intent = new Intent(LoginPage.this, CreateAccountPage.class);
                    startActivity(intent);
                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        forgotPassBtn.setOnClickListener(buttonClickListener);
        loginBtn.setOnClickListener(buttonClickListener);
        createAccountBtn.setOnClickListener(buttonClickListener);
    }


    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }
    private void loginUser(String email)
    {
        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.commit();
        dbHelper.userIsStudent(email)
                .addOnSuccessListener(bool -> {
                    if (bool) {
                        editor.putString("status", "student");
                        editor.commit();
                        Intent home = new Intent(LoginPage.this, StudentHomePage.class);
                        startActivity(home);
                    } else {
                        editor.putString("status", "professor");
                        editor.commit();
                        Intent home = new Intent(LoginPage.this, ProfHomePage.class);
                        startActivity(home);
                    }
                });
    }
    private void confirmAndLoginUser(String email, String password)
    {
        if(email.isEmpty())
            showError("Introduceti adresa de e-mail!");
        else if(password.isEmpty())
            showError("Introduceti parola!");
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task ->{
                        if(task.isSuccessful())
                        {
                            loginUser(email);
                        }
                        else showError("E-mail sau parola introdusa gresit!");
                    });
        }
    }



}
