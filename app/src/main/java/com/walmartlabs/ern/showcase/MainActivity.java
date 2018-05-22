package com.walmartlabs.ern.showcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.walmartlabs.ern.container.ElectrodeReactActivityDelegate;
import com.walmartlabs.ern.showcase.fragment.ColorPickerMiniAppFragment;
import com.walmartlabs.ern.showcase.fragment.ElectrodeMiniAppFragment;
import com.walmartlabs.ern.showcase.fragment.InitialFragment;
import com.walmartlabs.ern.showcase.fragment.MovieListMiniAppFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ElectrodeReactActivityListener,
        ElectrodeMiniAppFragment.OnFragmentInteractionListener,
        ElectrodeReactActivityDelegate.BackKeyHandler {

    private ElectrodeReactActivityDelegate mReactActivityDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mReactActivityDelegate = new ElectrodeReactActivityDelegate(this);
        mReactActivityDelegate.setBackKeyHandler(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment initialFragment = InitialFragment.newInstance();
        switchToThisFragment(initialFragment);
    }

    private void switchToThisFragment(@NonNull final Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReactActivityDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReactActivityDelegate.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReactActivityDelegate.onDestroy();
    }


    @Override
    public void onBackPressed() {
        mReactActivityDelegate.onBackPressed();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movies) {
            Fragment movieListMiniAppFragment = MovieListMiniAppFragment.newInstance();
            switchToThisFragment(movieListMiniAppFragment);
        } else if (id == R.id.nav_color_picker) {
            Fragment colorPickerFragment = ColorPickerMiniAppFragment.newInstance();
            switchToThisFragment(colorPickerFragment);
        } else if (id == R.id.viewbuilder) {
            String uri = "https://ern.walmart.com/ern/NavDemoMiniApp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Navigation for this item is not supported yet.! ", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public ElectrodeReactActivityDelegate getElectrodeDelegate() {
        if (mReactActivityDelegate == null) {
            throw new IllegalStateException("Something is not right, looks like reactActivityDelegate is not initialized");
        }
        return mReactActivityDelegate;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        throw new IllegalStateException("TODO: handle fragment interactions");
    }

    @Override
    public void onBackKey() {
        //TODO: handle what needs to happen when a back key is pressed on a react native view.
        Toast.makeText(this, "TODO: Handle onBackKey()", Toast.LENGTH_SHORT).show();
    }
}
