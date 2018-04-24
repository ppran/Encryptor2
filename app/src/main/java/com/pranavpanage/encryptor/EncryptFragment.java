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
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Pranav on 1/11/2018.
 */

public class EncryptFragment extends Fragment  {

    String mode;
    private byte [] encoded;

    byte[] mKey,mAES;
    String md5,mSHA;
    String inputtext;
    TextView textview,resulttextview;


    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    @Nullable
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







            //Encrypt Button Clicked
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputtext = textview.getText().toString();

                //No Input
                if (inputtext.equals("")) {

                    //Display Error Dialogue : No Algorithm Selected
                    AlertDialog.Builder alertDialogBuilderKey = new AlertDialog.Builder(getContext());
                    //alertDialogBuilderKey.setMessage(md5).setTitle("Enter Key");


                    alertDialogBuilderKey.setMessage("Please Enter Text To Encode").setTitle("Error");

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
                    return;


                } else {
                    switch (mode) {
                        case "Base64":

                            try {
                                encoded = Base64.encode(inputtext.getBytes("UTF-8"), Base64.DEFAULT);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            // Toast.makeText(getActivity().getApplicationContext(), "Selected: " + mode + encodedString, Toast.LENGTH_LONG).show();

                            //Display Dialogue With Result
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            try {
                                alertDialogBuilder.setMessage(new String(encoded, "UTF-8")).setTitle("Encoded " + mode + " Text");
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
                                                clip = ClipData.newPlainText("Encoded Text", new String(encoded, "UTF-8"));
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(getActivity().getApplicationContext(), "Copied To Clipboard", Toast.LENGTH_LONG).show();
                                        }
                                    });

                            // Share text to Other Apps
                            alertDialogBuilder.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    try {
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, new String(encoded, "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            break;

                        case "md5":
                            MessageDigest mdEnc = null;
                            try {
                                mdEnc = MessageDigest.getInstance("MD5");
                            } catch (NoSuchAlgorithmException e) {
                                System.out.println("Exception while encrypting to md5");
                                e.printStackTrace();
                            } // Encryption algorithm
                            try {
                                mdEnc.update(inputtext.getBytes("UTF-8"), 0, inputtext.length());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            md5 = new BigInteger(1, mdEnc.digest()).toString(16);
                            while (md5.length() < 32) {
                                md5 = "0" + md5;
                            }

                            //Display MD5 Hash
                            //Display Dialogue With Result
                            AlertDialog.Builder alertDialogBuilderMd5 = new AlertDialog.Builder(getContext());
                            alertDialogBuilderMd5.setMessage(md5).setTitle("Encoded " + mode + " Text");
                            alertDialogBuilderMd5.setPositiveButton("Copy & Close",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = null;
                                            clip = ClipData.newPlainText("Encoded Text", md5);
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(getActivity().getApplicationContext(), "Copied To Clipboard", Toast.LENGTH_LONG).show();
                                        }
                                    });

                            alertDialogBuilderMd5.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, md5);

                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);

                                    //Do SOmething
                                }
                            });

                            AlertDialog alertDialogMd5 = alertDialogBuilderMd5.create();
                            alertDialogMd5.show();
                            break;

                        /*
                    case "AES":
                        // setup AES cipher in CBC mode with PKCS #5 padding
                        Cipher localCipher = null;
                        try {
                            localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        }
                        try {
                            localCipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(mKey, "AES"), new IvParameterSpec(ivBytes));
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        }


                        try {
                            mAES = localCipher.doFinal(inputtext.getBytes("UTF-8"));
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //Display AES Encryption
                        //Display Dialogue With Result
                        AlertDialog.Builder alertDialogBuilderAES = new AlertDialog.Builder(getContext());
                        alertDialogBuilderAES.setMessage(md5).setTitle("Encrypted Text");
                        alertDialogBuilderAES.setPositiveButton("Copy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = null;
                                        clip = ClipData.newPlainText("Encrypted Text", mAES.toString());
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(getActivity().getApplicationContext(), "Copied To Clipboard" , Toast.LENGTH_LONG).show();
                                    }
                                });

                        alertDialogBuilderAES.setNegativeButton("Close",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Do SOmething
                            }
                        });

                        AlertDialog alertDialogAES = alertDialogBuilderAES.create();
                        alertDialogAES.show();

                        break;*/

                        case "SHA":

                            MessageDigest ShaEncoder = null;
                            try {
                                ShaEncoder = MessageDigest.getInstance("SHA");
                            } catch (NoSuchAlgorithmException e) {
                                System.out.println("Exception while encrypting to md5");
                                e.printStackTrace();
                            } // Encryption algorithm
                            try {
                                ShaEncoder.update(inputtext.getBytes("UTF-8"), 0, inputtext.length());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            mSHA = new BigInteger(1, ShaEncoder.digest()).toString(16);
                            while (mSHA.length() < 32) {
                                mSHA = "0" + mSHA;
                            }

                            //Display MD5 Hash
                            //Display Dialogue With Result
                            AlertDialog.Builder alertDialogBuilderSHA = new AlertDialog.Builder(getContext());
                            alertDialogBuilderSHA.setMessage(mSHA).setTitle("Encoded " + mode + " Text");
                            alertDialogBuilderSHA.setPositiveButton("Copy & Close",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = null;
                                            clip = ClipData.newPlainText("Encoded Text", mSHA);
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(getActivity().getApplicationContext(), "Copied To Clipboard", Toast.LENGTH_LONG).show();
                                        }
                                    });

                            //share to Other Apps
                            alertDialogBuilderSHA.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, mSHA);

                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);

                                }
                            });

                            AlertDialog alertDialogSHA = alertDialogBuilderSHA.create();
                            alertDialogSHA.show();
                            break;




                        /*
                    default:



                        //Display Error Dialogue : No Algorithm Selected
                        AlertDialog.Builder alertDialogBuilderKey = new AlertDialog.Builder(getContext());
                        //alertDialogBuilderKey.setMessage(md5).setTitle("Enter Key");


                        alertDialogBuilderKey.setMessage("Select Algorithm To Encode").setTitle("Error");

                        alertDialogBuilderKey.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {




                                    }
                                });

                        alertDialogBuilderKey.setNegativeButton("Close",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Do SOmething
                            }
                        });

                        AlertDialog alertDialogKey = alertDialogBuilderKey.create();
                        alertDialogKey.show();

                        break;

                        */


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
                    //Toast.makeText(getActivity().getApplicationContext(), "Selected: " + mode, Toast.LENGTH_LONG).show();

                    if (mode.equals("AES")){

                        //Display Dialogue To get Secret Key
                        AlertDialog.Builder alertDialogBuilderKey = new AlertDialog.Builder(getContext());
                        //alertDialogBuilderKey.setMessage(md5).setTitle("Enter Key");
                        // Set up the input
                        final EditText input = new EditText(getContext());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        alertDialogBuilderKey.setView(input).setTitle("Enter Key For AES Encryption");

                        alertDialogBuilderKey.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        try {
                                            mKey = input.getText().toString().getBytes("UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                        alertDialogBuilderKey.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Do SOmething
                            }
                        });

                        AlertDialog alertDialogKey = alertDialogBuilderKey.create();
                        alertDialogKey.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        return view;


    }

}
