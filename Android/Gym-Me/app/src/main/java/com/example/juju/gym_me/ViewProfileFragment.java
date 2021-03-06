package com.example.juju.gym_me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import static com.example.juju.gym_me.R.id.add;
import static com.example.juju.gym_me.R.id.toolbar;

/**
 * Created by Juju on 2/13/17.
 */

public class ViewProfileFragment extends Fragment {

    String email;
    String password;
    ProfileInfo user;
    TextView name;
    TextView phone;
    TextView address;
    TextView description;
    TextView tags;
    ImageView image;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getActivity();
//    }

    //ineDO: Display image stored in user.image

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        email = getArguments().getString("email");
        password = getArguments().getString("password");
        View v = inflater.inflate(R.layout.fragment_view_profile, container, false);
        try {
            user = new ProfileInfo(email);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name = (TextView) v.findViewById(R.id.view_profile_name);
        phone = (TextView) v.findViewById(R.id.view_profile_phone);
        address = (TextView) v.findViewById(R.id.view_profile_address);
        description = (TextView) v.findViewById(R.id.view_profile_bio);
        tags = (TextView) v.findViewById(R.id.view_profile_tags);
        image = (ImageView) v.findViewById(R.id.imageee);
        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
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

    }

    @Override
    public void onResume(){
        super.onResume();
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
    }

}
