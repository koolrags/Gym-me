package com.example.juju.gym_me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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

public class ViewInvitesFragment extends Fragment {
    SearchView sv;
    String email;
    String password;
    String usernames_list;
    String[] usernames;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_invites, container, false);
        final ListView listview = (ListView) v.findViewById(R.id.listview);

        final ArrayList<String> list = new ArrayList<String>();
        // Get matches and add to list, remove hardcoded names

        email = getArguments().getString("email");
        password = getArguments().getString("password");

        Server s = new Server();
        try {
            usernames_list = s.execute("getallwaiting", email, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(!usernames_list.equals("empty")) {
            usernames = usernames_list.split(",");
            for (int i = 0; i < usernames.length; i++) {
                String profile = null;
                try {
                    Server t = new Server();
                    profile = t.execute("profile", usernames[i]).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String[] info_arr = profile.split(",", -1);
                list.add(info_arr[1]);
            }


            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
            sv = (SearchView) v.findViewById(R.id.searchview3);

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

                    //Toast.makeText(getActivity(), sv.getQuery().toString(),
                    //        Toast.LENGTH_LONG).show();
                    // Do your task here

                    return false;
                }

            });

            //send tag, get new list.

            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Intent intent = new Intent(getActivity(), ViewOtherUsersProfileActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("other_user", usernames[position]);
                    intent.putExtra("type", "final");
                    startActivity(intent);
                }

            });
        }
        else{
            list.add("There are currently no invites.");
            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
        }

        return v;
    }

}
