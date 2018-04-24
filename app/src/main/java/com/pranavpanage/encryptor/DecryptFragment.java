package com.pranavpanage.encryptor;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * Created by Pranav on 1/11/2018.
 */

public class DecryptFragment extends Fragment{


    String demode;
    TextView encryptedText, decreptedText;
    String inputtext;
    byte [] decoded;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.decrypt_fragment,container,false);


        Spinner spinner2 = (Spinner) view.findViewById(R.id.decspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.decodealgo_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter);

        Button decryptButton = (Button) view.findViewById(R.id.Decrypt);



        encryptedText = (TextView) view.findViewById(R.id.textToDecrypt) ;



        //Check if there is any shared data
        String SharedData= ((MainActivity) getActivity()).getSharedData();
        if(SharedData!=null){
           // if(((MainActivity) getActivity()).getDecodeModeStatus())
                encryptedText.setText(SharedData);

        }



        //Decode Function
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                inputtext = encryptedText.getText().toString();

                //Toast.makeText(getActivity().getApplicationContext(), "Selected: " + demode + inputtext, Toast.LENGTH_LONG).show();





                switch (demode){
                    case "Base64" :

                        String error="";
                        try {
                            decoded = Base64.decode(inputtext.getBytes("UTF-8"), Base64.DEFAULT);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            error = e.toString();
                        }
                        // Toast.makeText(getActivity().getApplicationContext(), "Selected: " + mode + encodedString, Toast.LENGTH_LONG).show();

                        if (error.contains("bad base-64")){

                            //Display Error Dialogue : No Algorithm Selected
                            AlertDialog.Builder alertDialogBuilderKey = new AlertDialog.Builder(getContext());
                            //alertDialogBuilderKey.setMessage(md5).setTitle("Enter Key");


                            alertDialogBuilderKey.setMessage("Please Enter Valid Base-64").setTitle("Error");

                            alertDialogBuilderKey.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {



                                        }
                                    });

                            alertDialogBuilderKey.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Do SOmething
                                }
                            });

                            AlertDialog alertDialogKey = alertDialogBuilderKey.create();
                            alertDialogKey.show();
                        }
                        else{


                            //Display Dialogue With Result
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            try {
                                alertDialogBuilder.setMessage(new String(decoded,"UTF-8")).setTitle("Decoded Text");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            alertDialogBuilder.setPositiveButton("Copy & Close",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = null;
                                            try {
                                                clip = ClipData.newPlainText("Encrypted Text", new String(decoded,"UTF-8"));
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(getActivity().getApplicationContext(), "Copied To Clipboard" , Toast.LENGTH_LONG).show();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("Share",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    try {
                                        sendIntent.putExtra(Intent.EXTRA_TEXT,  new String(decoded,"UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }





                        break;



                }


            }
        });


        //Select Mode
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                demode = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getActivity().getApplicationContext(), "Selected: " + demode, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}
