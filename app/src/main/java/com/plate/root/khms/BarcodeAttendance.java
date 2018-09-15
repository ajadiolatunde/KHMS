package com.plate.root.khms;

//import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeAttendance extends AppCompatActivity  {
    SurfaceView cameraView;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    ImageView imageView;
    TextView phonetxt;
    Boolean first = false;
    String code;
    String previous=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);
        imageView =(ImageView)findViewById(R.id.imgview);
        phonetxt = (TextView)findViewById(R.id.phonetxt);
        cameraView = (SurfaceView)findViewById(R.id.srf);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                int camera = ContextCompat.checkSelfPermission(BarcodeAttendance.this, android.Manifest.permission.CAMERA);
                try {

                    if (camera == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    }else{
                        Toast.makeText(getBaseContext(),"Permission not granted",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    phonetxt.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            //Check if parent tag first=true
                            code = barcodes.valueAt(0).displayValue;
                            if (!first) {
                                toneDisplay(getBaseContext(), code);
                                first = true;
                                previous = code;
                            } else {
                                if (previous.equals(code)) {
                                    //Do nothing
                                } else {
                                    toneDisplay(getBaseContext(), code);
                                    previous = code;

                                }
                            }
                        }
                     }
                   );
                }
            }
        });

    }
    private void toneDisplay(Context context,String cod){
        Toast.makeText(context, cod, Toast.LENGTH_SHORT).show();
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,150);
    }


}
