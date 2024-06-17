package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

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

        SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);

        Button forgotPassBtn = findViewById(R.id.forgotpassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        Button createAccountBtn = findViewById(R.id.createAccount);

        ImageView seePassword = findViewById(R.id.seePassword);
        seePassword.setImageResource(R.drawable.see_password);
        seePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    seePassword.setImageResource(R.drawable.hide_password);
                }
                else
                {
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    seePassword.setImageResource(R.drawable.see_password);
                }
            }
        });

        View.OnClickListener buttonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.loginBtn)
                {
                    email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    if(email.isEmpty())
                        showError("Introduceti adresa de e-mail!");
                    else if(password.isEmpty())
                        showError("Introduceti parola!");
                    else {
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task ->{
                                    if(task.isSuccessful())
                                    {
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
                                    else showError("E-mail sau parola introdusa gresit!");
                                });
                    }

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

}
