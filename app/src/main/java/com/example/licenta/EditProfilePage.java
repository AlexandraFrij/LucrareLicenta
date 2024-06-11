package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.item.RecentChatsRecyclerViewItem;

import java.util.regex.Pattern;

public class EditProfilePage extends AppCompatActivity
{
    private FirebaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new FirebaseHelper();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        Button saveBtn = findViewById(R.id.saveBtn);
        Button leaveBtn = findViewById(R.id.leaveBtn);
        EditText editLastName = findViewById(R.id.lastname);
        EditText editFirstName = findViewById(R.id.firstname);
        EditText editEmail = findViewById(R.id.email);
        TextView status = findViewById(R.id.status);

        String oldEmail = sp.getString("email", null);
        String[] info = new String[1];
        dbHelper.retrieveDataWithEmail(oldEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String[] result = task.getResult();
                        if (result != null) {
                            editLastName.setHint(result[0]);
                            editFirstName.setHint(result[1]);
                            editEmail.setHint(oldEmail);
                            status.setText(result[2]);
                            info[0] = result[2];
                        }
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });



        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.saveBtn)
                {
                    String lastName = editLastName.getText().toString().trim();
                    String firstName = editFirstName.getText().toString().trim();
                    String newEmail = editEmail.getText().toString().trim();
                    updateData(lastName, firstName, info[0], newEmail, oldEmail, sp);
                    String message = "Date salvate!";
                    Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_SHORT).show();
                }
                else
                if (v.getId() == R.id.leaveBtn)
                {
                    Intent profile = new Intent(EditProfilePage.this, ProfilePage.class);
                    startActivity(profile);
                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        saveBtn.setOnClickListener(buttonClickListener);
        leaveBtn.setOnClickListener(buttonClickListener);
    }

    private void updateData(String lastname, String firstname, String password, String newEmail, String oldEmail, SharedPreferences sp)
    {
        if (! lastname.isEmpty())
            dbHelper.updateLastName(lastname, oldEmail);
        if (! firstname.isEmpty())
            dbHelper.updateFirstName(firstname, oldEmail);
        if (! newEmail.isEmpty())
            dbHelper.emailExists(oldEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean exists = task.getResult();
                    if (exists) {
                        String message = "Adresa de e-mail aflata in folosinta! ";
                        Toast.makeText(EditProfilePage.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dbHelper.updateEmail(newEmail, oldEmail);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", newEmail);
                        editor.apply();
                    }
                }
            });
    }


}
