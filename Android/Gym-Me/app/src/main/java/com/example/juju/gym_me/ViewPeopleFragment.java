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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    String waiting_list;
    String already_matched;
    List<String> usernames;
    List<String> waiting_usernames;
    List<String> matched_usernames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_other_users, container, false);
        final ListView listview = (ListView) v.findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<String>();
        // Get people and add to list, remove hardcoded names

        //TODO: get list of waiting and matches and be sure not to include them

        email = getArguments().getString("email");
        password = getArguments().getString("password");

        Server s = new Server();
        Server r = new Server();
        Server a = new Server();
        try {
            usernames_list = s.execute("getallprofiles", email, password).get();
            waiting_list = r.execute("getallwaiting", email, password).get();
            already_matched = a.execute("allmatches", email, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(usernames_list.equals("empty")){
            list.add("There are currently no other users.");
            final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
        }
        else {
            usernames = Arrays.asList(usernames_list.split(","));
            waiting_usernames = Arrays.asList(waiting_list.split(","));
            matched_usernames = Arrays.asList(already_matched.split(","));

            for (int i = 0; i < usernames.size(); i++) {
                if(!waiting_usernames.contains(usernames.get(i)) && !matched_usernames.contains(usernames.get(i))) {
                    String profile = null;
                    try {
                        Server t = new Server();
                        profile = t.execute("profile", usernames.get(i)).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    String[] info_arr = profile.split(",", -1);
                    list.add(info_arr[1]);
                }
            }

            if(list.size() == 0){
                list.add("There are currently no new users.");
                final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);
            }


            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
            sv = (SearchView) v.findViewById(R.id.searchview2);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

                    Toast.makeText(getActivity(), sv.getQuery().toString(),
                            Toast.LENGTH_LONG).show();
                    // TODO: search backend

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
                    intent.putExtra("other_user", usernames.get(position));
                    intent.putExtra("type", "initial");
                    startActivity(intent);
                }

            });
        }
        return v;
    }
}
