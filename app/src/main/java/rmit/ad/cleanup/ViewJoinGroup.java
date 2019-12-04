package rmit.ad.cleanup;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class ViewJoinGroup extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.join_cleanup_view);

        TextView titleTxt = findViewById(R.id.titleTxt);
        TextView dateTxt = findViewById(R.id.projectDate);
        TextView locationTxt = findViewById(R.id.projectLocation);

        try {
            System.out.println(getIntent().getExtras().getString("siteInfo"));
            JSONObject siteInfoJson = new JSONObject(getIntent().getExtras().getString("siteInfo"));

            String title = "Cleanup Site: " + siteInfoJson.get("title");
            titleTxt.setText(title);

            dateTxt.setText(siteInfoJson.getString("when"));
            locationTxt.setText(siteInfoJson.getString("where"));


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.locationMap);


            Double latitude = Double.valueOf(siteInfoJson.getString("latitude"));
            Double longitude = Double.valueOf(siteInfoJson.getString("longitude"));
            LatLng latLng = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            mMap.getUiSettings().setZoomControlsEnabled(false);

            mapFragment.getMapAsync(this);
        } catch (Exception ex) {
            Log.e("joinDialog", "Error occurred while parsing the json String to JsonObject");
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}