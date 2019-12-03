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

public class newInfo extends AppCompatActivity {
    private DatabaseReference mDatabase;

    EditText nName,nContact;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newinfo);
        Bundle extras = getIntent().getExtras();
        String key = (String) extras.get("key");
        Toast.makeText(newInfo.this,""+key,Toast.LENGTH_SHORT).show();

        mDatabase= FirebaseDatabase.getInstance().getReference("Cleanup Sites").child(key).child("Users");

        final Button create = findViewById(R.id.btnCreate);

        nName = findViewById(R.id.edtName);
        nContact = findViewById(R.id.edtPhone);

        final String strName = nName.getText().toString();
        final String strContact = nContact.getText().toString();
        if(strName.isEmpty()){
            nName.setError("Error no input");
        }
        if(strContact.isEmpty()){
            nContact.setError("Error no input");
        }


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nName.getText().toString().isEmpty()){
                    return;
                }
                if(nContact.getText().toString().isEmpty()){
                    return;
                }


                String name = nName.getText().toString().trim();
                String contact = nContact.getText().toString().trim();

                String id=  mDatabase.push().getKey();
                mContact info = new mContact(name,contact);
                //databaseReference
                mDatabase.child(id).setValue(info);

                Intent intent = new Intent(newInfo.this, JoinGroup.class);
                startActivity(intent);
            }
        });
    }


}