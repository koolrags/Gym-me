package com.example.juju.gym_me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.juju.gym_me.R.id.toolbar;

/**
 * Created by Juju on 2/13/17.
 */

public class ViewMatchFragment extends Fragment {
    SearchView sv;
    String email;
    String password;
    String usernames_list;
    String[] usernames;
    List<String> search_usernames;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_matches, container, false);
        final ListView listview = (ListView) v.findViewById(R.id.listview);
        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);

        List<String> spinnerArray =  new ArrayList<String>();
        Server s = new Server();
        try {
            String resp = s.execute("gettags").get();
            String[] tags = resp.split(",");
            spinnerArray.add("Select tag");
            for(int i = 0; i<tags.length; i++){
                spinnerArray.add(tags[i]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);

        final ArrayList<String> list = new ArrayList<String>();
        // Get matches and add to list, remove hardcoded names

        email = getArguments().getString("email");
        password = getArguments().getString("password");

        Server t = new Server();
        try {
            usernames_list = t.execute("allmatches", email, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(usernames_list.equals("empty")){
            list.add("You have no matches.");
            final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
        }
        else {
            usernames = usernames_list.split(",");
            for (int i = 0; i < usernames.length; i++) {
                String profile = null;
                try {
                    Server u = new Server();
                    profile = u.execute("profile", usernames[i]).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String[] info_arr = profile.split(",", -1);
                list.add(info_arr[1]);
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //apply sort on selection, copy code from search view.

                String mselection = spinner.getSelectedItem().toString();

                if(mselection == "Select tag"){
                    final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter1);
                    return;
                }

                final ArrayList<String> list2 = new ArrayList<String>();

                Server s = new Server();
                try {
                    if(!mselection.equals("Select tag")) {
                        usernames_list = s.execute("sortbytag", email, password, mselection).get();
                        Log.d("Tag1","(!mselection.equals(\"Select tag\")");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (usernames_list.equals("empty")) {
                    list2.add("No users match the query.");
                    final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list2);
                    listview.setAdapter(adapter);
                    Log.d("Tag1"," if (usernames_list.equals(\"empty\")) {");

                } else {
                    search_usernames = Arrays.asList(usernames_list.split(","));

                    for (int i = 0; i < search_usernames.size(); i++) {
                        if (Arrays.asList(usernames).contains(search_usernames.get(i))) {
                            String profile = null;
                            try {
                                Server t = new Server();
                                profile = t.execute("profile", search_usernames.get(i)).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            String[] info_arr = profile.split(",", -1);
                            list2.add(info_arr[1]);
                        }
                    }

                    if (list2.size() == 0) {
                        list2.add("There are currently no new users.");
                        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list2);
                        listview.setAdapter(adapter);
                    }

                    final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list2);
                    listview.setAdapter(adapter);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        sv = (SearchView) v.findViewById(R.id.searchview1);


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()==0) {
                    final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter1);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                final ArrayList<String> list3 = new ArrayList<String>();


                if(list.contains(query)){
                    list3.add(query);
                    final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list3);
                    listview.setAdapter(adapter1);
                }
                else{
                    list3.add("Search returned no entry.");
                    final ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list3);
                    listview.setAdapter(adapter1);
                }

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
                intent.putExtra("swiped", "YES");
                startActivity(intent);
            }

        });

        return v;
    }

}
