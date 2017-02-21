package com.example.juju.gym_me;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;

public class Server extends AsyncTask<String,Void,String> {

        /* To use: 
        Create an instance of server and then call the function execute 
        parameters: 
            first is either "login", "register" or "profile" 
            if login: second is username, third is password 
            if register: second is name, third is email, fourth is password 
            if profile: second is username, third is password, fourth is token 
        */

    @Override
    protected String doInBackground(String... params) {

        if (params[0] == "register") {

            try {

                URL url = new URL("http://10.0.2.2:8080/register");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("password", params[3]);

                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(jsonParam.toString());
                wr.flush();
                wr.close();

                int response_code = urlConnection.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (params[0] == "login") {

            try {

                URL url = new URL("http://10.0.2.2:8080/login");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", params[2]);

                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(jsonParam.toString());
                wr.flush();
                wr.close();

                int response_code = urlConnection.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (params[0] == "profile") {

            try {

                URL url = new URL("http://10.0.2.2:8080/profile");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", params[1]);
                jsonParam.put("password", params[2]);
                jsonParam.put("token", params[3]);

                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(jsonParam.toString());
                wr.flush();
                wr.close();

                int response_code = urlConnection.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}

