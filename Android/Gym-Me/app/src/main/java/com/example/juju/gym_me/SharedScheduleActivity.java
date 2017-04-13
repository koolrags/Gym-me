package com.example.juju.gym_me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Juju on 4/13/17.
 */

public class SharedScheduleActivity extends AppCompatActivity {
    String other_user;
    String email;
    String password;
    String swiped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_schedule);
        other_user = getIntent().getStringExtra("other_user");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        swiped = getIntent().getStringExtra("swiped");
    }

    public void SaveCombinedSchedule(View V){
        Intent intent = new Intent(this, ViewOtherUsersProfileActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("other_user", other_user);
        intent.putExtra("swiped", swiped);

        startActivity(intent);
        finish();


    }
}
