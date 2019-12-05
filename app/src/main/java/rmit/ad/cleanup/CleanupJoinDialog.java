package rmit.ad.cleanup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.json.JSONObject;


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

        return builder.create();
    }
}
