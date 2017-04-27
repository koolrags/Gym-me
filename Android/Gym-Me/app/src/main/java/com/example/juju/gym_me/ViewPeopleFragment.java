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
import android.util.Log;
import android.widget.Button;
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
    Button distance;
    AlertDialog.Builder builder;
    String distanceLength;
    ProfileInfo p1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_other_users, container, false);
        final ListView listview = (ListView) v.findViewById(R.id.listview);

        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner2);

        List<String> spinnerArray =  new ArrayList<String>();
        Server m = new Server();
        try {
            String resp = m.execute("gettags").get();
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String  mselection = spinner.getSelectedItem().toString();
                //apply sort on selection, copy code from search view.

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        final ArrayList<String> list = new ArrayList<String>();
        distance = (Button) v.findViewById(R.id.setDistance);
        distance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Pick the distance in meters!");
                final CharSequence distanceList[] = new CharSequence[]{"5", "10", "15","20","30","40","50","500000"};

                builder.setItems(distanceList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        distanceLength =distanceList[which].toString();
                        Server x = new Server();
                        x.execute("addmaxdistance", email, password, distanceLength);

                        Server b = new Server();
                        Server c = new Server();
                        Server a = new Server();
                        try {
                            usernames_list = b.execute("getallprofiles", email, password).get();
                            waiting_list = c.execute("getallwaiting", email, password).get();
                            already_matched = a.execute("allmatches", email, password).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        if(usernames_list.equals("No users found.")){
                            list.add("There are currently no other users.");
                            final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
                            listview.setAdapter(adapter);
                        }
                        else {
                            usernames = Arrays.asList(usernames_list.split(","));
                            waiting_usernames = Arrays.asList(waiting_list.split(","));
                            matched_usernames = Arrays.asList(already_matched.split(","));

                            try {
                                p1 = new ProfileInfo(email);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < usernames.size(); i++) {
                                if (!waiting_usernames.contains(usernames.get(i)) && !matched_usernames.contains(usernames.get(i))) {
                                    String profile = null;
                                    try {
                                        Server t = new Server();
                                        profile = t.execute("profile", usernames.get(i)).get();
                                        Log.d("Manasi", profile);
                                        String[] info_arr = profile.split(",", -1);
                                        ProfileInfo p2 = new ProfileInfo(usernames.get(i));
                                        double dist = distanceBetweenLocations(p1.latitude, p1.longitude, p2.latitude, p2.longitude);
                                        if (dist <= p1.maxdistance) {
                                            list.add(info_arr[1]);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            if (list.size() == 0) {
                                list.add("There are currently no new users.");
                                final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                                listview.setAdapter(adapter);
                            }
                        }


                            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                            listview.setAdapter(adapter);

                    }
                });
                builder.show();
            }
        });
        // Get people and add to list, remove hardcoded names

        //TODO: get list of waiting and matches and be sure not to include them

        email = getArguments().getString("email");
        password = getArguments().getString("password");

        Server s = new Server();
        Server r = new Server();
        Server a = new Server();
        final ArrayList<String> list_dist = new ArrayList<String>();
        try {
            usernames_list = s.execute("getallprofiles", email, password).get();
            waiting_list = r.execute("getallwaiting", email, password).get();
            already_matched = a.execute("allmatches", email, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(usernames_list.equals("No users found.")){
            list_dist.add("There are currently no other users.");
            final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list_dist);
            listview.setAdapter(adapter);
        }
        else {
            usernames = Arrays.asList(usernames_list.split(","));
            waiting_usernames = Arrays.asList(waiting_list.split(","));
            matched_usernames = Arrays.asList(already_matched.split(","));

            try {
                p1 = new ProfileInfo(email);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < usernames.size(); i++) {
                if(!waiting_usernames.contains(usernames.get(i)) && !matched_usernames.contains(usernames.get(i))) {
                    String profile = null;
                    try {
                        Server t = new Server();
                        profile = t.execute("profile", usernames.get(i)).get();
                        Log.d("Manasi", profile);
                        String[] info_arr = profile.split(",", -1);
                        ProfileInfo p2 = new ProfileInfo(usernames.get(i));
                        double dist = distanceBetweenLocations(p1.latitude,p1.longitude,p2.latitude,p2.longitude);
                        if(dist <= p1.maxdistance) {
                            list_dist.add(info_arr[1]);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }

            if(list_dist.size() == 0){
                list_dist.add("There are currently no new users.");
                final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list_dist);
                listview.setAdapter(adapter);
            }


            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list_dist);
            listview.setAdapter(adapter);

            sv = (SearchView) v.findViewById(R.id.searchview2);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

                    final ArrayList<String> list2 = new ArrayList<String>();

                    Server s = new Server();
                    Server r = new Server();
                    Server a = new Server();
                    try {
                        usernames_list = s.execute("sortbytag", email, password, sv.getQuery().toString()).get();
                        waiting_list = r.execute("getallwaiting", email, password).get();
                        already_matched = a.execute("allmatches", email, password).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    try {
                        p1 = new ProfileInfo(email);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (usernames_list.equals("empty")) {
                        list2.add("There are currently no other users.");
                        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list2);
                        listview.setAdapter(adapter);
                    } else {
                        usernames = Arrays.asList(usernames_list.split(","));
                        waiting_usernames = Arrays.asList(waiting_list.split(","));
                        matched_usernames = Arrays.asList(already_matched.split(","));

                        for (int i = 0; i < usernames.size(); i++) {
                            if (!waiting_usernames.contains(usernames.get(i)) && !matched_usernames.contains(usernames.get(i))) {
                                String profile = null;
                                try {
                                    Server t = new Server();
                                    profile = t.execute("profile", usernames.get(i)).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                ProfileInfo p2 = null;
                                try {
                                    p2 = new ProfileInfo(usernames.get(i));
                                    double dist = distanceBetweenLocations(p1.latitude,p1.longitude,p2.latitude,p2.longitude);
                                    if(dist <= p1.maxdistance) {
                                        String[] info_arr = profile.split(",", -1);
                                        list2.add(info_arr[1]);
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (list2.size() == 0) {
                            list2.add("There are currently no new users.");
                            final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list2);
                            listview.setAdapter(adapter);
                        }

                        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list2);
                        listview.setAdapter(adapter);

                        //Toast.makeText(getActivity(), sv.getQuery().toString(),
                        //        Toast.LENGTH_LONG).show();
                        // TODO: search backend

                        return false;
                    }
                    return false;
                }

            });
            //send tag, get new list.


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

    public double distanceBetweenLocations(double user1_lat,double user1_lon,double user2_lat,double user2_lon ){
        final int R = 6371000;

        double lat = Math.toRadians(user2_lat - user1_lat);
        double lon = Math.toRadians(user2_lon - user1_lon);
        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(user1_lat)) * Math.cos(Math.toRadians(user2_lat)) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // meters

        return Math.sqrt(distance);
    }
}
