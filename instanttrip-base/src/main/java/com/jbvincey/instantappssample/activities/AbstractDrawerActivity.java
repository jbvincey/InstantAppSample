package com.jbvincey.instantappssample.activities;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.instantapps.InstantApps;
import com.jbvincey.instantappssample.R;
import com.jbvincey.instantappssample.dependencies.DependencyManager;
import com.jbvincey.instantappssample.helper.IntentHelper;
import com.jbvincey.instantappssample.presenters.DrawerPresenter;


/**
 * Created by jean-baptistevincey on 29/06/2017.
 */

public abstract class AbstractDrawerActivity extends AppCompatActivity implements DrawerPresenter.View {

    private Toolbar toolbar;

    private NavigationView navigationView;

    private DrawerLayout drawerLayout;

    private DrawerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        findViews();
        if (toolbar == null || navigationView == null || drawerLayout == null) {
            throw new RuntimeException(this.getLocalClassName() + " must have a toolbar, a navigation view and a drawer layout");
        }
        setupToolbar();
        setupNavigationView();
        setupDrawer();

        presenter = DependencyManager.getInstance().provideDrawerPresenter();
        presenter.bind(this);
    }

    protected abstract @LayoutRes
    int getLayout();

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupNavigationView() {
        navigationView.getMenu().findItem(R.id.menuInstallApp).setVisible(InstantApps.isInstantApp(this));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                if (id == R.id.menuInstallApp) {
                    presenter.onInstallClicked();
                    return true;
                } else if (id == R.id.menuSettings) {
                    presenter.onSettingsClicked();
                    return true;
                } else if (id == R.id.menuAbout) {
                    presenter.onAboutClicked();
                    return true;
                } else {
                    return true;
                }
            }
        });
    }

    private void setupDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void displayPlayStoreDownload() {
        try {
            startActivity(IntentHelper.getInstantTripPlayStoreAppIntent());
        } catch (ActivityNotFoundException activityNotFoundException) {
            startActivity(IntentHelper.getInstantTripPlayStoreBrowserIntent());
        }

    }

    @Override
    public void showSettings() {
        showSnack(R.string.menu_settings);
    }

    @Override
    public void showAbout() {
        showSnack(R.string.menu_about);
    }

    public void showSnack(@StringRes int stringRes) {
        showSnack(getString(stringRes));
    }

    public void showSnack(String message) {
        Snackbar.make(drawerLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
