package com.example.bof_group_28;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;

public class Utilities {

  // This method was brought from Lab 4 (https://docs.google.com/document/d/1Gbf7HGxMNuVX3yluPZ40ss0IjlNDwtGmXDlL1p6PXMk/edit#heading=h.a205kzsi5eva)
  public static void showAlert(Activity activity, String message) {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

    alertBuilder
      .setTitle("Alert!")
      .setMessage(message)
      .setPositiveButton("Ok", (dialog, id) -> {
        dialog.cancel();
      })
      .setCancelable(true);

    AlertDialog AlertDialog = alertBuilder.create();
    AlertDialog.show();
  }
}
