package com.example.juju.gym_me;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by manasigoel on 2/22/17.
 */

public class ProfileInfo {

    String email;
    String password;
    String username;
    String phone;
    String address;

    Server s = new Server();

    ProfileInfo(String email, String password) throws ExecutionException, InterruptedException {
        this.email = email;
        this.password = password;
        try {
            String info = s.execute("profile", email, password, "token").get();
            if(info.equals("unsuccessful")){
                //TODO: toast
            }
            if(info.equals("Incorrect email or password")){
                //TODO: toast
            }
            //info contains: username, name, email, phone, address
            String[] info_arr = info.split(",");
            this.username = info_arr[0];
            this.phone = info_arr[3];
            this.address = info_arr[4];
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
