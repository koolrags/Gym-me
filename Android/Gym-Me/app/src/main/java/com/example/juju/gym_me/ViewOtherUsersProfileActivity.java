package com.example.juju.gym_me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Juju on 3/1/17.
 */

public class ViewOtherUsersProfileActivity extends AppCompatActivity {
    String email;
    String password;
    ProfileInfo user;
    TextView name;
    TextView phone;
    TextView address;
    TextView description;
    TextView tags;
    ImageView image;
    Button yes;
    Button no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        name = (TextView) findViewById(R.id.view_profile_name);
        phone = (TextView) findViewById(R.id.view_profile_phone);
        address = (TextView) findViewById(R.id.view_profile_address);
        description = (TextView) findViewById(R.id.view_profile_bio);
        tags = (TextView) findViewById(R.id.view_profile_tags);
        image = (ImageView) findViewById(R.id.imageUserO);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);

    if(getIntent().getStringExtra("swiped")!=null) {
        if (getIntent().getStringExtra("swiped").equals("YES")) {
            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
        }
    }
    }
    public void yesClicked(View V){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("name", sendname);
        startActivity(intent);
    }
    public void noClicked(View V){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("name", sendname);
        startActivity(intent);
    }
}
