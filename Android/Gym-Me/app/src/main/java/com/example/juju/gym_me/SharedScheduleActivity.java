package com.example.juju.gym_me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

/**
 * Created by Juju on 4/13/17.
 */

public class SharedScheduleActivity extends AppCompatActivity {
    String other_user;
    String email;
    String password;
    String swiped;
    EditText monday;
    EditText tuesday;
    EditText wednesday;
    EditText thursday;
    EditText friday;
    EditText saturday;
    EditText sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_schedule);
        other_user = getIntent().getStringExtra("other_user");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        swiped = getIntent().getStringExtra("swiped");
        sunday = (EditText) findViewById(R.id.edit_sunday);
        monday = (EditText) findViewById(R.id.edit_monday);
        tuesday = (EditText) findViewById(R.id.edit_tuesday);
        wednesday = (EditText) findViewById(R.id.edit_wednesday);
        thursday = (EditText) findViewById(R.id.edit_thursday);
        friday = (EditText) findViewById(R.id.edit_friday);
        saturday = (EditText) findViewById(R.id.edit_saturday);

        Server s = new Server();
        try {
            String resp = s.execute("getsharedschedule", email, other_user).get();
            if(resp.equals("empty")){
                Server t = new Server();
                t.execute("createsharedschedule", email, other_user, "");
            }
            else{
                String[] days = resp.split(";:;");
                sunday.setText(days[0]);
                monday.setText(days[1]);
                tuesday.setText(days[2]);
                wednesday.setText(days[3]);
                thursday.setText(days[4]);
                friday.setText(days[5]);
                saturday.setText(days[6]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void SaveCombinedSchedule(View V){
        Intent intent = new Intent(this, ViewOtherUsersProfileActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("other_user", other_user);
        intent.putExtra("swiped", swiped);

        String schedule = sunday.getText() + " ;:;";
        schedule += monday.getText() + " ;:;";
        schedule += tuesday.getText() + " ;:;";
        schedule += wednesday.getText() + " ;:;";
        schedule += thursday.getText() + " ;:;";
        schedule += friday.getText() + " ;:;";
        schedule += saturday.getText() + " ;:;";

        Server s = new Server();
        s.execute("editsharedschedule", email, other_user, schedule);

        startActivity(intent);
        finish();


    }
}
