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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
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
    protected LatLng start;
    protected LatLng end;
    private static final String LOG_TAG = "MyActivity";
    int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Marker to,from;
    LatLng my_loc,latLng1;
    AutoCompleteTextView starting,destination;
    ImageView send;
 /*   @InjectView(R.id.start)

    @InjectView(R.id.destination)

    @InjectView(R.id.send)*/

    private boolean firstRefresh = true;
    private GoogleMap mMap;
    boolean turned = true;
    private PlaceAutoCompleteAdapter mAdapter;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.orange,R.color.colorPrimary,R.color.colorAccent,R.color.colorPrimaryDark,R.color.primary_dark_material_light};

   /* AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
      /*  starting = (AutoCompleteTextView)findViewById(R.id.start);
        destination = (AutoCompleteTextView)findViewById(R.id.destination);
        send = (ImageView)findViewById(R.id.send);*/
        q++;
        if(q==1){if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();


        }
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            MapsInitializer.initialize(this);
            mGoogleApiClient.connect();

            polylines = new ArrayList<>();
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        /*starting.setAdapter(mAdapter);
        destination.setAdapter(mAdapter);
        starting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
              /*  PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                        start=place.getLatLng();
                    }
                });

            }
        });
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
             /*   PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                        end=place.getLatLng();
                    }
                });

            }
        });
        starting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int startNum, int before, int count) {
                if (start != null) {
                    start = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(end!=null)
                {
                    end=null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

    }


    public void sendRequest()
    {



route();
    }


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
        LatLng my_loc = new LatLng(latitude, longitude);
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

    public void sendRequest(View view) {
    route();
    }

 /*
  GetDirections.java
   package com.example.admin.tiptrial;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.List;

*/
/*
    public class GetDirectionsData extends AsyncTask<Object,String,String> {

        GoogleMap mMap;
        String url;
        String googleDirectionsData;
        String duration, distance;
        LatLng latLng;
        @Override
        protected String doInBackground(Object... objects) {
            mMap = (GoogleMap)objects[0];
            url = (String)objects[1];
            latLng = (LatLng)objects[2];



            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googleDirectionsData = downloadUrl.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return googleDirectionsData;
        }

        @Override
        protected void onPostExecute(String s) {

            String[] directionsList;
            DataParser parser = new DataParser();
            directionsList = parser.parseDirections(s);
            displayDirection(directionsList);

        }

        public void displayDirection(String[] directionsList)
        {

            int count = directionsList.length;
            for(int i = 0;i<count;i++)
            {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.RED);
                options.width(10);
                options.addAll(PolyUtil.decode(directionsList[i]));

                mMap.addPolyline(options);
            }
        }






    }

*/

/*
GetNearbyPlacesData.java
package com.example.admin.tiptrial;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;




    public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... objects) {
            mMap = (GoogleMap)objects[0];
            url = (String)objects[1];

            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googlePlacesData = downloadUrl.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String s) {
            List<HashMap<String, String>> nearbyPlaceList = null;
            DataParser parser = new DataParser();
            nearbyPlaceList = parser.parse(s);
            showNearbyPlaces(nearbyPlaceList);
        }

        private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList)
        {
            for(int i = 0;i<nearbyPlaceList.size() ; i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String , String> googlePlace = nearbyPlaceList.get(i);
                Log.d("onPostExecute","Entered into showing locations");

                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                double lat = Double.parseDouble( googlePlace.get("lat") );
                double lng = Double.parseDouble( googlePlace.get("lng"));

                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName +" : "+ vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));



            }


        }

    }


 */
/*
DownloadUrl
package com.example.admin.tiptrial;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

*/
/*

    public class DownloadUrl {

        public String readUrl(String myUrl) throws IOException
        {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(myUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();

                String line = "";
                while((line = br.readLine()) != null)
                {
                    sb.append(line);

                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());

                br.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(inputStream != null)
                    inputStream.close();
                urlConnection.disconnect();
            }

            Log.d("data downlaod",data);
            return data;

        }
    }
 */
/*
DataParser
package com.example.admin.tiptrial;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class DataParser {


    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance ="";


        try {

            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration" , duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return googleDirectionsMap;
    }


    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        Log.d("getPlace", "Entered");


        try {
            if(!googlePlaceJson.isNull("name"))
            {

                placeName = googlePlaceJson.getString("name");

            }
            if( !googlePlaceJson.isNull("vicinity"))
            {
                vicinity = googlePlaceJson.getString("vicinity");

            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name" , placeName);
            googlePlacesMap.put("vicinity" , vicinity);
            googlePlacesMap.put("lat" , latitude);
            googlePlacesMap.put("lng" , longitude);
            googlePlacesMap.put("reference" , reference);


            Log.d("getPlace", "Putting Places");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }



    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;
        Log.d("Places", "getPlaces");

        for(int i = 0;i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;

    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");

            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJson )
    {
        int count = googleStepsJson.length();
        String[] polylines = new String[count];

        for(int i = 0;i<count;i++)
        {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    public String getPath(JSONObject googlePathJson)
    {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }



}

 */
