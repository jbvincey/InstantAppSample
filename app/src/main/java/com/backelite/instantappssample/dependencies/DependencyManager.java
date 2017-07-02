package com.backelite.instantappssample.dependencies;

import android.content.Context;

import com.backelite.instantappssample.presenters.DetailPresenter;
import com.backelite.instantappssample.presenters.DrawerPresenter;
import com.backelite.instantappssample.presenters.MainPresenter;

/**
 * Created by jean-baptistevincey on 13/06/2017.
 */

public class DependencyManager {

    private static DependencyManager instance;

    private RepositoryModule repositoryModule;

    private DependencyManager() {

    }

    public void initWithContext(Context applicationContext) {
        repositoryModule = RepositoryModule.getInstance(applicationContext);
    }

    public static DependencyManager getInstance() {
        if(instance == null) {
            instance = new DependencyManager();
        }
        return instance;
    }

    public MainPresenter provideMainPresenter() {
        return new MainPresenter(this.repositoryModule.provideTripRepository());
    }

    public DetailPresenter provideDetailPresenter() {
        return new DetailPresenter(this.repositoryModule.provideTripRepository());
    }

    public DrawerPresenter provideDrawerPresenter() {
        return new DrawerPresenter();
    }
}
