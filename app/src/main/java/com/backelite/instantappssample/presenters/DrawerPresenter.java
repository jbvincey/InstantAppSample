package com.backelite.instantappssample.presenters;

/**
 * Created by jean-baptistevincey on 02/07/2017.
 */

public class DrawerPresenter extends AbstractPresenter<DrawerPresenter.View> {

    public DrawerPresenter() {
        super();
    }

    public void onSettingsClicked() {
        view.showSettings();
    }

    public void onAboutClicked() {
        view.showAbout();
    }

    public void onInstallClicked() {
        view.displayPlayStoreDownload();
    }

    public interface View {

        void showSettings();

        void showAbout();

        void displayPlayStoreDownload();

    }
}
