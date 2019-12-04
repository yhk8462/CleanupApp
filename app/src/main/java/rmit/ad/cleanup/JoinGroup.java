package rmit.ad.cleanup;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class JoinGroup extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{
    private GoogleMap mMap;
    private DatabaseReference mUsers;

    SearchView searchView;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_cleanup_map);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchView = findViewById(R.id.sv_location);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                System.out.println(location);
                List<Address> addressList = null;

                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
                    try{
                        addressList = geocoder.getFromLocationName(location, 1);

                        Address searchAddress = addressList.get(0);
                        System.out.println("address: " + searchAddress.getLatitude()+searchAddress.getLongitude());
                        LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mUsers= FirebaseDatabase.getInstance().getReference("Cleanup Sites");
        mUsers.push().setValue(marker);

        Button btnreturn = findViewById(R.id.btnReturn);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--- inside menu");
                Intent menu = new Intent(JoinGroup.this, menu.class);
                startActivity(menu);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Default address
        LatLng rmit =  new LatLng( 10.730183, 106.694264);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rmit));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rmit, 15));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    ClusterMarker user = s.getValue(ClusterMarker.class);
                    String key = s.getKey();
                    //Toast.makeText(JoinGroup.this,""+key,Toast.LENGTH_SHORT).show();
                    LatLng location=new LatLng(user.latitude,user.longitude);

                    JSONObject siteInfo = new JSONObject();
                    try{
                        siteInfo.put("title", user.getTitle());
                        siteInfo.put("latitude", user.getLatitude());
                        siteInfo.put("longitude", user.getLongitude());
                        siteInfo.put("when", user.getWhen());
                        siteInfo.put("where", user.getWhere());
                        siteInfo.put("contact", user.getContact());
                    } catch (Exception ex) {

                    }

                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(key)
                            .snippet(siteInfo.toString())
                            )
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        CleanupJoinDialog cleanupJoinDialog = new CleanupJoinDialog();
        Bundle args = new Bundle();
        args.putString("key", marker.getTitle());
        args.putString("info",marker.getSnippet());

        cleanupJoinDialog.setArguments(args);
        cleanupJoinDialog.show(getSupportFragmentManager(),"cleanup_join_dialog");
        return true;
    }
}