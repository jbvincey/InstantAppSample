package com.jbvincey.instantappssample.presenters;

import com.jbvincey.instantappssample.model.Trip;
import com.jbvincey.instantappssample.repositories.TripRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jean-baptistevincey on 13/06/2017.
 */

public class MainPresenter extends AbstractPresenter<MainPresenter.View> {

    private TripRepository tripRepository;

    public MainPresenter(TripRepository tripRepository) {
        super();
        this.tripRepository = tripRepository;
    }

    public void start() {
        tripRepository.getTrips()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        call(disposable);
                    }
                })
                .subscribe(new Consumer<List<Trip>>() {
                    @Override
                    public void accept(List<Trip> tripList) throws Exception {
                        view.displayTrips(tripList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showTripLoadingError();
                    }
                });
    }

    public void onTripClicked(Trip trip) {
        view.goToTripDetails(trip.getId());
    }

    public interface View {

        void displayTrips(List<Trip> tripList);

        void showTripLoadingError();

        void goToTripDetails(String tripId);
    }
}
