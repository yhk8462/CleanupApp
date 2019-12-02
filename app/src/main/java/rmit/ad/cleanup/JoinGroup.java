package rmit.ad.cleanup;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGroup extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener{
    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmap2);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;
        mUsers= FirebaseDatabase.getInstance().getReference("Cleanup Sites");
        mUsers.push().setValue(marker);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(key)
                            .snippet(user.getTitle()+"\n"+"Where: "+user.getWhen()+"\n"+ "When: " + user.getWhere()+"\n" +"Contact: "+user.getContact())
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