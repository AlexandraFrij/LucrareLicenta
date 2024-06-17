package com.example.licenta.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AndroidUtil {
    public static void setProfilePicture(Context context, Uri image, ImageView imageView)
    {
        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
