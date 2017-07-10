package com.jbvincey.instantappssample.dependencies;

import android.content.Context;

import com.jbvincey.instantappssample.repositories.TripRepository;

/**
 * Created by jean-baptistevincey on 18/06/2017.
 */

public class RepositoryModule {

    private static RepositoryModule instance;

    private TripRepository tripRepository;

    private RepositoryModule(Context applicationContext) {
        tripRepository = new TripRepository(applicationContext);
    }

    public static RepositoryModule getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new RepositoryModule(applicationContext);
        }
        return instance;
    }

    public TripRepository provideTripRepository() {
        return tripRepository;
    }
}
