package com.backelite.instantappssample.helper;

import android.content.Intent;
import android.net.Uri;

import com.backelite.instantappssample.BuildConfig;
import com.backelite.instantappssample.constants.Constants;
import com.backelite.instantappssample.model.Coordinates;

/**
 * Created by jean-baptistevincey on 24/06/2017.
 */

public final class IntentHelper {

    private static final String PLAYSTORE_BASE_URL = "market://details?id=";

    private static final String PACKAGE_MAPS = "com.google.android.apps.maps";

    private static final String COORDINATES_PREFIX = "geo:0,0?q=";

    private static final String COORDINATES_SEPARATOR = ",";

    private static final String SHARE_INTENT_TYPE = "text/plain";

    private static final String MAILTO_URI = "mailto:";

    public static Intent getMapsLocationItent(Coordinates coordinates) {
        Uri mapsUri = Uri.parse(buildCoordinatesUri(coordinates));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        mapIntent.setPackage(PACKAGE_MAPS);
        return mapIntent;
    }

    private static String buildCoordinatesUri(Coordinates coordinates) {
        return COORDINATES_PREFIX
                + coordinates.getLatitude()
                + COORDINATES_SEPARATOR
                + coordinates.getLongitude();
    }

    public static Intent getContactIntent(String contact) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(MAILTO_URI));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{contact});
        return intent;
    }

    public static Intent getShareDetailUrlIntent(String tripId) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(SHARE_INTENT_TYPE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, buildDetailUrl(tripId));
        return shareIntent;
    }

    private static String buildDetailUrl(String tripId) {
        return Constants.BASE_URL + "/" + tripId;
    }

    public static Intent getInstantTripPlayStoreIntent() {
        return new Intent(Intent.ACTION_VIEW, buildPlayStoreUrl());
    }

    private static Uri buildPlayStoreUrl() {
        return Uri.parse(PLAYSTORE_BASE_URL + BuildConfig.APPLICATION_ID);
    }

}
