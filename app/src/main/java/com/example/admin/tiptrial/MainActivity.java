package com.example.admin.tiptrial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/*<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>*/
public class MainActivity extends AppCompatActivity {
    private PreferenceManager prefManager;
    ImageView i1,i2;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PreferenceManager(this);
       /* i1 = (ImageView) findViewById(R.id.imageView);
        i2 = (ImageView) findViewById(R.id.imageView2);*/

    }
    @Override
    protected void onStart() {
        super.onStart();

        startAnimation();
        /*float b1 = getResources().getDisplayMetrics().heightPixels - (i1.getHeight());
        float b2 = getResources().getDisplayMetrics().heightPixels - (i2.getHeight());
        float b = b1 - b2;
        i1.animate()
                .translationY(b)
.setInterpolator(new AccelerateInterpolator())
                .setInterpolator(new BounceInterpolator())
                .setDuration(2000);*/
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                isConnectionAvailable();
                isGpsAvailable();
                if(isGpsAvailable() & isConnectionAvailable())
                {
                    if (!prefManager.isFirstTimeLaunch()) {
                        launchHomeScreen();
                        finish();
                    }
                    else{

                        launchNextScreen();
                    }
                }

            }
        }, 1970);




    }
    private void launchHomeScreen() {

        startActivity(new Intent(MainActivity.this, Main4Activity.class));
        finish();
    }
    private void launchNextScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
        finish();
    }

    public void startAnimation() {

        final ImageView image = (ImageView)findViewById(R.id.imageView);
        image.setVisibility(View.VISIBLE);

        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.loader_anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.loader_anim);
                anim.setAnimationListener(this);
                image.startAnimation(anim);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });


        image.startAnimation(anim);
    }
    public static boolean hasPermissions(Context context, String... permissions){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }
    private boolean isConnectionAvailable() {

        boolean netCon = false;

        try {

            //Internet & network information "Object" initialization
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //Internet enable & connectivity checking
            if ("WIFI".equals(networkInfo.getTypeName()) || "MOBILE".equals(networkInfo.getTypeName()) && networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()) {
                netCon = true;
            }
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("No Network Connection!")
                    .setMessage("Please connect your device to either WiFi or switch on Mobile Data, operator charges may apply!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    }).show();
        }
        return netCon;
    }

    private boolean isGpsAvailable() {

        boolean gpsCon = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled.", Toast.LENGTH_SHORT).show();
            gpsCon = true;
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("GPS is disabled!")
                    .setMessage("Without GPS this application will not work! Would you like to enable the GPS?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent callGpsSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGpsSetting);
                        }
                    })
                    .setNegativeButton("Exit.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }
        return gpsCon;
    }


}
