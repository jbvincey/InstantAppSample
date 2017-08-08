package com.jbvincey.instantappssample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbvincey.instantappssample.R;
import com.jbvincey.instantappssample.dependencies.DependencyManager;
import com.jbvincey.instantappssample.helper.IntentHelper;
import com.jbvincey.instantappssample.helper.ResourceHelper;
import com.jbvincey.instantappssample.model.Coordinates;
import com.jbvincey.instantappssample.model.Trip;
import com.jbvincey.instantappssample.presenters.DetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jean-baptistevincey on 09/06/2017.
 */

public class DetailActivity extends AbstractDrawerActivity implements DetailPresenter.View {

    private static final String KEY_TRIP_ID = "KEY_TRIP_ID";
    @BindView(R.id.detailActivityTripPicture)
    ImageView tripPicture;
    @BindView(R.id.detailActivityTripGrade)
    TextView tripGrade;
    @BindView(R.id.detailActivityTripName)
    TextView tripName;
    @BindView(R.id.detailActivityTripDescription)
    TextView tripDescription;
    @BindView(R.id.detailActivityActionLocation)
    Button actionViewLocation;
    @BindView(R.id.detailActivityActionContact)
    Button actionViewContact;
    @BindView(R.id.detailActivityActionShare)
    Button actionViewShare;
    @BindView(R.id.detailActivityBookButton)
    Button bookButton;
    private DetailPresenter presenter;

    public static void startActivity(Context context, String tripId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_TRIP_ID, tripId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tripId = getIntent().getStringExtra(KEY_TRIP_ID);

        presenter = DependencyManager.getInstance().provideDetailPresenter();
        presenter.bind(this);

        presenter.init(tripId);
    }

    @Override
    public void onDestroy() {
        presenter.unbind();
        presenter = null;
        super.onDestroy();
    }


    @Override
    protected void setContentAndBind() {
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void setupTripView(final Trip trip) {
        tripPicture.setImageResource(ResourceHelper.getImageResourceFromName(this, trip.getCardImageFile()));
        tripName.setText(trip.getName());
        tripGrade.setText(trip.getGradeAsString());
        tripDescription.setText(trip.getDescription());
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBookClicked();
            }
        });
        actionViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onActionLocationClicked();
            }
        });
        actionViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onActionContactClicked();
            }
        });
        actionViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onActionShareClicked();
            }
        });
    }

    @Override
    public void showTripLoadingError() {
        showSnack(R.string.trip_loading_error);
        tripPicture.setImageResource(R.drawable.asos);
        tripName.setText(R.string.trip_error_name);
        tripGrade.setVisibility(View.GONE);
        bookButton.setVisibility(View.GONE);
        actionViewLocation.setVisibility(View.GONE);
        actionViewContact.setVisibility(View.GONE);
        actionViewShare.setVisibility(View.GONE);
    }

    @Override
    public void displayLocationIntent(Coordinates coordinates) {
        startActivity(IntentHelper.getMapsLocationItent(coordinates));
    }

    @Override
    public void displayContactIntent(String contact) {
        startActivity(IntentHelper.getContactIntent(contact));
    }

    @Override
    public void displayShareIntent(String tripId) {
        startActivity(IntentHelper.getShareDetailUrlIntent(tripId));
    }

    @Override
    public void confirmBooking(String name) {
        showSnack(getString(R.string.booking_confirmed, name));
    }

}
