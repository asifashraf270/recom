package com.glowingsoft.Recomendados.Fcm;

import android.util.Log;

import com.glowingsoft.Recomendados.GlobalClass;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/*
 * Created by Poyyamozhi on 21-Mar-18.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        GlobalClass.getInstance().storeFcmToken(refreshedToken);
        Log.d("fcm_token", refreshedToken);
    }
}