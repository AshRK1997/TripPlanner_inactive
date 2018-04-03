package com.example.admin.tiptrial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Main3Activity extends FragmentActivity implements OnMapReadyCallback {

    // Google Map
    private int q=0;
    private GoogleMap googleMap;
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    double latitude2, longitude2;
    Marker to,from;
    private GoogleMap mMap;
    Boolean mLocationPermissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
q++;
if(q==1){

    try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }}

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {

            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
            mapFragment.getMapAsync(Main3Activity.this);

            // check if map is created successfully or not


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            gpsTracker = new GPSTracker(getApplicationContext());
            mLocation = gpsTracker.getLocation();

            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            gpsTracker = new GPSTracker(getApplicationContext());
            mLocation = gpsTracker.getLocation();

            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(q==1){
        mMap = googleMap;
        LatLng my_loc = new LatLng(latitude, longitude);
        from = mMap.addMarker(new MarkerOptions().position(my_loc).title("I'm here...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_loc, 13));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



            mMap.animateCamera(CameraUpdateFactory.zoomIn());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(my_loc)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+latitude2+","+longitude2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER );
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent);*/
          /*  mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                // Return null here, so that getInfoContents() is called next.
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    // Inflate the layouts for the info window, title and snippet.
                    View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                    TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                    title.setText(marker.getTitle());

                    TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                    snippet.setText(marker.getSnippet());

                    return infoWindow;
                }
            });*/



        }}

    public void onMapSearch(View view) {
        if(q==1) {
            search();
        }
    }
    public  void search()
    {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        mMap.clear();
        List<Address> addressList = null;

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
            from = mMap.addMarker(new MarkerOptions().position(my_loc).title("I'm here...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
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

       /*     Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener((RoutingListener) this)
                    .waypoints(my_loc,latLng1)
                    .build();
            routing.execute();*/

           /* getDirection(my_loc,latLng1);*/
}
    }
   public void getDirection(final LatLng a, final LatLng b){
/*
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
                });*/

    }

}