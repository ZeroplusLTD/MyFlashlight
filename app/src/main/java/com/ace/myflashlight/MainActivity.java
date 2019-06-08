package com.ace.myflashlight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView lightBtn,MyChannel;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightBtn = findViewById(R.id.flashLightButton);
        MyChannel = findViewById(R.id.myChannel_Please_subscribe_my_channel);
        MyChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCNEKDh3EET6RBjmYsjGx23w?sub_confirmation=1"));
                startActivity(intent);
            }
        });
        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        boolean isEnable = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST );

        lightBtn.setEnabled(isEnable);

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (hasCameraFlash){

                   if (flashLightStatus){
                       flashlightOff();
                       MyChannel.setVisibility(View.GONE);
                   }else {
                       flashlightOn();
                       MyChannel.setVisibility(View.VISIBLE);
                   }


                }else {

                    //Toast.makeText(MainActivity.this, "No flash on your device", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void flashlightOn(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            lightBtn.setImageResource(R.drawable.flash_on);



        }catch (CameraAccessException e){

        }


    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void flashlightOff(){

        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,false);
            flashLightStatus = false;
            lightBtn.setImageResource(R.drawable.flash_off);
        }catch (CameraAccessException e){

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    lightBtn.setEnabled(true);
                }else {
                    Toast.makeText(this, "Permession denied for the camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }
}
