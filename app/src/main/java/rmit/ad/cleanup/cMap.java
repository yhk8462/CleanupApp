package rmit.ad.cleanup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class cMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,LocationListener {

    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    public DatabaseReference mSites,mDatabase;

    private static final String TAG = "cMap";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private EditText mSearchText;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmap);

        mSearchText = (EditText) findViewById(R.id.input_search);
        getDeviceLocation();
        getLocationPermission();
        init();

        ChildEventListener mChildEventListener;

        mSites= FirebaseDatabase.getInstance().getReference().child("Cleanup Sites");

        mSites.push().setValue(marker);


        Button button = findViewById(R.id.btnRMIT);
        Button btnreturn = findViewById(R.id.btnReturn);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(cMap.this, menu.class);
                startActivity(menu);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            //Get markers from firebase


            googleMap.setOnMarkerClickListener(this);
            mMap = googleMap;
            mSites.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()){
                        ClusterMarker mark = s.getValue(ClusterMarker.class);
                        String key = s.getKey();

                        LatLng location=new LatLng(mark.latitude, mark.longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(mark.getTitle())
                                .snippet(key)


                        ).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                        Log.d(TAG, "Get marker location");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });

            init();
            //add marker if clicked on map
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(cMap.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you want to create a cleanup?"+ "Your coordinates are:"+latLng)
                            .setNegativeButton( "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(cMap.this, cInfo.class);
                                    intent.putExtra("sLatitude", latLng.latitude);
                                    intent.putExtra("sLongitude", latLng.longitude);
                                    startActivity(intent);
                                }
                            })
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            });
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.getSnippet();
        String key = marker.getSnippet();

        ShowUpdateDialog(key);
        return true;
    }
    private boolean updateData(String key, String title, String location, String date, String contact, double latitude, double longitude ){
        mDatabase= FirebaseDatabase.getInstance().getReference("Cleanup Sites").child(key);
        ClusterMarker updateInfo = new ClusterMarker(title,location,date,contact,latitude,longitude);
        mDatabase.setValue(updateInfo);
        return true;
    }
    private void ShowUpdateDialog(final String key){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update, null);
        dialogBuilder.setView(dialogView);

        final EditText uTitle = (EditText) dialogView.findViewById(R.id.edtTitle);
        final EditText uLocation = (EditText) dialogView.findViewById(R.id.edtWhen);
        final EditText uDate = (EditText) dialogView.findViewById(R.id.edtWhere);
        final EditText uLatitude = (EditText) dialogView.findViewById(R.id.edtLat);
        final EditText uLongitude = (EditText) dialogView.findViewById(R.id.edtLng);

        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnCreate);

        mDatabase= FirebaseDatabase.getInstance().getReference("Cleanup Sites").child(key);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClusterMarker data = dataSnapshot.getValue(ClusterMarker.class);
                double lat = data.getLatitude();
                double lng = data.getLongitude();
                String sLat = Double.toString(lat);
                String sLong = Double.toString(lng);

                uTitle.setText(data.getTitle());
                uLocation.setText(data.getWhere());
                uDate.setText(data.getWhen());
                uLatitude.setText(sLat);
                uLongitude.setText(sLong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialogBuilder.setTitle("Update info");
        dialogBuilder.setMessage("Warning!! This will reset Users joined");
        final AlertDialog alertDialog = dialogBuilder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(300, 300);
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude= Double.parseDouble(uLatitude.getText().toString().trim());
                double longitude= Double.parseDouble(uLongitude.getText().toString().trim());
                String title = uTitle.getText().toString().trim();
                String location = uLocation.getText().toString().trim();
                String date = uDate.getText().toString().trim();
                String contact = "0762002087";
                updateData(key,title, location, date, contact, latitude, longitude );
                alertDialog.dismiss();
            }
        });



    }


    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
        HideKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(cMap.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
            HideKeyboard();
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(cMap.this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            //rmit 10.7295612 106.6937702 currentLocation.getLatitude() currentLocation.getLongitude()
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,"My location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(cMap.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
        HideKeyboard();

    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        HideKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(cMap.this);
    }




    //----------------------------------------------------------------------------------------------------------------------------------------- Below are Permissions




    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    private void HideKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