/*
my_direct.java
package com.example.admin.tiptrial;



import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

    public class my_direct {
        public final static String MODE_DRIVING = "driving";
        public final static String MODE_WALKING = "walking";

        public my_direct() {
        }

        public Document getDocument(LatLng start, LatLng end, String mode) {
            String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                    + "origin=" + start.latitude + "," + start.longitude
                    + "&destination=" + end.latitude + "," + end.longitude
                    + "&sensor=false&units=metric&mode=driving";
            Log.d("url", url);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse response = httpClient.execute(httpPost, localContext);
                InputStream in = response.getEntity().getContent();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document doc = builder.parse(in);
                return doc;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getDurationText(Document doc) {
            try {

                NodeList nl1 = doc.getElementsByTagName("duration");
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                Log.i("DurationText", node2.getTextContent());
                return node2.getTextContent();
            } catch (Exception e) {
                return "0";
            }
        }

        public int getDurationValue(Document doc) {
            try {
                NodeList nl1 = doc.getElementsByTagName("duration");
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "value"));
                Log.i("DurationValue", node2.getTextContent());
                return Integer.parseInt(node2.getTextContent());
            } catch (Exception e) {
                return -1;
            }
        }

        public String getDistanceText(Document doc) {
        /*
         * while (en.hasMoreElements()) { type type = (type) en.nextElement();
         *
         * }
         */

      /*      try {
                NodeList nl1;
                nl1 = doc.getElementsByTagName("distance");

                Node node1 = nl1.item(nl1.getLength() - 1);
                NodeList nl2 = null;
                nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "value"));
                Log.d("DistanceText", node2.getTextContent());
                return node2.getTextContent();
            } catch (Exception e) {
                return "-1";
            }

        /*
         * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
         * doc.getElementsByTagName("distance");
         *
         * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
         * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
         * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
         * node2.getTextContent()); return node2.getTextContent(); } else return
         * "-1";} else return "-1";
         */
        }

   /*     public int getDistanceValue(Document doc) {
            try {
                NodeList nl1 = doc.getElementsByTagName("distance");
                Node node1 = null;
                node1 = nl1.item(nl1.getLength() - 1);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "value"));
                Log.i("DistanceValue", node2.getTextContent());
                return Integer.parseInt(node2.getTextContent());
            } catch (Exception e) {
                return -1;
            }
        /*
         * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
         * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
         * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
         * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
         * node2.getTextContent()); return
         * Integer.parseInt(node2.getTextContent()); } else return 0;
         */
      /*  }

        public String getStartAddress(Document doc) {
            try {
                NodeList nl1 = doc.getElementsByTagName("start_address");
                Node node1 = nl1.item(0);
                Log.i("StartAddress", node1.getTextContent());
                return node1.getTextContent();
            } catch (Exception e) {
                return "-1";
            }

        }

        public String getEndAddress(Document doc) {
            try {
                NodeList nl1 = doc.getElementsByTagName("end_address");
                Node node1 = nl1.item(0);
                Log.i("StartAddress", node1.getTextContent());
                return node1.getTextContent();
            } catch (Exception e) {
                return "-1";
            }
        }
        public String getCopyRights(Document doc) {
            try {
                NodeList nl1 = doc.getElementsByTagName("copyrights");
                Node node1 = nl1.item(0);
                Log.i("CopyRights", node1.getTextContent());
                return node1.getTextContent();
            } catch (Exception e) {
                return "-1";
            }

        }

        public ArrayList<LatLng> getDirection(Document doc) {
            NodeList nl1, nl2, nl3;
            ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
            nl1 = doc.getElementsByTagName("step");
            if (nl1.getLength() > 0) {
                for (int i = 0; i < nl1.getLength(); i++) {
                    Node node1 = nl1.item(i);
                    nl2 = node1.getChildNodes();

                    Node locationNode = nl2
                            .item(getNodeIndex(nl2, "start_location"));
                    nl3 = locationNode.getChildNodes();
                    Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                    double lat = Double.parseDouble(latNode.getTextContent());
                    Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                    double lng = Double.parseDouble(lngNode.getTextContent());
                    listGeopoints.add(new LatLng(lat, lng));

                    locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                    nl3 = locationNode.getChildNodes();
                    latNode = nl3.item(getNodeIndex(nl3, "points"));
                    ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                    for (int j = 0; j < arr.size(); j++) {
                        listGeopoints.add(new LatLng(arr.get(j).latitude, arr
                                .get(j).longitude));
                    }

                    locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                    nl3 = locationNode.getChildNodes();
                    latNode = nl3.item(getNodeIndex(nl3, "lat"));
                    lat = Double.parseDouble(latNode.getTextContent());
                    lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                    lng = Double.parseDouble(lngNode.getTextContent());
                    listGeopoints.add(new LatLng(lat, lng));
                }
            }

            return listGeopoints;
        }

        private int getNodeIndex(NodeList nl, String nodename) {
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeName().equals(nodename))
                    return i;
            }
            return -1;
        }

        private ArrayList<LatLng> decodePoly(String encoded) {
            ArrayList<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
                poly.add(position);
            }
            return poly;
        }
    }

 */



