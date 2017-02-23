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
    TextView name;
    TextView phone;
    TextView address;
    TextView description;
    TextView tags;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getActivity();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        email = getArguments().getString("email");
        password = getArguments().getString("password");
        View v = inflater.inflate(R.layout.fragment_view_profile, container, false);
        try {
            user = new ProfileInfo(email, password);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name = (TextView) v.findViewById(R.id.view_profile_name);
        phone = (TextView) v.findViewById(R.id.view_profile_phone);
        //Log.d("manasi phone number", user.phone);
        address = (TextView) v.findViewById(R.id.view_profile_address);
        //Log.d("manasi address", user.address);
        description = (TextView) v.findViewById(R.id.view_profile_bio);
        //Log.d("manasi description", user.description);
        tags = (TextView) v.findViewById(R.id.view_profile_tags);
        //TODO: receive ProfileInfo
        //TODO: populate fields with profile information
        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        name.setText(user.email);
        phone.setText(user.password);
//        address.setText(user.address);
//        description.setText(user.description);
//        tags.setText(user.tags);


    }
    @Override
    public void onResume(){
        super.onResume();
        name.setText(user.email);
        phone.setText(user.password);
//        address.setText(user.address);
//        description.setText(user.description);
//        tags.setText(user.tags);

    }

}
