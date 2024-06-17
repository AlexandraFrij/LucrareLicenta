package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;


public class CreateProfAccount extends AppCompatActivity
{

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_prof_account);

        EditText editTextLastName = findViewById(R.id.lastname);
        EditText editTextFirstName = findViewById(R.id.firstname);
        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextIdentificationNb = findViewById(R.id.identificationNb);
        EditText editTextPassword = findViewById(R.id.password);
        EditText editTextConfirmPassword = findViewById(R.id.confirmPassword);

        Button createAcc = findViewById(R.id.createProfAccBtn);
        createAcc.setOnClickListener(v ->
        {
            String lastName = editTextLastName.getText().toString().trim();
            String firstName = editTextFirstName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirmation = editTextConfirmPassword.getText().toString().trim();
            String identificationNb = editTextIdentificationNb.getText().toString().trim();

            String message;
            message = verifyData(lastName, firstName, email, password, passwordConfirmation, identificationNb);

            if (!message.equals("ok"))
            {
               showError(message);
            } else
            {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->{
                    if(task.isSuccessful())
                    {
                        dbHelper.insertProfData(lastName, firstName, email, identificationNb);
                        dbHelper.insertUser(email, "professor", password);
                        Intent login = new Intent(CreateProfAccount.this, LoginPage.class);
                        startActivity(login);
                    }
                    else {
                        showError("Eroare la autentificare! Incercati din nou..");
                    }
                });
            }
        });
    }
    private String verifyData(String lastName, String firstName, String email, String pass1, String pass2, String identificationNumber)
    {
        String s = verifyName(lastName, firstName);
        if (!s.equals("ok"))
            return s;
        s = verifyEmail(email);
        if (!s.equals("ok"))
            return s;
        s = verifyPassword(pass1, pass2);
        if (!s.equals("ok"))
            return s;
        s = verifyIdentificationNumber(email, identificationNumber);
        if (!s.equals("ok"))
            return s;
        return "ok";
    }

    private String verifyPassword(String password, String passwordConf)
    {
        if (!password.equals(passwordConf))
            return "Parola reintrodusa este gresita!";
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*()_+=-]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(password).matches())
            return "Parola invalida!";
        return "ok";
    }

    private String verifyName(String lastName, String firstName)
    {
        String regex = "^[A-Z][a-z]*(?:-[A-Z][a-z]*)*$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(lastName).matches())
            return "Nume invalid!";
        if (!pattern.matcher(firstName).matches())
            return "Prenume invalid!";
        return "ok";
    }

    private String verifyEmail(String email) {
        String[] message = new String[] {"ok"};
        dbHelper.emailExists(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean exists = task.getResult();
                if (exists) {
                    message[0]= "Adresa de e-mail aflata in folosinta!";
                }
                else {
                    String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
                    Pattern pattern = Pattern.compile(regex);
                    if (!pattern.matcher(email).matches()) {
                        message[0]= "E-mail invalid!";
                    }
                }
            }
        });
        return message[0];
    }
    private String verifyIdentificationNumber(String userEmail, String identificationNumber)
    {
        String[] message = new String[] {"Cod de verificare gresit!"};
        String path = "identificationNumbers.txt";

        try (InputStream inputStream = this.getAssets().open(path);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }
                String email = parts[0].trim();
                String code = parts[1].trim();
                if (email.equals(userEmail) && code.equals(identificationNumber))
                    return "ok";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message[0];

    }

    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }


}