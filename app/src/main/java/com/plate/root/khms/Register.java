package com.plate.root.khms;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Register extends Activity {
    ImageView imVCature_pic;
    Button btnCapture;
    File filedir,file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Test
        String f =new Jasonparsee(getBaseContext()).loadJSONFromAsset();
        initializeControls();
    }

    private void initializeControls() {
        imVCature_pic=(ImageView)findViewById(R.id.imVCature_pic);
        filedir = new File(Environment.getExternalStorageDirectory(),Constants.PHOTODIRR);
        if (!filedir.exists())filedir.mkdir();
        file = new File(filedir.toString(), "img.jpg");
        if (file.exists()){
            //imVCature_pic.setImageBitmap();
            Toast.makeText(getBaseContext(),"File exist",Toast.LENGTH_SHORT).show();
            System.out.println("Tunde File exist "+file.toString());
            Glide.with(Register.this)
                    .load(file.toString())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(imVCature_pic);
        }

        btnCapture=(Button)findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                file = new File(filedir.toString(), "img.jpg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(intent, 1);
                Intent intent = new Intent(Register.this,BarcodeAttendance.class);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if request code is same we pass as argument in startActivityForResult
        if(requestCode==1){
            //create instance of File with same name we created before to get image from storage
            File file = new File(filedir.toString(), "img.jpg");
            //Crop the captured image using an other intent
            try {
                /*the user's device may not support cropping*/
                cropCapturedImage(Uri.fromFile(file));
            }
            catch(ActivityNotFoundException aNFE){
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if(requestCode==2){
            //Create an instance of bundle and get the returned data
            Bundle extras = data.getExtras();
            //get the cropped bitmap from extras
            Bitmap thePic = extras.getParcelable("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thePic.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            File f = new File(filedir.toString(),"img.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            }catch (IOException io){
                io.printStackTrace();
            }

            //set image bitmap to image view
            //imVCature_pic.setImageBitmap(thePic);
            Glide.with(Register.this)
                    .load(file.toString())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(imVCature_pic);

            System.out.println("Tunde "+thePic.getConfig().toString());
        }
    }
    //create helping method cropCapturedImage(Uri picUri)
    public void cropCapturedImage(Uri picUri){
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }
}