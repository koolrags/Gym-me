package com.example.juju.gym_me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.juju.gym_me.R.id.toolbar;

/**
 * Created by Juju on 2/13/17.
 */

public class ViewScheduleFragment1 extends Fragment {
    SearchView sv;

    String email;
    String password;

    ProfileInfo user;

    EditText monday;
    EditText tuesday;
    EditText wednesday;
    EditText thursday;
    EditText friday;
    EditText saturday;
    EditText sunday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_schedule, container, false);

        email = getArguments().getString("email");
        password = getArguments().getString("password");

        sunday = (EditText) v.findViewById(R.id.edit_sunday);
        monday = (EditText) v.findViewById(R.id.edit_monday);
        tuesday = (EditText) v.findViewById(R.id.edit_tuesday);
        wednesday = (EditText) v.findViewById(R.id.edit_wednesday);
        thursday = (EditText) v.findViewById(R.id.edit_thursday);
        friday = (EditText) v.findViewById(R.id.edit_friday);
        saturday = (EditText) v.findViewById(R.id.edit_saturday);

        try {

            user = new ProfileInfo(email);
            String schedule = user.schedule;
            if(schedule.contains(";:;")) {
                String[] days = schedule.split(";:;");
                sunday.setText(days[0]);
                monday.setText(days[1]);
                tuesday.setText(days[2]);
                wednesday.setText(days[3]);
                thursday.setText(days[4]);
                friday.setText(days[5]);
                saturday.setText(days[6]);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button submit = (Button) v.findViewById(R.id.save_schedule_button);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String schedule = sunday.getText() + " ;:;";
                schedule += monday.getText() + " ;:;";
                schedule += tuesday.getText() + " ;:;";
                schedule += wednesday.getText() + " ;:;";
                schedule += thursday.getText() + " ;:;";
                schedule += friday.getText() + " ;:;";
                schedule += saturday.getText() + " ;:;";

                Server s = new Server();
                try {
                    String resp = s.execute("addschedule", email, password, schedule).get();
                    Toast.makeText(getActivity(), resp, Toast.LENGTH_LONG).show();
                    if(resp.equals("success")) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

}
