package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.util.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends AppCompatActivity {

    private FirebaseHelper dbHelper;
    private FirebaseAuth firebaseAuth;
    private String userStatus, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new FirebaseHelper();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        Button logoffBtn = findViewById(R.id.logoffBtn);
        Button editProfileBtn = findViewById(R.id.editProfileBtn);
        Button goBackBtn = findViewById(R.id.goBackBtn);
        Button deleteAccBtn = findViewById(R.id.deleteAccBtn);
        TextView name = findViewById(R.id.profileName);
        TextView emailAddress = findViewById(R.id.profileEmail);


        ImageView profilePicView = findViewById(R.id.profilePic);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userEmail = sp.getString("email", null);
        userStatus = sp.getString("status", null);


        FirebaseHelper.getProfilePicture(userEmail).getDownloadUrl()
                        .addOnCompleteListener( task ->
                        {
                            if(task.isSuccessful())
                            {
                                Uri uri = task.getResult();
                                AndroidUtil.setProfilePicture(getApplicationContext(), uri, profilePicView);
                            }
                        });

        dbHelper.retrieveDataWithEmail(userEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String[] info = task.getResult();
                        if (info != null) {
                            String username = info[0] + " " + info[1];
                            name.setText(username);
                            emailAddress.setText(userEmail);
                        }

                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });

        View.OnClickListener buttonClickListener = v -> {
            if (v.getId() == R.id.logoffBtn)
            {
                Intent signIn = new Intent(ProfilePage.this, LoginPage.class);
                startActivity(signIn);
            }
            else
            if (v.getId() == R.id.editProfileBtn)
            {
                Intent edit = new Intent(ProfilePage.this, EditProfilePage.class);
                startActivity(edit);
            }
            else
            if (v.getId() == R.id.goBackBtn)
            {
                if(userStatus.equals("professor"))
                {
                    Intent home = new Intent(ProfilePage.this, ProfHomePage.class);
                    startActivity(home);
                }
                else {
                    Intent home = new Intent(ProfilePage.this, StudentHomePage.class);
                    startActivity(home);
                }
            }
            else
            if (v.getId() == R.id.deleteAccBtn)
            {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if( user != null)
                {
                    user.delete().addOnCompleteListener( task ->{
                        if(task.isSuccessful())
                        {
                            dbHelper.deleteAccount(userEmail);
                            showDialog("Cont sters!");
                            Intent signIn = new Intent(ProfilePage.this, LoginPage.class);
                            startActivity(signIn);
                        }
                        else   showDialog("Eroare la stergerea contului!");

                    });
                }
            }
            else
            {
                throw new IllegalArgumentException("Unknown button");
            }
        };

        logoffBtn.setOnClickListener(buttonClickListener);
        editProfileBtn.setOnClickListener(buttonClickListener);
        goBackBtn.setOnClickListener(buttonClickListener);
        deleteAccBtn.setOnClickListener(buttonClickListener);
    }
    public void showDialog(String message) {
        new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
