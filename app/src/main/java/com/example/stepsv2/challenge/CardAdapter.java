package com.example.stepsv2.challenge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.stepsv2.R;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //List to store all superheroes
    List<Challenge> challenges;

    //Constructor of this class
    public CardAdapter(List<Challenge> challenges, Context context){
        super();
        //Getting all superheroes
        this.challenges = challenges;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challenge_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        Challenge challenge =  challenges.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(challenge.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.halfface, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(challenge.getImageUrl(), imageLoader);
        holder.textViewName.setText(challenge.getName());
        holder.textViewActive.setText(challenge.getActive());
        holder.textViewAim.setText(challenge.getAim());
        holder.textViewDescription.setText(challenge.getDescription());

    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewActive;
        public TextView textViewAim;
        public TextView textViewDescription;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewActive = (TextView) itemView.findViewById(R.id.textViewActive);
            textViewAim = (TextView) itemView.findViewById(R.id.textViewAim);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}