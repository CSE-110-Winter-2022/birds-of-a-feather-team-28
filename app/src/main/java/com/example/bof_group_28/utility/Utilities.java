package com.example.bof_group_28.utility;
import android.app.Activity;
import android.app.AlertDialog;

import java.util.Optional;

public class Utilities {

    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("ALERT").setMessage(message).setPositiveButton("ok", (dialog,id) -> {dialog.cancel();}).setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static Optional<Integer> parseCount(String str) {
        try {
            int maxCount = Integer.parseInt(str);
            return Optional.of(maxCount);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
