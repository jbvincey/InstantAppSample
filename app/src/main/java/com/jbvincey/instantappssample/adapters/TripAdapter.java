package com.jbvincey.instantappssample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbvincey.instantappssample.R;
import com.jbvincey.instantappssample.helper.ResourceHelper;
import com.jbvincey.instantappssample.model.Trip;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        @BindView(R.id.itemTripPicture)
        ImageView tripPicture;
        @BindView(R.id.itemTripText)
        TextView tripTitle;
        @BindView(R.id.itemTripGrade)
        TextView tripGrade;

        TripViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Trip trip, final TripAdapterItemListener listener, Context context) {
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
