package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import android.widget.ImageView;

import com.example.licenta.util.AlertDialogMessages;
import com.example.licenta.util.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;

public class CreateStudentAccount extends AppCompatActivity
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
        setContentView(R.layout.create_student_account);

        EditText editTextLastName = findViewById(R.id.lastname);
        EditText editTextFirstName = findViewById(R.id.firstname);
        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextIdNumber = findViewById(R.id.studIDNum);
        EditText editTextPassword = findViewById(R.id.password);
        EditText editTextConfirmPassword = findViewById(R.id.confirmPassword);

        ImageView seePassword = findViewById(R.id.seePassword);
        ImageView seePasswordConfirmation = findViewById(R.id.seePasswordConfirmation);
        AndroidUtil.seePassword(editTextPassword, seePassword);
        AndroidUtil.seePassword(editTextConfirmPassword, seePasswordConfirmation);

        Button createAcc = findViewById(R.id.createStudAccBtn);
        createAcc.setOnClickListener(v ->
        {
            String lastName = editTextLastName.getText().toString().trim();
            String firstName = editTextFirstName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String idNumber = editTextIdNumber.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirmation = editTextConfirmPassword.getText().toString().trim();

            String message;
            message = verifyData(lastName, firstName, email, idNumber, password, passwordConfirmation);
            if (!message.equals("ok"))
            {
                showError(message);
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful())
                            {
                                dbHelper.insertStudentData(lastName, firstName, email, idNumber);
                                dbHelper.insertUser(email, "student");
                                Intent login = new Intent(CreateStudentAccount.this, LoginPage.class);
                                startActivity(login);
                            }
                            else {
                                showError("Eroare la autentificare! Incercati din nou..");
                            }

                        });

            }
        });

    }

    public String verifyData(String lastName, String firstName, String email, String idNumber, String pass1, String pass2)
    {
        String s = verifyName(lastName, firstName);
        if (!s.equals("ok"))
            return s;
        s = verifyEmail(email);
        if (!s.equals("ok"))
            return s;
        s = verifyIdNumber(idNumber);
        if (!s.equals("ok"))
            return s;
        s = verifyPassword(pass1, pass2);
        if (!s.equals("ok"))
            return s;
        return "ok";
    }

    public String verifyPassword(String password, String passwordConf)
    {
        if (!password.equals(passwordConf))
            return "Parola reintrodusa este gresita!";
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#%^&*()_+=-]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(password).matches())
            return "Parola invalida!";
        return "ok";
    }

    public String verifyName(String lastName, String firstName)
    {
        String regex = "^[A-Z][a-z]*(?:-[A-Z][a-z]*)*$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(lastName).matches())
            return "Nume invalid!";
        if (!pattern.matcher(firstName).matches())
            return "Prenume invalid!";
        return "ok";
    }

    public String verifyEmail(String email) {
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

    public String verifyIdNumber(String idNumber)
    {
        String[] message = new String[] {"ok"};
        dbHelper.idNumberExists(idNumber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean exists = task.getResult();
                if (exists) {
                    message[0]= "Numar matricol aflat in folosinta!";
                }
            }
        });
        String regex = "^310910401(RSL|ESL)\\d{6}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(idNumber).matches())
            message[0] = "Numar matricol invalid!";
        return message[0];
    }
    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }


}