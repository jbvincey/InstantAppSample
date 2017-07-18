package com.jbvincey.instantappssample.helpers;

import android.content.Intent;
import android.net.Uri;

import com.jbvincey.instantappssample.BuildConfig;
import com.jbvincey.instantappssample.models.Coordinates;

/**
 * Created by jean-baptistevincey on 24/06/2017.
 */

public final class IntentHelper {

    public static final String INSTANT_TRIP_URL_AUTHORITY = "instantappsample.jbvincey.com";
    private static final String PLAYSTORE_APP_BASE_URL = "market://details?id=";
    private static final String PLAYSTORE_BROWSER_BASE_URL = "https://play.google.com/store/apps/details?id=";
    private static final String PACKAGE_MAPS = "com.google.android.apps.maps";
    private static final String COORDINATES_PREFIX = "geo:0,0?q=";
    private static final String COORDINATES_SEPARATOR = ",";
    private static final String SHARE_INTENT_TYPE = "text/plain";
    private static final String MAILTO_URI = "mailto:";
    private static final String HTTPS_SCHEME = "https";
    private static final String DETAILS_ACTIVITY_PATH = "trips";

    public static Intent getMapsLocationItent(Coordinates coordinates) {
        Uri mapsUri = Uri.parse(buildCoordinatesUri(coordinates));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        //cannot specify package, throwing security exception when launched from instant app
        //mapIntent.setPackage(PACKAGE_MAPS);
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

    public static Intent getShareDetailsUrlIntent(String tripId) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(SHARE_INTENT_TYPE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, buildDetailsUrl(tripId).toString());
        return shareIntent;
    }

    public static Intent getDetailsActivityUrl(String tripId) {
        return new Intent(Intent.ACTION_VIEW, buildDetailsUrl(tripId));
    }

    private static Uri buildDetailsUrl(String tripId) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder
                .scheme(HTTPS_SCHEME)
                .authority(INSTANT_TRIP_URL_AUTHORITY)
                .appendPath(DETAILS_ACTIVITY_PATH)
                .appendPath(tripId);
        return uriBuilder.build();
    }

    public static Intent getInstantTripPlayStoreAppIntent() {
        return new Intent(Intent.ACTION_VIEW, buildPlayStoreUrl(PLAYSTORE_APP_BASE_URL));
    }

    public static Intent getInstantTripPlayStoreBrowserIntent() {
        return new Intent(Intent.ACTION_VIEW, buildPlayStoreUrl(PLAYSTORE_BROWSER_BASE_URL));
    }

    private static Uri buildPlayStoreUrl(String baseUrl) {
        return Uri.parse(baseUrl + BuildConfig.APPLICATION_ID);
    }
}
