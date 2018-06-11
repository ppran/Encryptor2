package com.pranavpanage.encryptor;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class MyEncoders {
    Context context;

    public MyEncoders(Context context) {
        this.context = context;
    }

    static String calculateMD5(String inputtext) {

        String md5;
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

        return md5;
    }

    static String calculateSHA(String input) {

        MessageDigest ShaEncoder = null;
        String mSHA;
        try {
            ShaEncoder = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to SHA");
            e.printStackTrace();
        } // Encryption algorithm
        try {
            ShaEncoder.update(input.getBytes("UTF-8"), 0, input.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mSHA = new BigInteger(1, ShaEncoder.digest()).toString(16);
        while (mSHA.length() < 32) {
            mSHA = "0" + mSHA;
        }
        return mSHA;
    }

    public String calculateFileMD5(Uri file_uri) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //Log.e("Encoder", "Exception while getting digest", e);
            //Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
            return "";
        }

        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(file_uri);
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        byte[] buffer = new byte[8192];
        int read;

        try {
            while ((read = inputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;

        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("Encoder", "Exception on closing MD5 input stream", e);
            }
        }
    }

    public String calculateFileSHA(Uri file_uri) {
        MessageDigest ShaEncoder = null;
        try {
            ShaEncoder = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to SHA");
            e.printStackTrace();
        } // Encryption algorithm

        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(file_uri);
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        byte[] buffer = new byte[8192];
        int read;

        try {
            while ((read = inputStream.read(buffer)) > 0) {
                ShaEncoder.update(buffer, 0, read);
            }
            byte[] md5sum = ShaEncoder.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;

        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for SHA", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("Encoder", "Exception on closing SHA input stream", e);
            }
        }

    }

    static String calculateBase64(String inputtext) {

        byte[] encoded = new byte[0];
        try {
            encoded = Base64.encode(inputtext.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String output = null;
        try {
            output = new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return output;
    }


}
