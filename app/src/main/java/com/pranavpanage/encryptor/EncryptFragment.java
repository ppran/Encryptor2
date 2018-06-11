package com.pranavpanage.encryptor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Pranav on 1/11/2018.
 */

public class EncryptFragment extends Fragment  {

    String mode;
    TextView textview;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().

            if (resultData != null) {
                Uri file_uri = resultData.getData();
                //Toast.makeText(getContext(),file_uri.toString(),Toast.LENGTH_SHORT).show();
                MyEncoders myEncoders = new MyEncoders(getContext());
                CustomAlertDialogues customAlertDialogues = new CustomAlertDialogues(getContext());
                switch (mode) {
                    case "MD5":
                        customAlertDialogues.displayAlertDialogue(myEncoders.calculateFileMD5(file_uri), "MD5");
                        break;
                    case "SHA":
                        customAlertDialogues.displayAlertDialogue(myEncoders.calculateFileSHA(file_uri), "MD5");
                        break;
                    case "Base64":
                        customAlertDialogues.displayErrorDialogue("Base64 of File too long to display", "Not Supported");
                        break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.encrypt_fragment,container,false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.algo_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Button encryptButton = (Button) view.findViewById(R.id.button);

        textview= (TextView) view.findViewById(R.id.textToDecrypt);

        //Check if there is any shared data
        String SharedData= ((MainActivity) getActivity()).getSharedData();
        if(SharedData!=null) {
           // if(((MainActivity) getActivity()).getEncodeModeStatus())
                textview.setText(SharedData);
        }

        //Load File
        Button loadFile = (Button) view.findViewById(R.id.button2);
        loadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        //Encrypt Button Clicked
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomAlertDialogues customAlertDialogues = new CustomAlertDialogues(getContext());
                String inputtext = textview.getText().toString();

                //No Input
                if (inputtext.equals("")) {
                    //Display Error Dialogue : No Algorithm Selected
                    customAlertDialogues.displayErrorDialogue("Please Enter Text To Encode", "Error");
                    return;
                } else {
                    //Select mode Base64 / MD5 / SHA
                    switch (mode) {
                        case "Base64":
                            String output = MyEncoders.calculateBase64(inputtext);
                            //Display Result
                            customAlertDialogues.displayAlertDialogue(output, "Base64");
                            break;

                        case "MD5":
                            //Encode Using MD5
                            String md5 = MyEncoders.calculateMD5(inputtext);
                            //Display Result
                            customAlertDialogues.displayAlertDialogue(md5, "MD5");
                            break;

                        case "SHA":
                            String mSHA = MyEncoders.calculateSHA(inputtext);
                            //Display SHA Hash
                            //Display Dialogue With Result
                            customAlertDialogues.displayAlertDialogue(mSHA, "SHA");
                            break;
                    }
                }
            }
        });

        //Select Mode
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {
                    mode = parent.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        return view;
    }
}
