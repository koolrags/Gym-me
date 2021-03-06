package com.example.juju.gym_me;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * Created by Juju on 2/13/17.
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    Class fragmentClass;
    Fragment fragment;
    String email;
    String password;
    double latitude = 0.0;
    double longitude = 0.0;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        //Toast.makeText(this, email, Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        location();

        fragmentClass = ViewPeopleFragment.class;
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        Server s = new Server();
        s.execute("addlocation", email, password, longitude + ";;;" + latitude);

        fragmentClass = ViewPeopleFragment.class;
        try {
            //Intent intent = new Intent(MainActivity.this,ActivityClass);
            //startActivity(intent);
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle=new Bundle();
            bundle.putString("email", email);
            bundle.putString("password", password);
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();



    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = ViewProfileFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = ViewPeopleFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ViewMatchFragment.class;
                break;
            case R.id.nav_fourth_fragment:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_sixth_fragment:
                fragmentClass = ViewInvitesFragment.class;
                break;
            case R.id.nav_tenth_fragment:
                fragmentClass = ViewgoalsFragment.class;
                break;
            case R.id.nav_eight_fragment:
                fragmentClass = ViewScheduleFragment1.class;
                break;

            case R.id.nav_fifth_fragment:
                //logout
                fragmentClass = ViewPeopleFragment.class;
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
                break;

            default:
                fragmentClass = ViewPeopleFragment.class;
        }

        try {
            //Intent intent = new Intent(MainActivity.this,ActivityClass);
            //startActivity(intent);
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

         //Insert the fragment by replacing any existing fragment
        //TODO: send profile information to fragment
        Bundle bundle=new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void edit(View V){
        Intent intent = new Intent(this,EditProfileActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }
    public void reminder(View V)
    {
        TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
        int hour = tp.getHour();
        int min = tp.getMinute();
        Date date  = new Date();
        Calendar reminder = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(date);
        reminder.setTime(date);
        reminder.set(Calendar.HOUR_OF_DAY,hour);
        reminder.set(Calendar.MINUTE, min);
        reminder.set(Calendar.SECOND,0);
        if(reminder.before(currentTime)){
            reminder.add(Calendar.DATE,1);
        }
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        intentAlarm.putExtra("email", email);
        intentAlarm.putExtra("password", password);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,reminder.getTimeInMillis(), PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        Toast.makeText(this, "Reminder Set for"+hour+min, Toast.LENGTH_LONG).show();

    }

    public void location(){


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            //Toast.makeText(this, longitude + " " + latitude, Toast.LENGTH_LONG).show();
        }

    }
}