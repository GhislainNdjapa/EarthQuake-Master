package com.ndjapa_s1803449.earthquake_master.adapters;

// Author : Ghislain T Ndjapa
// StudentID: S1803449

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ndjapa_s1803449.earthquake_master.R;
import com.ndjapa_s1803449.earthquake_master.models.Earthquake;

import java.util.ArrayList;

public class EarthQuakesAdapter extends RecyclerView.Adapter<EarthQuakesAdapter.EarthQuakeViewHolder> {
    private ArrayList<Earthquake> earthquakes;
    private OnEarthQuakeClickListener clickListener;

    public EarthQuakesAdapter(ArrayList<Earthquake> earthquakes,OnEarthQuakeClickListener clickListener) {
        this.earthquakes = earthquakes;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public EarthQuakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_item_layout,parent,false);
        return new EarthQuakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthQuakeViewHolder holder, int position) {
        holder.bind(earthquakes.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    public void setEarthquakes(ArrayList<Earthquake> earthquakes) {
        this.earthquakes = earthquakes;
        notifyDataSetChanged();
    }

    public interface OnEarthQuakeClickListener{
        void onEarthQuakeClicked(Earthquake earthquake);
    }



    static class EarthQuakeViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView locationTextView;
        TextView magnitudeTextView;
        TextView dateTextView;
        public EarthQuakeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            magnitudeTextView = itemView.findViewById(R.id.textViewMagnitude);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(final Earthquake earthquake, final OnEarthQuakeClickListener earthQuakeClickListener) {
            magnitudeTextView.setText(String.format("M %s", earthquake.getMagnitude()));
            locationTextView.setText(earthquake.getLocation());
            dateTextView.setText(earthquake.getDateString());
            if (earthquake.getMagnitude() < 5.9) {
                magnitudeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.green));
            } else if(earthquake.getMagnitude() < 6.9) {
                magnitudeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.lightRed));
            } else {
                magnitudeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.darkRed));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    earthQuakeClickListener.onEarthQuakeClicked(earthquake);
                }
            });

        }
    }


}
