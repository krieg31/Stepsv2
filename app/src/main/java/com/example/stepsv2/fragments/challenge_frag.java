package com.example.stepsv2.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.stepsv2.AppConfig;
import com.example.stepsv2.R;
import com.example.stepsv2.activity.MainActivity;
import com.example.stepsv2.challenge.adapter.FeedListAdapter;
import com.example.stepsv2.challenge.data.FeedItem;
import com.example.stepsv2.login_register.AppController;
import com.example.stepsv2.login_register.LoginActivity;
import com.example.stepsv2.login_register.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class challenge_frag extends Fragment {
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    private static final String TAG = challenge_frag.class.getSimpleName();
    private ListView listView;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://u234765589.hostingerapp.com/android_challenge_api/qwe.json";
    private String URL_ACCEPT ="http://u234765589.hostingerapp.com/android_challenge_api/norm/acceptchallenge.php";

    Dialog MyDialog;
    Button accept,close;


    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_frag, container, false);
        listView = view.findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                set_dialog(position);
            }
        });

        // We first check for cached request
        //Cache cache = AppController.getInstance().getRequestQueue().getCache();
        //Entry entry = cache.get(URL_FEED);
        Entry entry = null;
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
        return view;
    }
    private void set_dialog(int position)
    {
        FeedItem feedItem;
        feedItem = (FeedItem) listView.getAdapter().getItem(position);

        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.challenge_dialog);
        NetworkImageView profilePic = MyDialog.findViewById(R.id.profilePic);

        profilePic.setImageUrl(feedItem.getProfilePic(), imageLoader);
        TextView descr = MyDialog.findViewById(R.id.mainDescr);
        TextView name = MyDialog.findViewById(R.id.ChalName);

        if (feedItem.status!=null){
            descr.setText(feedItem.status);
        }
        if (feedItem.name!=null){
            name.setText(feedItem.name);
        }


        accept = MyDialog.findViewById(R.id.accept);
        close = MyDialog.findViewById(R.id.close);

        accept.setEnabled(true);
        close.setEnabled(true);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(getActivity(), "Challenge Accepted", Toast.LENGTH_SHORT, true).show();
                MyDialog.cancel();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }
    private void accept_challenge(final long chal_id)
    {
        final String challenge_id = Long.toString(chal_id).trim();
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ACCEPT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("active_chal", challenge_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
