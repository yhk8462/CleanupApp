package rmit.ad.cleanup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;

public class menu extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private FirebaseAuth mAuth;
    Button cCreate,cJoin,cMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mAuth = FirebaseAuth.getInstance();
        cCreate = findViewById(R.id.btnCleanup);
        cJoin = findViewById(R.id.btnJoin);
        cMap = findViewById(R.id.btnMap);

        cJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(menu.this, JoinGroup.class);
                startActivity(intent2);
            }
        });

        cCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, cMap.class);
                startActivity(intent);
            }
        });

        cMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(menu.this, cMap3.class);
                startActivity(intent3);
            }
        });
    }

}