package com.backelite.instantappssample.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.backelite.instantappssample.R;
import com.backelite.instantappssample.adapters.TripAdapter;
import com.backelite.instantappssample.dependencies.DependencyManager;
import com.backelite.instantappssample.model.Trip;
import com.backelite.instantappssample.presenters.MainPresenter;

import java.util.List;

public class MainActivity extends AbstractDrawerActivity implements MainPresenter.View, TripAdapter.TripAdapterItemListener {

    private MainPresenter presenter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.mainActivityTripRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        presenter = DependencyManager.getInstance().provideMainPresenter();
        presenter.bind(this);
        presenter.start();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void onDestroy() {
        presenter.unbind();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void displayTrips(List<Trip> tripList) {
        recyclerView.setAdapter(new TripAdapter(this, tripList, this));
    }

    @Override
    public void showTripLoadingError() {
        showSnack(R.string.trip_list_loading_error);
    }

    @Override
    public void goToTripDetails(String tripId) {
        DetailActivity.startActivity(this, tripId);
    }

    @Override
    public void onTripClicked(Trip trip) {
        presenter.onTripClicked(trip);
    }
}
