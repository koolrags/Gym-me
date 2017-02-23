package com.example.juju.gym_me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Juju on 2/22/17.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Toast.makeText(context, "Workout Reminder!", Toast.LENGTH_LONG).show();
    }

}