package com.plate.root.khms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by olatunde on 7/19/2017.
 */

public class PhyssonUtil {
    public static String getLocalDateTimeFromStamp(String ts){
        //System.out.println("Tunde time "+ts);
        long timestamp = Long.parseLong(ts); //Example -> in ms
        Date d = new Date(timestamp );
        return d.toLocaleString();
    }

    public void confirmAction(Context mContext){

    }
    public static boolean connected(){
        boolean isConnected = false;
        try {
            // URL url = new URL("http://79.137.20.205:8888/nsvr/auth/partner/telephone/");

            URL url = new URL("https://www.physson.com/d");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int respCode = conn.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
                isConnected = true;
                //System.out.println("Tunde is connected........."+String.valueOf(respCode));
            }else {

            }
        }catch (IOException io){
            //System.out.println("Tunde isnot connected.........");
            io.printStackTrace();
        }
        return isConnected;

    }

    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }


    public static int getDistancebtw(Double lat1, Double lon1, Double lat2, Double lon2){
        float[] results = new float[1];
        Location.distanceBetween(lat1,lon1,
                lat2, lon2, results);
        float distanceb = results[0];
        int distance = (int)distanceb;
        return distance;
    }

    public static String getDeviceId(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static boolean isgpsenabled(final Context context) {
        Boolean res = true;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            res= false;
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(context);
            dialog.setMessage("Location service disabled");
            dialog.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
        return res;
    }


}
