package com.example.admin.tiptrial;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends SampleActivityBase implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,RoutingListener {
    /*** direct api = AIzaSyBBnJigWiu0TUp0IREUsnVoDw3HlliShzE***/
    // Google Map

    final static int REQUEST_LOCATION = 199;
    protected GeoDataClient mGeoDataClient;
    LocationRequest locationRequest;
GPSTracker gps;
    private Location mLocation;
    double latitude, longitude;
    double latitude2, longitude2;
    LocationRequest mLocationRequest;
Boolean searched = false;

    private AutoCompleteTextView start, destination;

    GoogleApiClient mGoogleApiClient;
    Marker to, from;
    LatLng my_loc, latLng1;




    private GoogleMap mMap;
    boolean turned = true;
    private PlaceAutoCompleteAdapter mAdapter;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.orange, R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.primary_dark_material_light};
    private static final LatLngBounds BOUNDS_NEAR = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        start = (AutoCompleteTextView) findViewById(R.id.start);
        destination = (AutoCompleteTextView) findViewById(R.id.destination);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        start.setOnItemClickListener(mAutocompleteClickListener);
        destination.setOnItemClickListener(mAutocompleteClickListener1);
        mAdapter = new PlaceAutoCompleteAdapter(this, mGeoDataClient, BOUNDS_NEAR, null);
        start.setAdapter(mAdapter);
        destination.setAdapter(mAdapter);
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();


        }

        polylines = new ArrayList<>();
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void sendRequest() {


        route();
    }


    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(Main4Activity.this);
        gps = new GPSTracker(Main4Activity.this);
            Log.e("permission", "granted");
            final LocationManager manager = (LocationManager) Main4Activity.this.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("keshav", "Gps not enabled");
                mGoogleApiClient = new GoogleApiClient.Builder(Main4Activity.this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);

                //**************************
                builder.setAlwaysShow(true); //this is the key ingredient
                //**************************

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can initialize location
                                // requests here.
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(
                                            Main4Activity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                });
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {
                Log.e("keshav", "Gps already enabled");
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
if(latitude == 0 && longitude == 0){
    latitude = gps.getLatitude();
    longitude = gps.getLongitude();
}



        LatLngBounds BOUNDS_NEAR = new LatLngBounds(
                new LatLng(latitude-2,longitude-2), new LatLng(latitude+2, longitude+2));
        start.setOnItemClickListener(mAutocompleteClickListener);
        destination.setOnItemClickListener(mAutocompleteClickListener1);
        mAdapter = new PlaceAutoCompleteAdapter(this, mGeoDataClient, BOUNDS_NEAR, null);
        start.setAdapter(mAdapter);
        destination.setAdapter(mAdapter);
       /* if (hasGPSDevice(Main4Activity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionCheck = ContextCompat.checkSelfPermission(Main4Activity.this,
                        Manifest.permission.CAMERA);
                mGoogleApiClient = new GoogleApiClient.Builder(Main4Activity.this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);

                //**************************
                builder.setAlwaysShow(true); //this is the key ingredient
                //**************************

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can initialize location
                                // requests here.
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(
                                            Main4Activity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                });

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Log.e("permission", "granted");
                    locationEnabled_or_Not();

                } else {
                    ActivityCompat.requestPermissions(Main4Activity.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
                    locationEnabled_or_Not();
                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "We cannpt function as you do not have GPS ",
                    Toast.LENGTH_SHORT).show();
        }





        latitude = gps.getLatitude();
        longitude = gps.getLongitude();


        // check if map is created successfully or not

/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            gpsTracker = new GPSTracker(getApplicationContext());
            mLocation = gpsTracker.getLocation();

            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();

        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            gpsTracker = new GPSTracker(getApplicationContext());


            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

        }
        else{
            Toast.makeText(this, "Not able to get Location " , Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();

       initilizeMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (turned) {
            turned = false;
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

            }
        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap = googleMap;
        mMap.clear();
        my_loc = new LatLng(latitude, longitude);
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


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /*** direct api = AIzaSyBBnJigWiu0TUp0IREUsnVoDw3HlliShzE***/
 /*   public void onMapSearch(View view) {
        if (q == 1) {
            search();
        }
    }
*/
    public void search() {
       Toast.makeText(getApplicationContext(), "Clicked ",
                Toast.LENGTH_SHORT).show();
        String location_frm = start.getText().toString();
        String location_to = destination.getText().toString();
        mMap.clear();

        List<Address> addressList = null;
        List<Address> addressList1 = null;
        if (location_frm != null || !location_frm.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location_frm, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Address address = addressList.get(0);
                latitude = address.getLatitude();
                longitude = address.getLongitude();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Enter the from address ",
                        Toast.LENGTH_SHORT).show();
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
            if (location_to != null || !location_to.equals("")) {
                Geocoder geocoder1 = new Geocoder(this);
                try {
                    addressList1 = geocoder1.getFromLocationName(location_to, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Address address1 = addressList1.get(0);
                    latitude2 = address1.getLatitude();
                    longitude2 = address1.getLongitude();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Enter the destination address ",
                            Toast.LENGTH_SHORT).show();
                }
            }
            latLng1 = new LatLng(latitude2, longitude2);
            my_loc = new LatLng(latitude, longitude);
            LatLng my_loc = new LatLng(latitude, longitude);
            from = mMap.addMarker(new MarkerOptions().position(my_loc).title("I'm here...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            to = mMap.addMarker(new MarkerOptions().position(latLng1).title("marker1"));
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

        }
        searched=true;
        route();
    }
/*
@Override
 public void onLocationChanged(Location location)
 {*/
  /*   double lat = location.getLatitude();
     double lng = location.getLongitude();
     my_loc = new LatLng(lat, lng);
     if(firstRefresh)
     {
         mMap.clear();
         //Add Start Marker.
         from = mMap.addMarker(new MarkerOptions().position(my_loc).title("Current Position"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
         firstRefresh = false;
         to = mMap.addMarker(new MarkerOptions().position(latLng1).title("Destination"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
         mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

 /*    }
     else
     {
         from.setPosition(my_loc);
     }*/


    public void route() {
        if (my_loc == null || latLng1 == null) {

        } else {
            /*progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);*/
            try {
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener((RoutingListener) this)
                        .alternativeRoutes(true)
                        .waypoints(my_loc, latLng1)
                        .build();
                routing.execute();

            } catch (Exception e) {

            }

        }
    }


    public void onRoutingFailure(RouteException e) {
        // The Routing request failed

        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        int i, store_route = 0;
        double a = 0;

        polylines = new ArrayList<>();
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }


        //add route(s) to the map.
        for (i = 0; i < route.size(); i++) {
            if (i != 0) {
                if (route.get(i).getDurationValue() <= a) {
                    a = route.get(i).getDurationValue();
                    store_route = i;
                }
            } else {
                a = route.get(i).getDurationValue();
                store_route = i;
            }
        }
        for (i = 0; i < route.size(); i++) {
            if (i == store_route) {
                //In case of more than 5 alternative routes
                int colorIndex = i % COLORS.length;

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);
                Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();

            }
            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onRoutingCancelled() {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }


    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public void sendRequest(View view) {
        route();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);


            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener1
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);


            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    public void onLocationChanged(Location location) {
    /*    if(searched){
        mLocation = gpsTracker.getLocation();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        my_loc = new LatLng(latitude, longitude);
            Toast.makeText(this, "Loc changed " , Toast.LENGTH_SHORT).show();
        route();

    }*/}

    public void onMapSearch(View view) {

        search();
    }

    public void mylocation(View view) {
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
    }

   /* public boolean hasGPSDevice(Context context) {
        Log.e("keshav", "hasGPS Step 1 Pass...........");
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null){
            Log.e("keshav", "no gps...........");
            return false;}
        final List<String> providers = mgr.getAllProviders();
        if (providers == null){
            Log.e("keshav", "no gps...........");
            return false;}
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    // TODO When Location not enabled show popup
    // TODO When Location already Enabled CAll GPS Tracker
    private void locationEnabled_or_Not() {
        Log.e("keshav", "locationEnabled_or_Not Step 2 Pass...........");
        final LocationManager manager = (LocationManager) Main4Activity.this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Main4Activity.this)) {
            Log.e("keshav", "Gps not enabled");
            callLocationDialog();
        } else {
            Log.e("keshav", "Gps already enabled");
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {

                callLocationDialog();
            }          // TODO When Gps already enabled call direct GPS Tracker
        }
    }

    //TODO Step 3 when hasGPSDevice return true

    private void callLocationDialog() {

        Log.e("keshav", "callLocationDialog Step 3 Popup called ...........");

        mGoogleApiClient = new GoogleApiClient.Builder(Main4Activity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    Main4Activity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

    }

    //TODO Step 4 when location on fetch GPS Tracker Through Latitude Longitude

*/
}