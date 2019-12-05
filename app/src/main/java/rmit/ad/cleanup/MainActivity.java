package rmit.ad.cleanup;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private FirebaseAuth mAuth;
    private String fromPage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromPage = getIntent().getExtras().getString("from");

        mAuth = FirebaseAuth.getInstance();

        Button login = findViewById(R.id.btnLogin);
        final Button register = findViewById(R.id.btnRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 =new Intent(MainActivity.this,create.class);
                startActivity(intent2);
            }
        });
    }

    public void login() {
        String email, password;
        EditText mEmail = findViewById(R.id.edtEmail);
        EditText mPassword = findViewById(R.id.edtPassword);
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                            if(fromPage != null && fromPage.equals("JOIN")){
                                Intent intent =new Intent(MainActivity.this,JoinGroup.class);
                                startActivity(intent);
                            } else {
                                Intent intent =new Intent(MainActivity.this,menu.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }
}