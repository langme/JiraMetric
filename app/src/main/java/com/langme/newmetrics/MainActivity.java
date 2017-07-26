package com.langme.newmetrics;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.langme.newmetrics.dummy.DetailContent;
import com.langme.newmetrics.dummy.ResumeItem;
import com.langme.newmetrics.dummy.SheetContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.langme.newmetrics.Constantes.MyPREFERENCES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SheetFragment.OnListFragmentInteractionListener,
        DetailSheetFragment.OnListFragmentInteractionListener,
        IndicatorDetailSheetFragment.OnFragmentInteractionListener,
        ResumeListDialogFragment.Listener,
        MySheetRecyclerViewAdapter.SettingsFragment.OnFragmentInteractionListener{

    private static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private Fragment fragment;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isStoragePermissionGranted()) {
            if (savedInstanceState == null) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new SheetFragment();
                fragmentTransaction.add(R.id.fragmentLayout, fragment, "SHEET");
                fragmentTransaction.commit();
            }
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Fragment f = getSupportFragmentManager().findFragmentById(R.id.listDetail);
                if (f instanceof DetailSheetFragment) {
                    // do something with f
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }*/

                FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                if(fragments != null){
                    for(Fragment fragment : fragments){
                        if(fragment != null && fragment.isVisible())
                            if (fragment instanceof DetailSheetFragment) {
                                // do something with f
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("resume", (Serializable) DetailContent.getITEMS());

                                ResumeListDialogFragment bottomSheetDialogFragment = new ResumeListDialogFragment();
                                bottomSheetDialogFragment.setArguments(bundle);
                                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                            }
                    }
                }
            }
        });

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!Utils.CheckSharesPref(sharedpreferences)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("name", "B"); //B
            editor.putString("state", "C"); //C
            editor.putString("critic", "D");//D
            editor.putString("create", "H");//H
            editor.putString("typo", "I");//I
            editor.putString("wish", "K");//K
            editor.putString("valide", "L");//L
            editor.putString("info", "P");//P
            editor.putString("cloture", "R");//R
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new MySheetRecyclerViewAdapter.SettingsFragment();
            fragmentTransaction.replace(R.id.fragmentLayout, fragment, "SETTINGS_SHEET");
            fragmentTransaction.addToBackStack("SETTINGS_SHEET");
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Grantedt: ");
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment fragment = new SheetFragment();

                    fragmentTransaction.replace(R.id.fragmentLayout,fragment);
                    fragmentTransaction.commitAllowingStateLoss ();

                } else {
                    Log.d(TAG, "Permission Denied: ");
                    finish();
                }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onListFragmentInteraction(SheetContent.SheetItem item) {
        Log.d("", "onListFragmentInteraction: " + item );
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (Utils.CheckSharesPref(sharedpreferences)) {
            // on replace le fragment par le fragment detail
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new DetailSheetFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragmentLayout, fragment, "SHEET");
            fragmentTransaction.addToBackStack("SHEET");
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getBaseContext(),"La configuration n'est pas définie...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListFragmentInteraction(DetailContent.DetailItem item) {
        Log.d("", "onListFragmentInteraction: " + item );
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new IndicatorDetailSheetFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentLayout, fragment, "DETAIL_SHEET");
        fragmentTransaction.addToBackStack("DETAIL_SHEET");
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onResumeClicked(int position) {

    }

    public void showFloatingActionButton() {
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }
}
