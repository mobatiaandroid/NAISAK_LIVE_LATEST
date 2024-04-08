package com.nas.naisak.activity.trips.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nas.naisak.R;
import com.nas.naisak.constants.CommonMethods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripImageAdapter extends RecyclerView.Adapter<TripImageAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> tripList;

    public TripImageAdapter(Context mContext, ArrayList<String> tripListArray) {
        this.context = mContext;
        this.tripList = tripListArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_trip_image_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!tripList.get(position).equalsIgnoreCase("")) {
            Picasso.with(context).load(CommonMethods.Companion.replace(tripList.get(position))).fit().centerCrop()
                    .into(holder.photoImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;


        public MyViewHolder(View view) {
            super(view);
            photoImageView = (ImageView) view.findViewById(R.id.imgView);


        }
    }
}
