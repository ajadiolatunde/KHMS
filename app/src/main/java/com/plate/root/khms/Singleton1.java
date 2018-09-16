package com.plate.root.khms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

class Singleton1 {
    SharedPreferences mSharedPrefrence;
    private Context context;
    private static  Singleton1 ourInstance ;

    public static Singleton1 getInstance(Context context) {
        if (ourInstance == null){

            ourInstance = new Singleton1(context);
        }
        return ourInstance;
    }

    private Singleton1(Context context) {
        this.context = context;
    }

    public void setmSharedPrefrence(){
        //https://stackoverflow.com/questions/30806342/pendingintent-cause-error
        //mSharedPrefrence = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPrefrence = context.getSharedPreferences(Constants.MYPREF, Activity.MODE_PRIVATE);
    }


    public boolean isPrefCreated(String name){
        mSharedPrefrence = context.getSharedPreferences(Constants.MYPREF, Activity.MODE_PRIVATE);
        boolean created = (mSharedPrefrence.contains(name))? true:false;
        return created;

    }


    public void addStringSharedPreff(String key, String value){
        if (isPrefCreated(key)){
            System.out.println("Tunde....  yes prefcreated");
            SharedPreferences.Editor editor = mSharedPrefrence.edit();
            editor.putString(key,value);
            editor.commit();

        }else {
            //setmSharedPrefrence();
            System.out.println("Tunde about to be ");
            SharedPreferences.Editor editor = mSharedPrefrence.edit();
            editor.putString("test","init");
            editor.putString(key,value);
            editor.commit();
        }

    }

    public void getAllpref(){
        System.out.println("Tunde...prefkeys   "+mSharedPrefrence.getAll().toString());
    }

    public String getPrefKey(String key){
        setmSharedPrefrence();
        String value = mSharedPrefrence.getString(key,"{}");
        return value;
    }
    public File getPhotoFile(String name){
        File externalFileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFileDir == null){
            return null;
        }
        return  new File(externalFileDir,name);
    }




    //INstanstiate shared prfremce table student staff

    //Return regisetred number

    //Return late commmer
    //Check date
    //
}
