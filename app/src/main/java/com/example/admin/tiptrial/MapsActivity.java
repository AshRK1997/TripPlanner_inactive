/*package com.example.admin.tiptrial;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
*//*
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    GPSTracker gps;
    private Location mLocation;
    double latitude, longitude;
    double latitude2, longitude2;

    Marker to,from;
    Location location;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);*/
    /*    tv = (TextView)findViewById(R.id.textView2);
       /* if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            sendBroadcast(intent);
        }*/

   /*     mapfunction();*/
       /* gps = new GPSTracker(MapsActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }*/
/*        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

   public void mapfunction() {
       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
               == PackageManager.PERMISSION_GRANTED) {


           gpsTracker = new GPSTracker(getApplicationContext());
           mLocation = gpsTracker.getLocation();

           latitude = mLocation.getLatitude();
           longitude = mLocation.getLongitude();
       }

       }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng my_loc = new LatLng(latitude, longitude);
        from = mMap.addMarker(new MarkerOptions().position(my_loc).title("I'm here...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_loc, 13));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(my_loc)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }




  public void onMapSearch(View view) {
       search();
    }
public  void search()
{
    EditText locationSearch = (EditText) findViewById(R.id.editText);
    String location = locationSearch.getText().toString();
mMap.clear();
    List<Address>addressList = null;

    if (location != null || !location.equals("")) {
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Address address = addressList.get(0);
            latitude2 = address.getLatitude();
            longitude2 = address.getLongitude();

        }
        catch(Exception e)
        {
            search();
        }
        LatLng latLng1 = new LatLng(latitude2, longitude2);
        LatLng my_loc = new LatLng(latitude,longitude);
       /* LatLng my_loc = new LatLng(latitude, longitude);*/
   /*     from = mMap.addMarker(new MarkerOptions().position(my_loc).title("I'm here...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        to =   mMap.addMarker(new MarkerOptions().position(latLng1).title("marker1"));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(to.getPosition());
        builder.include(from.getPosition());
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
        mMap.animateCamera(cu);
        getDirection(my_loc,latLng1);
        continousrch();
        Toast.makeText(MapsActivity.this, "end of search", Toast.LENGTH_SHORT).show();
    }
}
public void continousrch(){
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            search();
            Toast.makeText(MapsActivity.this, "running search", Toast.LENGTH_SHORT).show();
            tv.setText(latitude+longitude+" ");
        }
    }, 5000);
}
public void getDirection(final LatLng a, final LatLng b){*/
    /*
    Toast.makeText(MapsActivity.this, "inside direction", Toast.LENGTH_SHORT).show();
    GoogleDirection.withServerKey("AIzaSyAGRYhhtVz3LmzcmfB2KAKlPeWhANcT6LA")
            .from(a)
            .to(b)
            .avoid(AvoidType.FERRIES)
            .transportMode(TransportMode.DRIVING)
            .execute(new DirectionCallback() {
                @Override
                public void onDirectionSuccess(Direction direction, String rawBody) {
                    if(direction.isOK()) {
                        // Do something
                    } else {
                        // Do something
                    }
                }

                @Override
                public void onDirectionFailure(Throwable t) {
                    getDirection(a,b);
                }
            });

}
*//*
}}*/