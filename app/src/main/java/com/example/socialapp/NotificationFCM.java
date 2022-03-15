package com.example.socialapp;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationFCM {
    public void sendNotify(Context context, String name, String message, String token ){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://fcm.googleapis.com/fcm/send";
        JSONObject data = new JSONObject();
        try {
            data.put("title", name);
            data.put("body", message);
            JSONObject notificationData = new JSONObject();
            notificationData.put("notification", data);
            notificationData.put("to", token);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                // sending Headers
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<>();
                    String key = "Key=AAAA6qfRLwk:APA91bFCxWMWc-tqDMRO3fN8df1RSaBkr63kT2B8_gFNlxEkLGH4rPEiHUCHK3orkm25BIakyA0MzgTu0I7VuK5yOdnI1sa-Oo6BiOheJlmEld3w6uXxENbzAncamyhsxKArxCOzWtS8\t\n";
                    header.put("Content-Type", "application/json");
                    header.put("Authorization", key);

                    return header;
                }
            };

            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
