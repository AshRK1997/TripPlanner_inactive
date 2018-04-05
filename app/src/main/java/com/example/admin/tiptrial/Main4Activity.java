package com.example.admin.tiptrial;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,RoutingListener {
    /*** direct api = AIzaSyBBnJigWiu0TUp0IREUsnVoDw3HlliShzE***/
    // Google Map
    int q=0;
    private ProgressDialog progressDialog;
    private GoogleMap googleMap;
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    double latitude2, longitude2;
    LocationRequest mLocationRequest;
    int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Marker to,from;
    LatLng my_loc,latLng1;
    private boolean firstRefresh = true;
    private GoogleMap mMap;
    boolean turned = true;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.orange,R.color.colorPrimary,R.color.colorAccent,R.color.colorPrimaryDark,R.color.primary_dark_material_light};

   /* AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;*/

    ArrayList markerPoints= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        q++;
        if(q==1){if (!CheckGooglePlayServices()) {
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

    }}

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(Main4Activity.this);

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
    /*private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBrfbhEkLUgWlnXAmSHlBoIW5QZIyv3AiI");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(turned){
            turned=false;
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

            }
        } else {
            buildGoogleApiClient();

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
    public void onMapSearch(View view) {
        if(q==1) {
            search();
        }
    }
    public  void search()
    {
        EditText locationSearch = (EditText) findViewById(R.id.editText2);
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
            latLng1 = new LatLng(latitude2, longitude2);
            my_loc = new LatLng(latitude,longitude);
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
            route();
         /*   Main4Activity.this.getRoutingPath();*/
           /* my_direct md = new my_direct();
            Document doc = md.getDocument(my_loc, latLng1,
                    my_direct.MODE_DRIVING);
            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.RED);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            Polyline polylin = mMap.addPolyline(rectLine);*/
       /*     Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener((RoutingListener) this)
                    .waypoints(my_loc,latLng1)
                    .build();
            routing.execute();*/

           /* getDirection(my_loc,latLng1);*/
        }
    }
 /*   public void getDirection(final LatLng a, final LatLng b) {

        GoogleDirection.withServerKey("AIzaSyAGRYhhtVz3LmzcmfB2KAKlPeWhANcT6LA")
                .from(a)
                .to(b)
                .avoid(AvoidType.FERRIES)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            // Do something
                        } else {
                            // Do something
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        getDirection(a, b);
                    }
                });
    }*/
@Override
 public void onLocationChanged(Location location)
 {
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
        /* Main4Activity.this.getRoutingPath();*/
 /*    }
     else
     {
         from.setPosition(my_loc);
     }*/
 }


    public void route()
    {
        if(my_loc==null || latLng1==null)
        {

        }
        else
        {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);
            try {
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener((RoutingListener) this)
                        .alternativeRoutes(true)
                        .waypoints(my_loc, latLng1)
                        .build();
                routing.execute();

            }
            catch (Exception e)
            {

            }

        }
    }



    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        progressDialog.dismiss();
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onRoutingStart() {
        progressDialog.dismiss();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        progressDialog.dismiss();
        polylines = new ArrayList<>();
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }


        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onRoutingCancelled() {
        progressDialog.dismiss();
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
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+latitude2+","+longitude2);
        googleDirectionsUrl.append("&key="+"AIzaSyBrfbhEkLUgWlnXAmSHlBoIW5QZIyv3AiI");

        return googleDirectionsUrl.toString();
    }
   /* @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }*/

}


