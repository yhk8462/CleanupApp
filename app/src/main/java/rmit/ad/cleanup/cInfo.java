package rmit.ad.cleanup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class cInfo extends AppCompatActivity {
    private DatabaseReference mDatabase;

    EditText mtitle, mwhere, mwhen, mcontact, mlatitude, mlongitude;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cinfo);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Cleanup SItes");

        final Button create = findViewById(R.id.btnCreate);

        mtitle = findViewById(R.id.edtTitle);
        mwhere = findViewById(R.id.edtWhere);
        mwhen = findViewById(R.id.edtWhen);
        mcontact = findViewById(R.id.edtContact);
        mlatitude = findViewById(R.id.edtLat);
        mlongitude = findViewById(R.id.edtLng);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude= Double.parseDouble(mlatitude.getText().toString().trim());
                double longitude= Double.parseDouble(mlongitude.getText().toString().trim());
                String title = mtitle.getText().toString().trim();
                String where = mwhere.getText().toString().trim();
                String when = mwhen.getText().toString().trim();
                String contact = mcontact.getText().toString().trim();


                ClusterMarker cleanup = new
                        ClusterMarker(title,where,when,contact,latitude,longitude);
                mDatabase.child("Cleanup Sites").setValue(cleanup);

                addSite(cleanup);
                Toast.makeText(cInfo.this,"Saved",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(cInfo.this, cMap.class);
                startActivity(intent);
            }
        });
    }

    public void addSite(ClusterMarker cmarker) {
        db.collection("Cleanup Sites")
                .add(cmarker)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(cInfo.this, "Add success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(cInfo.this, "Add failure", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}