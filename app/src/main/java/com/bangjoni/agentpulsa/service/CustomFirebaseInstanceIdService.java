package com.bangjoni.agentpulsa.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by agung.kurniawan on 12/9/2016.
 */

public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "CustomFirebaseInstance";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
