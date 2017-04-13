package com.example.juju.gym_me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Juju on 4/12/17.
 */

public class ChatActivity extends AppCompatActivity {
    Button send;
    EditText message;
    String messageToSend;
    ListView msgList;
    ArrayList<String> msgItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String other_user;
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send = (Button) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.msg);
        msgList = (ListView) findViewById(R.id.msguser1);
        other_user = getIntent().getStringExtra("other_user");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
    }
    public void sendClicked(View V){
        if(message.getText()!=null) {
            // replace email with name
            messageToSend = email + ": " +message.getText().toString();
            message.setText(" ");
            msgItems.add(messageToSend);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    msgItems);
            msgList.setAdapter(adapter);
        }
    }
}
