package com.softians.poller.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.softians.poller.adapter.MySingleton;
import com.softians.poller.app.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 04-09-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    String tok_url = "http://192.168.1.104:802/poller/tokensetter.php";
//    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        // sending reg id to your server
        if(pref.contains("userId")) {
            sendRegistrationToServer(refreshedToken);
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        // sending gcm token to server
        Log.e(TAG, "sendRegistration    ToServer: " + token);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,tok_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<String, String>();
                params.put("userId",pref.getString("userId",null));
                params.put("token",token);
                return params;
            }
        };
        MySingleton.getInstance(MyFirebaseInstanceIDService.this).addToRequestQueue(stringRequest);

    }

    private void storeRegIdInPref(String token) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}
