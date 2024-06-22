package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;

import com.example.licenta.util.AlertDialogMessages;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        EditText emailEditText = findViewById(R.id.userEmail);
        Button cancelButton = findViewById(R.id.cancelBtn);
        Button resetButton = findViewById(R.id.resetBtn);
        FirebaseHelper dbHelper = new FirebaseHelper();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        AlertDialogMessages alertDialogMessages = new AlertDialogMessages();


        cancelButton.setOnClickListener( v->{
            Intent intent = new Intent(ForgotPasswordPage.this, LoginPage.class);
            startActivity(intent);
        });
        resetButton.setOnClickListener( v->{
            String email = emailEditText.getText().toString().trim();
            if(email.isEmpty())
                alertDialogMessages.showErrorDialog(ForgotPasswordPage.this, "Introduceti adresa e-mail!");
            else
            {
                dbHelper.emailExists(email).addOnCompleteListener( task ->{
                    if(task.isSuccessful())
                    {
                        boolean bool = task.getResult();
                        if(bool)
                        {
                            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener( task1 ->{
                                if(task1.isSuccessful())
                                {
                                    alertDialogMessages.showSuccessDialog(ForgotPasswordPage.this, "Verificati e-mail!");
                                    Intent intent = new Intent(ForgotPasswordPage.this, LoginPage.class);
                                    startActivity(intent);
                                }

                                else alertDialogMessages.showErrorDialog(ForgotPasswordPage.this, "Resetarea a esuat!");
                            });
                        }
                        else alertDialogMessages.showErrorDialog(ForgotPasswordPage.this, "Adresa de e-mail invalida!");
                    }
                });
            }
        });
    }

}

