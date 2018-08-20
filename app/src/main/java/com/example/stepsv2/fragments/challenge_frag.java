package com.example.stepsv2.fragments;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.stepsv2.AppConfig;
import com.example.stepsv2.R;
import com.example.stepsv2.challenge.CardAdapter;
import com.example.stepsv2.challenge.Challenge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class challenge_frag extends Fragment implements RecyclerView.OnScrollChangeListener{

    //Creating a List of superheroes
    private List<Challenge> listChallenges;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_frag, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listChallenges = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Calling method to get data to fetch data
        getData(progressBar);

        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new CardAdapter(listChallenges, getActivity().getApplicationContext());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        return view;
    }
    public JsonArrayRequest getDataFromServer(int requestCount, final ProgressBar progressBar) {
        //Initializing

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        //setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.URL_CHALLENGE + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(getActivity().getApplicationContext(), "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getData(final ProgressBar progressBar) {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount,progressBar));
        //Incrementing the request counter
        requestCount++;
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            Challenge challenge = new Challenge();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                challenge.setImageUrl(json.getString(AppConfig.TAG_IMAGE_URL));
                challenge.setName(json.getString(AppConfig.TAG_NAME));
                challenge.setActive(json.getString(AppConfig.TAG_ACTIVE));
                challenge.setAim(json.getString(AppConfig.TAG_AIM));
                challenge.setDescription(json.getString(AppConfig.TAG_DESCRIPTION));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            listChallenges.add(challenge);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            ProgressBar progressBar = v.findViewById(R.id.progressBar1);
            getData(progressBar);
        }
    }


    public static class ChangeMaxEvent {

        public final int maxmessage;

        ChangeMaxEvent(int maxmessage) {
            this.maxmessage = maxmessage;
        }
    }


}
