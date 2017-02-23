package com.example.juju.gym_me;

import android.provider.Settings;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by manasigoel on 2/22/17.
 */

public class ProfileInfo {

    String email;
    String password;
    String username;
    String name;
    String phone;
    String address;
    String description;
    String tags;
    String image;

    Server s = new Server();

    ProfileInfo(String email, String password) throws ExecutionException, InterruptedException {
        this.email = email;
        this.password = password;
        try {
            String info = s.execute("profile", email, password).get(); // is getting a null??
            if(info.equals("unsuccessful")){
                //TODO: error
            }
            if(info.equals("Incorrect email or password")){
                //TODO: error
            }
            //info contains: username, name, email, phone, address, tags, description
            String[] info_arr = info.split(",",-1);
            this.username = info_arr[0];
            this.name = info_arr[1];
            this.phone = info_arr[3];
            this.address = info_arr[4];
            this.description =info_arr[5];
            this.tags = info_arr[6];
            this.image = info_arr[7];
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
