package rmit.ad.cleanup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import rmit.ad.cleanup.dto.User;
import rmit.ad.cleanup.dto.UserCleanupSite;

/**
 * Created the Dialog to be shown once the user clicks the a map marker
 * @author Shehani
 */
public class CleanupJoinDialog extends DialogFragment {

    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_join_dialog_layout, null);

        TextView infoLog = (view.findViewById(R.id.infos));
        infoLog.setText(getArguments().getString("info"));
        final String key = getArguments().getString("key");
        //Toast.makeText(CleanupJoinDialog.this,""+key,Toast.LENGTH_SHORT).show();

        builder.setView(view);

        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CleanupJoinDialog.this.getActivity(), newInfo.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("View users joined", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2 = new Intent(CleanupJoinDialog.this.getActivity(), SiteList.class);
                intent2.putExtra("key", key);
                startActivity(intent2);
            }
        });
        return builder.create();
    }

    private void saveUserCleanupInDb(String userId, String title) {
        UserCleanupSite userCleanupSite = new UserCleanupSite(userId, title, new Date());
        FirebaseDatabase.getInstance().getReference().child("user_cleanup").child(title).setValue(userCleanupSite);
    }
}
