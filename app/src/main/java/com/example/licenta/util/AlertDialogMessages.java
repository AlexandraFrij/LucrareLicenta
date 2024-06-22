package com.example.licenta.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.licenta.R;

public class AlertDialogMessages {
    public void showSuccessDialog(Context context, String message) {
        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle("Succes")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
    public void showErrorDialog(Context context, String message) {
        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle("Eroare")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
