package com.jbvincey.instantappssample.repositories;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jbvincey.instantappssample.model.Trip;

import org.apache.commons.io.IOUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jean-baptistevincey on 18/06/2017.
 */

public class TripRepository {

    private Gson gson;

    private Context applicationContext;

    public TripRepository(Context applicationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        this.applicationContext = applicationContext;
    }

    public Single<List<Trip>> getTrips() {
        return Single.fromCallable(new Callable<List<Trip>>() {
            @Override
            public List<Trip> call() throws Exception {
                Type listType = new TypeToken<ArrayList<Trip>>() {
                }.getType();

                List<Trip> tripList = gson.fromJson(
                        IOUtils.toString(applicationContext.getAssets().open("trips/trips.json"), "UTF-8"),
                        listType
                );
                return tripList;
            }
        });
    }

    public Maybe<Trip> getTrip(final String tripId) {
        return getTrips()
                .toObservable()
                .flatMap(new Function<List<Trip>, ObservableSource<? extends Trip>>() {
                    @Override
                    public ObservableSource<? extends Trip> apply(List<Trip> tripList) throws Exception {
                        return Observable.fromIterable(tripList);
                    }
                })
                .filter(new Predicate<Trip>() {
                    @Override
                    public boolean test(Trip trip) throws Exception {
                        return trip.getId().equals(tripId);
                    }
                })
                .singleElement();
    }
}
