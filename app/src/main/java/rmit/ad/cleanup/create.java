package rmit.ad.cleanup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.analytics.FirebaseAnalytics;

public class create extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText name, date, gender, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        mAuth = FirebaseAuth.getInstance();

        final Button create = findViewById(R.id.btnCreate);

        name = findViewById(R.id.edtName);
        date = findViewById(R.id.edtAge);
        gender = findViewById(R.id.edtGender);
        phone = findViewById(R.id.edtPhone);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create(); //Email and password auth

                List cleaningSite = new List(name.getText().toString(), date.getText().toString(), gender.getText().toString(), phone.getText().toString());
                addSite(cleaningSite);
                Toast.makeText(create.this, "Account Created", Toast.LENGTH_SHORT).show();
                alert();
            }
        });
    }

    public void create() {
        String NewEmail, NewPassword;

        EditText mEmail2 = findViewById(R.id.edtEmail2);
        EditText mPassword2 = findViewById(R.id.edtPassword2);

        NewEmail = mEmail2.getText().toString();
        NewPassword = mPassword2.getText().toString();

        mAuth.createUserWithEmailAndPassword(NewEmail, NewPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent intent = new Intent(create.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(create.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void addSite(List site) {
        db.collection("List")
                .add(site)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(create.this, "Add success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(create.this, "Add failure", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Account Created").setMessage("You can now Login");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
    }
}