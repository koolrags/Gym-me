package com.example.juju.gym_me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.juju.gym_me.R.id.toolbar;

/**
 * Created by Juju on 2/13/17.
 */

public class ViewgoalsFragment extends Fragment {
    EditText editText;
    ListView l1;
    ListView l2;
    Button done;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monthly_goals, container, false);
        editText = (EditText) v.findViewById(R.id.editText);
        l1 = (ListView) v.findViewById(R.id.list1);
        l2 = (ListView) v.findViewById(R.id.list2);
        done = (Button) v.findViewById(R.id.button2);
        final ArrayList<String> currentGoals = new ArrayList<String>();
        final ArrayList<String> completeGoals = new ArrayList<String>();

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, currentGoals);
        final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, completeGoals);

        //l1.setAdapter(adapter);
        //l2.setAdapter(adapter1);

        builder = new AlertDialog.Builder(getActivity());



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newGoal = editText.getText() + ""; // get text and append to current goals, send to backend.
                currentGoals.add(newGoal);
                l1.setAdapter(adapter); // refresh current list

            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String clickedGoal = (String) parent.getItemAtPosition(position);
                final CharSequence option[] = new CharSequence[]{"Delete", "Complete"};

                builder.setTitle("Pick an option!");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CharSequence optionPicked = option[which];
                        if(optionPicked == "Delete"){
                            // remove that from arraylist and backend.
                            // Either remove here and send backend new list or remove from backend and refresh list
                            currentGoals.remove(clickedGoal);
                            l1.setAdapter(adapter);
                        }
                        else{
                            //  remove that from arraylist l1 and move to l2 and backend.
                            // Either remove here and send backend new list or remove from backend and refresh list
                            currentGoals.remove(clickedGoal);
                            completeGoals.add(clickedGoal);
                            l1.setAdapter(adapter);
                            l2.setAdapter(adapter1);
                        }
                    }
                });
                builder.show();
            }
        });


        return v;
    }
}
