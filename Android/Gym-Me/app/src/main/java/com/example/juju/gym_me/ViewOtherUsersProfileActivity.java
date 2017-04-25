package com.example.juju.gym_me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by Juju on 3/1/17.
 */

public class ViewOtherUsersProfileActivity extends AppCompatActivity {
    String other_user;
    String email;
    String password;
    ProfileInfo user;
    TextView name;
    TextView phone;
    TextView address;
    TextView description;
    TextView tags;
    ImageView image;
    Button yes;
    Button no;
    Button unmatch;
    Button block;
    Button report;
    int MatchedFlag = 0;
    int blockedOtherFlag = 0;
    int blockedByOtherFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        name = (TextView) findViewById(R.id.view_profile_name);
        phone = (TextView) findViewById(R.id.view_profile_phone);
        address = (TextView) findViewById(R.id.view_profile_address);
        description = (TextView) findViewById(R.id.view_profile_bio);
        tags = (TextView) findViewById(R.id.view_profile_tags);
        image = (ImageView) findViewById(R.id.imageUserO);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        unmatch = (Button) findViewById(R.id.unmatch);
        block = (Button) findViewById(R.id.block);
        report = (Button) findViewById(R.id.report);
        unmatch.setVisibility(View.INVISIBLE);
        //sharedSchedule = (Button) findViewById(R.id.share_schedule_button);
        //sharedSchedule.setVisibility(View.INVISIBLE);

        if(getIntent().getStringExtra("swiped")!=null) {
            if (getIntent().getStringExtra("swiped").equals("YES")) {
                //yes.setVisibility(View.GONE);
                //no.setVisibility(View.GONE);
                MatchedFlag = 1;
                yes.setText("Shared Schedule");
                no.setText("Chat");
                unmatch.setVisibility(View.VISIBLE);
                //sharedSchedule.setVisibility(View.VISIBLE);
            }
        }
        other_user = getIntent().getStringExtra("other_user");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        try {
            user = new ProfileInfo(other_user);
            name.setText(user.name);
            if(!user.phone.equals("")) {
                phone.setText(user.phone);
            }
            if(!user.address.equals("")) {
                address.setText(user.address);
            }
            if(!user.description.equals("")) {
                description.setText(user.description);
            }
            if(!user.tags.equals("")) {
                tags.setText(user.tags);
            }
            if(!user.image.equals("")) {
                byte[] decodedString = Base64.decode(user.image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Server s = new Server();
        String blockees = null;
        try {
            blockees = s.execute("getallblocked", email).get();
            String[] blockeelist = blockees.split(",");
            for(int i = 0; i<blockeelist.length; i++){
                if(blockeelist[i].equals(user.email)){
                    blockedOtherFlag = 1;
                    block.setText("Unblock");
                }
            }
            Server t = new Server();
            blockees = s.execute("getallblocked", user.email).get();
            String[] blockeelist2 = blockees.split(",");
            for(int i = 0; i<blockeelist2.length; i++){
                if(blockeelist2[i].equals(email)){
                    blockedByOtherFlag = 1;
                    //TODO: HIDE THIS PROFILE
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    public void yesClicked(View V) throws ExecutionException, InterruptedException {
        if(MatchedFlag == 0) {
            Server s = new Server();
            String resp = "";
            if (getIntent().getStringExtra("type").equals("initial")) {
                resp = s.execute("sendmatch", email, other_user).get();
            }
            if (getIntent().getStringExtra("type").equals("final")) {
                resp = s.execute("acceptmatch", other_user, email).get();
            }
            Toast.makeText(this, resp, Toast.LENGTH_LONG).show();
            if (resp.equals("success")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(this, SharedScheduleActivity.class);
            //TODO: scheduling
            /*Server s = new Server();
            String resp = "";
            ProfileInfo p = new ProfileInfo(email);
            resp = s.execute("createsharedschedule", email, p.schedule).get();
            Toast.makeText(this, resp, Toast.LENGTH_LONG).show();
            if (resp.equals("success")) {*/
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("other_user", other_user);
                intent.putExtra("swiped", getIntent().getStringExtra("swiped"));
                startActivity(intent);
                finish();
            //}
        }
    }
    public void noClicked(View V) throws ExecutionException, InterruptedException {
        if(MatchedFlag == 0) {
            Server s = new Server();
            Intent intent = new Intent(this, MainActivity.class);
            if (getIntent().getStringExtra("type").equals("final")) {
                String resp = s.execute("declinematch", other_user, email).get();
                Toast.makeText(this, resp, Toast.LENGTH_LONG).show();
                if (resp.equals("success")) {
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
            } else {
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("other_user", other_user);
            intent.putExtra("swiped", getIntent().getStringExtra("swiped"));
            startActivity(intent);
        }
    }
    public void unmatchUser(View V) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(this, MainActivity.class);
        Server s = new Server();
        String resp  = s.execute("unmatch", email, other_user).get();
        Toast.makeText(this, resp, Toast.LENGTH_LONG).show();
        if(resp.equals("success")) {
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }

    public void blockUser(View V){
        if(blockedOtherFlag == 0) {
            Server s = new Server();
            s.execute("block", email, other_user);
        }
        else{
            Server s = new Server();
            s.execute("unblock", email, other_user);
        }

    }
    public void reportUser(View V){

    }
/*
    public void sharedSchedule(View V){
        Intent intent = new Intent(this, SharedScheduleActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("other_user", other_user);
        intent.putExtra("swiped", getIntent().getStringExtra("swiped"));
        startActivity(intent);
        finish();

    }
*/
}
