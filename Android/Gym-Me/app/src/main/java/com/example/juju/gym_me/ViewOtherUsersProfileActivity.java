package com.example.juju.gym_me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

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
    Button unmatch;

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
        unmatch = (Button) findViewById(R.id.unmatch);
        unmatch.setVisibility(View.INVISIBLE);

        Log.d("Manasi", "Inside view other users profile activity");
        if(getIntent().getStringExtra("swiped")!=null) {
            if (getIntent().getStringExtra("swiped").equals("YES")) {
                yes.setVisibility(View.GONE);
                no.setVisibility(View.GONE);
                unmatch.setVisibility(View.VISIBLE);
            }
        }
        email = getIntent().getStringExtra("username");
        Log.d("Manasi getting user", email);
        try {
            user = new ProfileInfo(email);
            Log.d("Manasi user's name", user.name);
            name.setText(user.name);
            if(!user.phone.equals("")) {
                phone.setText(user.phone);
            }
            if(!user.address.equals("")) {
                address.setText(user.address);
            }
            if(!user.description.equals("")) {
                description.setText(user.description);
            }
            if(!user.tags.equals("")) {
                tags.setText(user.tags);
            }
            if(!user.image.equals("")) {
                byte[] decodedString = Base64.decode(user.image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
    public void unmatchUser(View V){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("name", sendname);
        startActivity(intent);
    }
}
