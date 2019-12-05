package rmit.ad.cleanup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Date;

import rmit.ad.cleanup.dto.User;
import rmit.ad.cleanup.dto.UserCleanupSite;

/**
 * Created the Dialog to be shown once the user clicks the a map marker
 * @author Shehani
 */
public class CleanupJoinDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_join_dialog_layout, null);

        TextView infoLog = (view.findViewById(R.id.infos));
        String cleanUpSiteName = "";
        try {
            JSONObject siteInfoJson = new JSONObject(getArguments().getString("info"));
            cleanUpSiteName = (String) siteInfoJson.get("title");
            Log.d("joinDialog", siteInfoJson.toString());

        } catch (Exception ex) {
            Log.e("joinDialog", "Error occurred while parsing the json String to JsonObject");
        }
        infoLog.setText(cleanUpSiteName);
        final String key = getArguments().getString("key");

        builder.setView(view);

        builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CleanupJoinDialog.this.getActivity(), ViewJoinGroup.class);
                intent.putExtra("key", key);
                intent.putExtra("siteInfo", getArguments().getString("info"));
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        /**
        builder.setNegativeButton("View users joined", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2 = new Intent(CleanupJoinDialog.this.getActivity(), SiteList.class);
                intent2.putExtra("key", key);
                startActivity(intent2);
            }
        });
         */
        return builder.create();
    }
}
