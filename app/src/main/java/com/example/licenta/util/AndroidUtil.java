package com.example.licenta.util;

import android.content.Context;
import android.net.Uri;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.licenta.R;

public class AndroidUtil {
    public static void setProfilePicture(Context context, Uri image, ImageView imageView)
    {
        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static void seePassword(EditText editTextPassword, ImageView seePassword)
    {
        seePassword.setImageResource(R.drawable.see_password);
        seePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    seePassword.setImageResource(R.drawable.see_password);
                    editTextPassword.postInvalidate();
                }
                else
                {
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    seePassword.setImageResource(R.drawable.hide_password);
                    editTextPassword.postInvalidate();
                }
                seePassword.invalidate();
            }
        });
    }
}
