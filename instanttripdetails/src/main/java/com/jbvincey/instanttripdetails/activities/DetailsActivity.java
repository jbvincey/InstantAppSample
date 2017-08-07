package com.jbvincey.instanttripdetails.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbvincey.instantappssample.activities.AbstractDrawerActivity;
import com.jbvincey.instantappssample.dependencies.DependencyManager;
import com.jbvincey.instantappssample.helpers.IntentHelper;
import com.jbvincey.instantappssample.helpers.ResourceHelper;
import com.jbvincey.instantappssample.models.Coordinates;
import com.jbvincey.instantappssample.models.Trip;
import com.jbvincey.instantappssample.presenters.DetailsPresenter;
import com.jbvincey.instanttripdetails.R;

/**
 * Created by jean-baptistevincey on 09/06/2017.
 */

public class DetailsActivity extends AbstractDrawerActivity implements DetailsPresenter.View {

    public static final String TAG = "DetailsActivity";

    private static final String KEY_TRIP_ID = "KEY_TRIP_ID";

    private DetailsPresenter presenter;

    private ImageView tripPicture;
    private TextView tripGrade;
    private TextView tripName;
    private TextView tripDescription;
    private Button actionViewLocation;
    private Button actionViewContact;
    private Button actionViewShare;

    private Button bookButton;

    //not used anymore, DetailsActivity is only started by deeplink since it has its own feature
    public static void startActivity(Context context, String tripId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_TRIP_ID, tripId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tripId = null;
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null && uri.getLastPathSegment() != null) {
            tripId = uri.getLastPathSegment();
        } else if (intent.hasExtra(KEY_TRIP_ID)) {
            tripId = intent.getStringExtra(KEY_TRIP_ID);
        } else {
            showTripLoadingError();
        }

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
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected NavigationView getNavigationView() {
        return (NavigationView) findViewById(R.id.navigationView);
    }

    @Override
    protected DrawerLayout getDrawer() {
        return (DrawerLayout) findViewById(R.id.drawer);
    }

    @Override
    public void onDestroy() {
        presenter.unbind();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void setupTripView(@NonNull final Trip trip) {
        tripPicture.setVisibility(View.VISIBLE);
        tripPicture.setImageResource(ResourceHelper.getImageResourceFromName(this, trip.getCardImageFile()));
        tripName.setVisibility(View.VISIBLE);
        tripName.setText(trip.getName());
        tripGrade.setVisibility(View.VISIBLE);
        tripGrade.setText(trip.getGradeAsString());
        tripDescription.setVisibility(View.VISIBLE);
        tripDescription.setText(trip.getDescription());
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBookClicked();
            }
        });
        actionViewLocation.setVisibility(View.VISIBLE);
        actionViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onActionLocationClicked();
            }
        });
        actionViewContact.setVisibility(View.VISIBLE);
        actionViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onActionContactClicked();
            }
        });
        actionViewShare.setVisibility(View.VISIBLE);
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
        tripPicture.setImageResource(com.jbvincey.instantappssample.R.drawable.asos);
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
        startActivity(IntentHelper.getShareDetailsUrlIntent(tripId));
    }

    @Override
    public void confirmBooking(String name) {
        showSnack(getString(R.string.booking_confirmed, name));
    }

}
