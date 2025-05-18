package com.dev.banksampahdigital.firebasenotification;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {

    private final String userFcmToken;

    private final String title;

    private final String body;

    private final Context context;

    private final String postUrl = "POST https://fcm.googleapis.com/v1/projects/hanum-bank-sampah/messages:send";

    public FcmNotificationSender(String userFcmToken, String title, String body, Context context) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.context = context;

    }

    public void SendNotification(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try {
                JSONObject messageObject = new JSONObject();


                JSONObject notiticationObject = new JSONObject();
                notiticationObject.put("title", title);
                notiticationObject.put("body", body);

                messageObject.put("token", userFcmToken);
                messageObject.put("notification", notiticationObject);

                mainObj.put("message", messageObject);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {

            }, error -> {

            }){
                @NonNull
                @Override
                public Map<String, String> getHeaders() {
                    Accesstoken accesstoken = new Accesstoken();
                    String acceskey = accesstoken.getAccessToken();

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "Bearer " + acceskey);

                    return header;
                };

            };

            requestQueue.add(request);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }





}
