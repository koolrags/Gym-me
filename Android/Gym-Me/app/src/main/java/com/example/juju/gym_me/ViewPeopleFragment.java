package com.example.juju.gym_me;

import android.app.Activity;
import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.juju.gym_me.R.id.toolbar;

/**
 * Created by Juju on 2/13/17.
 */

public class ViewPeopleFragment extends Fragment {
    SearchView sv;
    String email;
    String password;
    String usernames_list;
    String[] usernames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_other_users, container, false);
        final ListView listview = (ListView) v.findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<String>();
        // Get people and add to list, remove hardcoded names

        email = getArguments().getString("email");
        password = getArguments().getString("password");
        Server s = new Server();
        try {
            usernames_list = s.execute("getallprofiles", email, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        usernames = usernames_list.split(",");
        for(int i = 0; i< usernames.length; i++){
            String profile = null;
            try {
                Server t = new Server();
                profile = t.execute("profile", usernames[i]).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String[] info_arr = profile.split(",",-1);
            list.add(info_arr[1]);
        }



        final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
        sv = (SearchView) v.findViewById(R.id.searchview2);
        String SearchedTag = sv.getQuery().toString();

        //send tag, get new list.

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), ViewOtherUsersProfileActivity.class);
                intent.putExtra("username", usernames[position]);
                startActivity(intent);
            }

        });

        return v;
    }
}
