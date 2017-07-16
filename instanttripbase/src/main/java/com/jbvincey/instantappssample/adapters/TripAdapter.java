package com.jbvincey.instantappssample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbvincey.instantappssample.R;
import com.jbvincey.instantappssample.helpers.ResourceHelper;
import com.jbvincey.instantappssample.models.Trip;

import java.util.List;

/**
 * Created by jean-baptistevincey on 20/06/2017.
 */

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private final List<Trip> tripList;

    private final TripAdapterItemListener listener;

    public TripAdapter(Context context, List<Trip> tripList, TripAdapterItemListener listener) {
        this.context = context;
        this.tripList = tripList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripViewHolder(LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {
        ((TripViewHolder) viewHolder).bind(tripList.get(index), listener, context);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public interface TripAdapterItemListener {

        void onTripClicked(Trip trip);

    }

    class TripViewHolder extends RecyclerView.ViewHolder {

        private ImageView tripPicture;
        private TextView tripTitle;
        private TextView tripGrade;

        TripViewHolder(View itemView) {
            super(itemView);
            tripPicture = (ImageView) itemView.findViewById(R.id.itemTripPicture);
            tripTitle = (TextView) itemView.findViewById(R.id.itemTripTitle);
            tripGrade = (TextView) itemView.findViewById(R.id.itemTripGrade);

        }

        void bind(final Trip trip, final TripAdapterItemListener listener, Context context) {
            tripTitle.setText(trip.getName());
            tripPicture.setImageResource(ResourceHelper.getImageResourceFromName(context, trip.getCardImageFile()));
            tripGrade.setText(trip.getGradeAsString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTripClicked(trip);
                }
            });
        }
    }
}
