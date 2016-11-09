package com.apps.darkstorm.swrpg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DiceFragment.OnDiceInteractionListener,
        GuideMain.OnGuideInteractionListener, CharacterList.OnListInteractionListener, CharacterEditMain.OnFragmentInteractionListener,
        CharacterEditAttributes.OnCharEditInteractionListener,CharacterEditNotes.OnNoteInteractionListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    GoogleApiClient gac;
    boolean completeFail = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.light_side_key),false)){
            setTheme(R.style.LightSide);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.universeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (pref.getBoolean(getString(R.string.cloud_key), false)) {
            if (gac == null)
                gac = new GoogleApiClient.Builder(this)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            gac.connect();
            AsyncTask<Void, Void, Void> tmpLoop = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (!gac.isConnected()) {
                        while (!completeFail) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Checking Connect");
                            if (gac.isConnected()) {
                                System.out.println("Init Connect");
                                new InitialConnect(NavigationActivity.this, gac);
                                break;
                            }
                        }
                    } else {
                        new InitialConnect(NavigationActivity.this, gac);
                    }
                    return null;
                }
            };
            tmpLoop.execute();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (pref.getBoolean(getString(R.string.dice_key),false)){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                            android.R.anim.fade_in,android.R.anim.fade_out)
                    .replace(R.id.content_navigation,DiceFragment.newInstance()).commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                            android.R.anim.fade_in,android.R.anim.fade_out)
                    .replace(R.id.content_navigation,CharacterList.newInstance(gac)).commit();
        }
    }

    public void onStart(){
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
        }else {
            final SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
            if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                if (gac == null)
                    gac = new GoogleApiClient.Builder(this)
                            .addApi(Drive.API)
                            .addScope(Drive.SCOPE_FILE)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();
                gac.connect();
                AsyncTask<Void, Void, Void> tmpLoop = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (!gac.isConnected()) {
                            while (!completeFail) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Checking Connect");
                                if (gac.isConnected()) {
                                    System.out.println("Init Connect");
                                    new InitialConnect(NavigationActivity.this, gac);
                                    break;
                                }
                            }
                        } else {
                            new InitialConnect(NavigationActivity.this, gac);
                        }
                        return null;
                    }
                };
                tmpLoop.execute();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.universeFab);
        int id = item.getItemId();
        if (id == R.id.nav_characters){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                            android.R.anim.fade_in,android.R.anim.fade_out)
                    .replace(R.id.content_navigation,CharacterList.newInstance(gac)).addToBackStack("toCharacters").commit();
        }else if (id == R.id.nav_ships){
            Toast.makeText(this,R.string.ship_coming_soon_text,Toast.LENGTH_LONG).show();
        }else if(id == R.id.nav_dice){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                            android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("toDice")
                    .replace(R.id.content_navigation,DiceFragment.newInstance()).commit();
        }else if(id == R.id.nav_guide){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                            android.R.anim.fade_in,android.R.anim.fade_out).addToBackStack("toGuide")
                    .replace(R.id.content_navigation,GuideMain.newInstance()).commit();
        }else if(id == R.id.nav_settings){
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDiceInteraction() {}
    @Override
    public void onGuideInteraction() {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 1);
            } catch (IntentSender.SendIntentException e) {
                completeFail = true;
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            System.out.println("Complete fail");
            completeFail = true;
        }
    }
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    System.out.println("ok");
                    gac.connect();
                }else{
                    System.out.println("here");
                    completeFail = true;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onListInteraction() {

    }

    @Override
    public void onNoteInteraction() {

    }
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if (requestCode == 50 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            final SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
            if (pref.getBoolean(getString(R.string.cloud_key), false)) {
                if (gac == null)
                    gac = new GoogleApiClient.Builder(this)
                            .addApi(Drive.API)
                            .addScope(Drive.SCOPE_FILE)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();
                gac.connect();
                AsyncTask<Void, Void, Void> tmpLoop = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (!gac.isConnected()) {
                            while (!completeFail) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Checking Connect");
                                if (gac.isConnected()) {
                                    System.out.println("Init Connect");
                                    new InitialConnect(NavigationActivity.this, gac);
                                    break;
                                }
                            }
                        } else {
                            new InitialConnect(NavigationActivity.this, gac);
                        }
                        return null;
                    }
                };
                tmpLoop.execute();
            }
        }
    }
}
