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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.juju.gym_me.R.id.toolbar;

/**
 * Created by Juju on 2/13/17.
 */

public class ViewInvitesFragment extends Fragment {
    SearchView sv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_invites, container, false);
        final ListView listview = (ListView) v.findViewById(R.id.listview);

        final ArrayList<String> list = new ArrayList<String>();
        // Get matches and add to list, remove hardcoded names

        list.add("Raaghav");
        list.add("Dinesh");
        list.add("Manasi");
        list.add("Juju");
        list.add("Scott");
        list.add("Mahathej");
        list.add("Corey");


        final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
        sv = (SearchView) v.findViewById(R.id.searchview3);

        String SearchedTag = sv.getQuery().toString();

        //send tag, get new list.

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), ViewOtherUsersProfileActivity.class);
                intent.putExtra("swiped", "YES");
                startActivity(intent);
            }

        });

        return v;
    }

}
