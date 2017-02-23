package com.example.juju.gym_me;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
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

import static java.lang.System.in;

public class Server extends AsyncTask<String,String,String> {

        /* To use: 
            Create an instance of server and then call the function execute 
            parameters: 
                first is either "login", "register", "profile"  or "updateprofile"
                login: second is username (email), third is password 
                register: second is name, third is email, fourth is password 
                profile: second is username (email), third is password, fourth is token  (optional)
                updateprofile: 2-name, 3-email, 4-password, 5-phone, 6-address, 7-description, 8-tags
            return values:
                register: "success", "unsuccessful" if response was not OK, or an error message from backend
                login: "success", "unsuccessful" if response was not OK, or an error message from backend
                profile: comma separated list of info, "unsuccessful" if response was not OK, or an error message from backend

        */

    @Override
    protected String doInBackground(String... params) {

        if (params[0] == "register") {

            try {
                // Set the URL
                URL url = new URL("http://10.0.2.2:8080/register");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Create a JSON object with the appropriate parameters
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("password", params[3]); //TODO: hash
                jsonParam.put("phone", "");
                jsonParam.put("address", "");
                jsonParam.put("description", "");
                jsonParam.put("tags", "");

                // Set up connection
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                // POST
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

                    // Parse JSON object and return it
                    JSONObject obj = new JSONObject(result.toString());
                    if(obj.getString("success").equals("false")){
                        return (obj.getString("errormsg").toString());
                    }
                    else {
                        return ("success");
                    }
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

                    // Parse JSON object and return it
                    JSONObject obj = new JSONObject(result.toString());
                    Log.d("manasi", obj.getString("success").toString());
                    if(obj.getString("success").toString().equals("false")){
                        return (obj.getString("errormsg").toString());
                    }
                    else {
                        return ("success");
                    }
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
                jsonParam.put("email", params[1]);
                jsonParam.put("password", params[2]);
                //jsonParam.put("token", params[3]); //TODO: keep optional?

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

                    // Parse JSON object and return it
                    JSONObject obj = new JSONObject(result.toString());
                    if(obj.getString("success").equals("false")){
                        return (obj.getString("errormsg").toString());
                    }
                    else {
                        JSONArray arr = obj.getJSONArray("profile");
                        String response = arr.getJSONObject(0).getString("username");
                        response += "," + arr.getJSONObject(0).getString("name");
                        response += "," + arr.getJSONObject(0).getString("email");
                        response += "," + arr.getJSONObject(0).getString("phone");
                        response += "," + arr.getJSONObject(0).getString("address");
                        response += "," + arr.getJSONObject(0).getString("description");
                        response += "," + arr.getJSONObject(0).getString("tags");
                        return (response);
                    }

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

        if (params[0] == "updateprofile") { //TODO

            try {

                URL url = new URL("http://10.0.2.2:8080/updateprofile");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //updateprofile: 2-name, 3-email, 4-password, 5-phone, 6-address, 7-description, 8-tags
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("password", params[3]);
                jsonParam.put("phone", params[4]);
                jsonParam.put("address", params[5]);
                jsonParam.put("description", params[6]);
                jsonParam.put("tags", params[7]);


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

                    // Parse JSON object and return it
                    JSONObject obj = new JSONObject(result.toString());
                    if(obj.getString("success").equals("false")){
                        return (obj.getString("errormsg").toString());
                    }
                    else {
                        return ("success");
                    }
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

