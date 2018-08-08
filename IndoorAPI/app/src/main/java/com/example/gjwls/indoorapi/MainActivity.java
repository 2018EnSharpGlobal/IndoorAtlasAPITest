package com.example.gjwls.indoorapi;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

public class MainActivity extends AppCompatActivity {

    private final int CODE_PERMISSIONS = 0;//...

    IALocationManager mLocationManager;

    IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            TextView txtLoc = (TextView)findViewById(R.id.textView);
            Log.e(this.getClass().getName(),String.valueOf("---------------------------------"+iaLocation.getLatitude() +","+iaLocation.getLongitude()));
            txtLoc.setText(String.valueOf(iaLocation.getLatitude() +","+iaLocation.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

            switch (i) {
                case IALocationManager.STATUS_CALIBRATION_CHANGED:
                    String quality = "unknown";
                    switch (bundle.getInt("quality")) {
                        case IALocationManager.CALIBRATION_POOR:
                            quality = "Poor";
                            break;
                        case IALocationManager.CALIBRATION_GOOD:
                            quality = "Good";
                            break;
                        case IALocationManager.CALIBRATION_EXCELLENT:
                            quality = "Excellent";
                            break;
                    }

//                    TextView txtLoc = (TextView)findViewById(R.id.textView);
//                    txtLoc.setText(String.valueOf("Calibration change. Quality: " + quality));
                    Log.e(this.getClass().getName(),String.valueOf("Calibration change. Quality: " + quality));
                    break;
                case IALocationManager.STATUS_AVAILABLE:
                    Log.e(this.getClass().getName(),String.valueOf("onStatusChanged: Available"));
                    break;
                case IALocationManager.STATUS_LIMITED:
                    Log.e(this.getClass().getName(),String.valueOf("onStatusChanged: Limited"));
                    break;
                case IALocationManager.STATUS_OUT_OF_SERVICE:
                    Log.e(this.getClass().getName(),String.valueOf("onStatusChanged: Out of service"));
                    break;
                case IALocationManager.STATUS_TEMPORARILY_UNAVAILABLE:
                    Log.e(this.getClass().getName(),String.valueOf("onStatusChanged: Temporarily unavailable"));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );

        mLocationManager = IALocationManager.create(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        mLocationManager.requestLocationUpdates(IALocationRequest.create(),mLocationListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mLocationManager.removeLocationUpdates(mLocationListener);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationManager.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Handle if any of the permissions are denied, in grantResults
    }
}
