package com.example.juju.gym_me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        email = getArguments().getString("email");
        password = getArguments().getString("password");
        try {
            user = new ProfileInfo(email, password);
            View v = inflater.inflate(R.layout.fragment_view_profile, container, false);
            TextView name = (TextView) v.findViewById(R.id.view_profile_name);
            name.setText(user.name);
            TextView phone = (TextView) v.findViewById(R.id.view_profile_phone);
            Log.d("manasi phone number", user.phone);
            phone.setText(user.phone);
            TextView address = (TextView) v.findViewById(R.id.view_profile_address);
            Log.d("manasi address", user.address);
            address.setText(user.address);
            TextView description = (TextView) v.findViewById(R.id.view_profile_bio);
            Log.d("manasi description", user.description);
            description.setText(user.description);
            TextView tags = (TextView) v.findViewById(R.id.view_profile_tags);
            tags.setText(user.tags);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: receive ProfileInfo
        //TODO: populate fields with profile information
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }
}
