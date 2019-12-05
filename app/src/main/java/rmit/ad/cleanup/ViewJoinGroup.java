package rmit.ad.cleanup;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;

public class ViewJoinGroup extends FragmentActivity implements
        OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    Button joinBtn, joineeBtn;
    String key;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.join_cleanup_view);

        TextView titleTxt = findViewById(R.id.titleTxt);
        TextView dateTxt = findViewById(R.id.projectDate);
        TextView locationTxt = findViewById(R.id.projectLocation);
        TextView contactTxt = findViewById(R.id.projectContact);
        joinBtn = findViewById(R.id.joinBtn);
        joineeBtn = findViewById(R.id.showJoineeBtn);


        try {
            System.out.println(getIntent().getExtras().getString("siteInfo"));
            final JSONObject siteInfoJson = new JSONObject(getIntent().getExtras().getString("siteInfo"));

            String title = "Cleanup Site: " + siteInfoJson.get("title");
            titleTxt.setText(title);

            String dateStr = "Date      ;  " + siteInfoJson.getString("when");
            String locationStr = "Location  ;  " + siteInfoJson.getString("where");
            String contactStr = "Contact  ;  " + siteInfoJson.getString("contact");
            key = siteInfoJson.getString("key");
            dateTxt.setText(dateStr);
            contactTxt.setText(contactStr);
            locationTxt.setText(locationStr);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.locationMap);


            Double latitude = Double.valueOf(siteInfoJson.getString("latitude"));
            System.out.println("latitude: " +latitude);
            Double longitude = Double.valueOf(siteInfoJson.getString("longitude"));
            System.out.println("longitude: " +longitude);
            latLng = new LatLng(latitude, longitude);

            mapFragment.getMapAsync(this);


        } catch (Exception ex) {
            Log.e("joinDialog", "Error occurred while parsing the json String to JsonObject");
        }

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JoinInfoDialog cleanupJoinDialog = new JoinInfoDialog();
                Bundle args = new Bundle();
                args.putString("key", key);
                cleanupJoinDialog.setArguments(args);
                cleanupJoinDialog.show(getSupportFragmentManager(),"cleanup_join_dialog");
            }
        });

        joineeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewJoinGroup.this, SiteJoineeList.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(latLng == null) {
            latLng = new LatLng(10.730183, 106.694264);
        }

        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition newCamPos = new CameraPosition(latLng,15.5f,
                mMap.getCameraPosition().tilt,
                mMap.getCameraPosition().bearing
        );
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);


        mMap.getUiSettings().setZoomControlsEnabled(true);


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}