package rmit.ad.cleanup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

        TextView cleanUpTitle = (view.findViewById(R.id.cleanupTitle));
        TextView infoLog = (view.findViewById(R.id.infos));
        cleanUpTitle.setText(getArguments().getString("title"));
        infoLog.setText(getArguments().getString("info"));

        builder.setView(view);

        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the user and the cleanup site mapping
                String userId = mAuth.getUid();

                // Save user in firebase
                saveUserCleanupInDb(userId, getArguments().getString("title"));
            }
        });
        return builder.create();
    }

    private void saveUserCleanupInDb(String userId, String title) {
        UserCleanupSite userCleanupSite = new UserCleanupSite(userId, title, new Date());
        FirebaseDatabase.getInstance().getReference().child("user_cleanup").child(title).setValue(userCleanupSite);
    }
}
