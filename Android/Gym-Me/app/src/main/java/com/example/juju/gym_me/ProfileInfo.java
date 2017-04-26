package com.example.juju.gym_me;

import android.provider.Settings;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by manasigoel on 2/22/17.
 */

public class ProfileInfo{

    String email;
    String password;
    String username;
    String name;
    String phone;
    String address;
    String description;
    String tags;
    String image;
    String schedule;
    String location;
    int maxdistance;
    double latitude;
    double longitude;

    Server s = new Server();

    ProfileInfo(String email) throws ExecutionException, InterruptedException {
        this.email = email;
        try {
            String info = s.execute("profile", email).get(); // is getting a null??
            if(info.equals("unsuccessful")){
                //TODO: error
            }
            if(info.equals("Incorrect email or password")){
                //TODO: error
            }
            //info contains: username, name, email, phone, address, tags, description, image, schedule
            String[] info_arr = info.split(",",-1);
            Log.d("Manasi", info);
            Log.d("Manasi", info_arr[10]);
            this.username = info_arr[0];
            this.name = info_arr[1];
            this.phone = info_arr[3];
            this.address = info_arr[4];
            this.description =info_arr[5];
            this.tags = info_arr[6];
            this.image = info_arr[7];
            this.schedule = info_arr[8];
            this.location = info_arr[9];
            if(!info_arr[10].equals("null")) {
                this.maxdistance = Integer.valueOf(info_arr[10]);
            }
            else{
                this.maxdistance = 100;
            }
            if(!location.equals("null")) {
                String[] loc = location.split(";;;");
                if (!loc[0].equals("null")) {
                    this.latitude = Double.parseDouble(loc[0]);
                } else {
                    this.latitude = 0;
                }
                if (!loc[1].equals("null")) {
                    this.longitude = Double.parseDouble(loc[1]);
                } else {
                    this.longitude = 0;
                }
            }
            else{
                this.latitude = 0;
                this.longitude = 0;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
