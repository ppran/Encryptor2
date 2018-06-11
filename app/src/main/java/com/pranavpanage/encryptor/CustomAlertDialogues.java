package com.pranavpanage.encryptor;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class CustomAlertDialogues {

    private Context context;

    public CustomAlertDialogues(Context context) {
        this.context = context;
    }

    public void displayErrorDialogue(String message, String title) {
        AlertDialog.Builder alertDialogBuilderKey = new AlertDialog.Builder(context);
        alertDialogBuilderKey.setMessage(message).setTitle(title);

        alertDialogBuilderKey.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialogKey = alertDialogBuilderKey.create();
        alertDialogKey.show();
    }

    public void displayDecodeAlertDialogue(final String output) {
        //Display MD5 Hash
        //Display Dialogue With Result
        AlertDialog.Builder alertDialogBuilderMd5 = new AlertDialog.Builder(context);
        alertDialogBuilderMd5.setMessage(output).setTitle("Decoded Text");
        alertDialogBuilderMd5.setPositiveButton("Copy",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Decoded Text", output);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context.getApplicationContext(), "Copied To Clipboard", Toast.LENGTH_LONG).show();
                    }
                });

        //Share Content
        alertDialogBuilderMd5.setNegativeButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, output);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        alertDialogBuilderMd5.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialogMd5 = alertDialogBuilderMd5.create();
        alertDialogMd5.show();
    }

    public void displayAlertDialogue(final String output, String encoder) {

        //Display Dialogue With Result
        AlertDialog.Builder alertDialogBuilderMd5 = new AlertDialog.Builder(context);
        alertDialogBuilderMd5.setMessage(output).setTitle(encoder + " Encoding Result");
        alertDialogBuilderMd5.setPositiveButton("Copy",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Encoded Text", output);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context.getApplicationContext(), "Copied To Clipboard", Toast.LENGTH_LONG).show();
                    }
                });

        //Share Content
        alertDialogBuilderMd5.setNegativeButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, output);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        alertDialogBuilderMd5.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialogMd5 = alertDialogBuilderMd5.create();
        alertDialogMd5.show();
    }


}
