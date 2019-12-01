package rmit.ad.cleanup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

public class cInfo extends AppCompatActivity {
    private DatabaseReference mDatabase;

    EditText mtitle, mwhere, mwhen, mcontact, mlatitude, mlongitude;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cinfo);
        mDatabase= FirebaseDatabase.getInstance().getReference("Cleanup Sites");

        final Button create = findViewById(R.id.btnCreate);

        mtitle = findViewById(R.id.edtTitle);
        mwhere = findViewById(R.id.edtWhere);
        mwhen = findViewById(R.id.edtWhen);
        mcontact = findViewById(R.id.edtContact);
        mlatitude = findViewById(R.id.edtLat);
        mlongitude = findViewById(R.id.edtLng);
        final String strTitle = mtitle.getText().toString();
        final String strWhere   = mwhere.getText().toString();
        final String strWhen = mwhen.getText().toString();
        final String strContact = mcontact.getText().toString();
        if(strTitle.isEmpty()){
            mtitle.setError("Error no input");
        }
        if(strWhere.isEmpty()){
            mwhere.setError("Error no input");
        }
        if(strWhen.isEmpty()){
            mwhen.setError("Error no input");
        }
        if(strContact.isEmpty()){
            mcontact.setError("Error no input");
        }
        Bundle extras = getIntent().getExtras();
        double sLatitude = extras.getDouble("sLatitude");
        double sLongitude = extras.getDouble("sLongitude");
        String sLat = Double.toString(sLatitude);
        String sLong = Double.toString(sLongitude);
        mlatitude.setText(sLat);
        mlongitude.setText(sLong);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtitle.getText().toString().isEmpty()){
                    return;
                }
                if(mwhere.getText().toString().isEmpty()){
                    return;
                }
                if(mwhen.getText().toString().isEmpty()){
                    return;
                }
                if(mcontact.getText().toString().isEmpty()){
                    return;
                }
                double latitude= Double.parseDouble(mlatitude.getText().toString().trim());
                double longitude= Double.parseDouble(mlongitude.getText().toString().trim());
                String title = mtitle.getText().toString().trim();
                String where = mwhere.getText().toString().trim();
                String when = mwhen.getText().toString().trim();
                String contact = mcontact.getText().toString().trim();

                String id=  mDatabase.push().getKey();
                ClusterMarker cleanup = new ClusterMarker(title,where,when,contact,latitude,longitude);
                //databaseReference
                mDatabase.child(id).setValue(cleanup);

                Toast.makeText(cInfo.this,"Saved",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(cInfo.this, cMap.class);
                startActivity(intent);
            }
        });
    }


}