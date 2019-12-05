package rmit.ad.cleanup;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CleanupFirebaseService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        storeToken(refreshedToken);

        Intent cleanupSiteUpdated = new Intent("cleanup_site_updated");
        cleanupSiteUpdated.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(cleanupSiteUpdated);
    }

    private void storeToken(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("cleanup_firebase", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.commit();
    }
}
