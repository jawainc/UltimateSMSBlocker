package com.example.jawad.smsblocker;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity
        implements InboxFragment.OnFragmentInteractionListener,
        AddNumberFragment.OnFragmentInteractionListener,
        AddRangeNumberForm.OnFragmentInteractionListener,
        ViewNumbersFragment.OnFragmentInteractionListener,
        ReadMessageFragment.OnFragmentInteractionListener,
        ImportFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {



    String active_fragment = "Inbox";
    SwitchCompat unknown_switch;
    EditText no_of_days;
    SettingModel settingModel;
    BlockMessageModel blockMessageModel;
    ScheduledFuture scheduledFuture;
    ScheduledExecutorService scheduledExecutorService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Menu menu = navigationView.getMenu();

        unknown_switch = (SwitchCompat) menu.findItem(R.id.nav_block_unknown).getActionView().findViewById(R.id.view_switch);

        no_of_days = (EditText) menu.findItem(R.id.nav_days_to_delete).getActionView().findViewById(R.id.settings_num_of_days);







    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        Fragment fragment = null;
        Class fragmentClass = InboxFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.flContent, fragment).commit();

        settingModel = new SettingModel(getBaseContext());
        blockMessageModel = new BlockMessageModel(getBaseContext());



        int[] settings = settingModel.getValues();

        /**
         * Settings
         */
        if(settings.length > 0) {

            if (settings[0] == 1)
                unknown_switch.setChecked(true);
            else
                unknown_switch.setChecked(false);

            if (settings[1] > 0) {
                no_of_days.setText("" + settings[1]);
                blockMessageModel.RemoveScheduleMessages(settings[1]);
                runScheduler();
            }
            else {
                no_of_days.setText("0");
                stopScheduler();
            }

        }
        else{
            unknown_switch.setChecked(false);
            no_of_days.setText("0");
            stopScheduler();
        }


        unknown_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingModel.updateUnknown(1);
                else
                    settingModel.updateUnknown(0);
            }
        });

        no_of_days.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    int num_days = Integer.parseInt(s.toString());
                    settingModel.updateDays(num_days);
                    if(num_days >= 1 && num_days <= 1000) {
                        runScheduler();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "To run scheduled delete please give numbers between 1 and 1000", Toast.LENGTH_LONG).show();
                        stopScheduler();
                    }

                }
                else{
                    stopScheduler();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

       active_fragment = uri.getQueryParameter("fragment");

        if(active_fragment.equals("Inbox")){

            setTitle("Messages");

        }
        if(active_fragment.equals("ReadMessage")){

               setTitle(uri.getQueryParameter("number"));

        }



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        Fragment fragment = null;
        Class fragmentClass;

        int id = item.getItemId();

        switch (id){
            case R.id.nav_inbox:
                fragmentClass = InboxFragment.class;
                break;
            case R.id.nav_block_number:
                fragmentClass = AddNumberFragment.class;
                break;
            case R.id.nav_view_numbers:
                fragmentClass = ViewNumbersFragment.class;
                break;
            case R.id.nav_upload:
                fragmentClass = ImportFragment.class;
                break;
            case R.id.nav_download:
                fragmentClass = ExportFragment.class;
                break;
            default:
                fragmentClass = InboxFragment.class;

        }



        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }



        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.flContent, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Scheduler to delete messages
     *
     */
    void runScheduler(){

        stopScheduler();

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        int[] settings = settingModel.getValues();
                        if(settings.length > 0){
                            blockMessageModel.RemoveScheduleMessages(settings[1]);
                        }
                    }
                }, 0,1, TimeUnit.DAYS
        );
    }


    void stopScheduler(){
        if(scheduledFuture != null )
            scheduledFuture.cancel(true);
    }




}
