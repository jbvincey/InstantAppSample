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

/**
 * Created by jean-baptistevincey on 09/06/2017.
 */

public class DetailActivity extends AbstractDrawerActivity implements DetailPresenter.View {

    private static final String KEY_TRIP_ID = "KEY_TRIP_ID";

    private DetailPresenter presenter;

    private ImageView tripPicture;
    private TextView tripGrade;
    private TextView tripName;
    private TextView tripDescription;
    private Button actionViewLocation;
    private Button actionViewContact;
    private Button actionViewShare;

    private Button bookButton;

    public static void startActivity(Context context, String tripId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_TRIP_ID, tripId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tripId = getIntent().getStringExtra(KEY_TRIP_ID);

        findViews();

        presenter = DependencyManager.getInstance().provideDetailPresenter();
        presenter.bind(this);

        presenter.init(tripId);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    private void findViews() {
        tripPicture = (ImageView) findViewById(R.id.detailActivityTripPicture);
        tripGrade = (TextView) findViewById(R.id.detailActivityTripGrade);
        tripName = (TextView) findViewById(R.id.detailActivityTripName);
        tripDescription = (TextView) findViewById(R.id.detailActivityTripDescription);
        actionViewLocation = (Button) findViewById(R.id.detailActivityActionLocation);
        actionViewContact = (Button) findViewById(R.id.detailActivityActionContact);
        actionViewShare = (Button) findViewById(R.id.detailActivityActionShare);
        bookButton = (Button) findViewById(R.id.detailActivityBookButton);
    }

    @Override
    public void onDestroy() {
        presenter.unbind();
        presenter = null;
        super.onDestroy();
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
