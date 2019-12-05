package rmit.ad.cleanup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinInfoDialog extends DialogFragment {
    EditText nName, nContact;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference mDatabase;
    private String strName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.newinfo, null);


        String key = getArguments().getString("key");


        mDatabase = FirebaseDatabase.getInstance().getReference("Cleanup Sites").child(key).child("Users");

        nName = view.findViewById(R.id.edtName);
        nContact = view.findViewById(R.id.edtPhone);

        strName = nName.getText().toString();
        final String strContact = nContact.getText().toString();
        if (strName.isEmpty()) {
            nName.setError("Error no input");
        }
        if (strContact.isEmpty()) {
            nContact.setError("Error no input");
        }

        builder.setView(view);

        builder.setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nName.getText().toString().isEmpty()) {
                    return;
                }
                if (nContact.getText().toString().isEmpty()) {
                    return;
                }


                String name = nName.getText().toString().trim();
                String contact = nContact.getText().toString().trim();

                String id = mDatabase.push().getKey();
                mContact info = new mContact(name, contact);
                //databaseReference
                mDatabase.child(id).setValue(info);

                dialog.dismiss();

                // Show success dialog
                CleanupJoinSuccessDialog cleanupJoinSuccessDialog = new CleanupJoinSuccessDialog();
                Bundle args = new Bundle();
                args.putString("name", strName);
                cleanupJoinSuccessDialog.setArguments(args);
                cleanupJoinSuccessDialog.show(getFragmentManager(),"cleanup_join_dialog");
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