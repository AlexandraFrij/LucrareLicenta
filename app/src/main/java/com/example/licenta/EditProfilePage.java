package com.example.licenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.util.AndroidUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class EditProfilePage extends AppCompatActivity
{
    private FirebaseHelper dbHelper;
    private AlertDialogMessages alertDialogMessages;
    private ActivityResultLauncher<Intent> imagePickLauncher;
    private Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dbHelper = new FirebaseHelper();
        alertDialogMessages = new AlertDialogMessages();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        Button saveBtn = findViewById(R.id.saveBtn);
        Button leaveBtn = findViewById(R.id.leaveBtn);
        Button changePhotoBtn = findViewById(R.id.changePhotoBtn);

        EditText editLastName = findViewById(R.id.lastname);
        EditText editFirstName = findViewById(R.id.firstname);
        EditText editEmail = findViewById(R.id.email);
        TextView status = findViewById(R.id.status);
        ImageView profilePic = findViewById(R.id.profilePic);


        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
            if(result.getResultCode() == Activity.RESULT_OK)
            {
                Intent data = result.getData();
                if(data != null && data.getData()!=null)
                {
                    selectedImage = data.getData();
                    AndroidUtil.setProfilePicture(getApplicationContext(), selectedImage, profilePic);
                }
            }
                });


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
        FirebaseHelper.getProfilePicture(oldEmail).getDownloadUrl()
                .addOnCompleteListener( task ->
                {
                    if(task.isSuccessful())
                    {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePicture(getApplicationContext(), uri, profilePic);
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


                    if(selectedImage != null)
                    {
                        dbHelper.getProfilePicture(oldEmail).putFile(selectedImage)
                                .addOnCompleteListener( task ->
                                {
                                    updateData(lastName, firstName, info[0], newEmail, oldEmail, sp);
                                    String message = "Date salvate!";
                                    showSuccess(message);

                                });
                    }
                    else {
                        updateData(lastName, firstName, info[0], newEmail, oldEmail, sp);
                        String message = "Date salvate!";
                        showSuccess(message);
                    }
                }
                else if (v.getId() == R.id.leaveBtn)
                {
                    Intent profile = new Intent(EditProfilePage.this, ProfilePage.class);
                    startActivity(profile);
                }
                else if (v.getId() == R.id.changePhotoBtn)
                {
                    ImagePicker.with(EditProfilePage.this).cropSquare().compress(512).maxResultSize(512, 512)
                            .createIntent(new Function1<Intent, Unit>() {
                                @Override
                                public Unit invoke(Intent intent) {
                                    imagePickLauncher.launch(intent);
                                    return null;
                                }
                            });

                }
                else
                {
                    throw new IllegalArgumentException("Unknown button");
                }
            }
        };

        saveBtn.setOnClickListener(buttonClickListener);
        leaveBtn.setOnClickListener(buttonClickListener);
        changePhotoBtn.setOnClickListener(buttonClickListener);
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
                        showError(message);
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
    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showErrorDialog(this, message));
    }
    private void showSuccess(String message) {
        new Handler(Looper.getMainLooper()).post(() -> alertDialogMessages.showSuccessDialog(this, message));
    }

}
