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
import java.util.ArrayList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.in;

public class Server extends AsyncTask<String,String,String> {


    String baseURL = "http://10.0.2.2:8080";

        /* To use: 
            Create an instance of server and then call the function execute 
            parameters: 
               first is either "login", "register", "profile"  or "updateprofile"
               login: second is username (email), third is password 
               register: second is name, third is email, fourth is password 
                profile: second is username (email)
                updateprofile: 2-name, 3-email, 4-password, 5-phone, 6-address, 7-description, 8-tags
                updateprofilepicture: second is email, third is password, fourth is picture
                getallprofiles: second is email, third is password
                sendmatch: second is sender email, third is receiver email
                getallwaiting: second is email, third is password
                acceptmatch: second is sender email, third is receiver email (current user)
                declinematch: second is sender email, third is receiver email (current user)
                unmatch: second is sender email, third is receiver email
                allmatches: second is email, third is password
                search: second is email, third is password, fourth is tag to search for
                gettags: no parameters (GET request)
                addtagtouser: second is email, third is password, fourth is tag to add
                addschedule: second is email, thrid is password, fourth is comma separated schedule
                createsharedschedule: second is email, third is other email, fourth is schedule
                addlocation: second is email, third is password, fourth is location
                addmaxdistance: second is email, third is password, fourth is max distance
                block: second is blocker, third is blockee
                unblock: second is blocker, third is blockee
                getallblocked: 2-blocker
                reportabuse: 2 - reporter, 3 - abuser
                createmonthlygoal: 2 - email, 3 - currentgoal, 4 - completegoal
                editmonthlygoal: 2 - email, 3 - currentgoal, 4 - completegoal
                getmonthlygoal: 2-email
                sortbyname: 2 - name
                getsharedschedule: 2 - user1, 3 - user2
                editsharedschedule: second is email, third is other email, fourth is schedule


            return values:
                register: "success", "unsuccessful" if response was not OK, or an error message from backend
                login: "success", "unsuccessful" if response was not OK, or an error message from backend
                getallprofiles: "empty" if none or comma separated list of usernames
                getallwaiting: "empty" if none or comma separated list of usernames
                allmatches: "empty" if none or comma separated list of usernames
                gettags: "empty" if none or comma separated list of tags
        */

    @Override
    protected String doInBackground(String... params) {

        if (params[0] == "register") {

            try {
                // Set the URL
                URL url = new URL(baseURL+ "/register");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Create a JSON object with the appropriate parameters
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("password", hashPassword(params[3]));
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

                URL url = new URL(baseURL+ "/login");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));

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
                URL url = new URL(baseURL+ "/profile");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                //jsonParam.put("password", params[2]); //no longer required
                //jsonParam.put("token", params[3]); //optional

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
                        if(arr.getJSONObject(0).getString("image")==null){
                            response += ",";
                        }
                        else {
                            response += "," + arr.getJSONObject(0).getString("image");
                        }
                        if(arr.getJSONObject(0).getString("schedule")==null){
                            response += ",";
                        }
                        else {
                            response += "," + arr.getJSONObject(0).getString("schedule");
                        }
                        if(arr.getJSONObject(0).getString("location")==null){
                            response += ",";
                        }
                        else {
                            response += "," + arr.getJSONObject(0).getString("location");
                        }
                        if(arr.getJSONObject(0).getString("maxdistance")==null){
                            response += ",";
                        }
                        else {
                            response += "," + arr.getJSONObject(0).getString("maxdistance");
                        }
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

        if (params[0] == "updateprofile") {

            try {

                URL url = new URL(baseURL+ "/updateprofile");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //updateprofile: 2-name, 3-email, 4-password, 5-phone, 6-address, 7-description, 8-tags
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", params[1]);
                jsonParam.put("email", params[2]);
                jsonParam.put("password", hashPassword(params[3]));
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

        if (params[0] == "updateprofilepicture") {

            try {

                URL url = new URL(baseURL+ "/updateprofilepicture");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));
                jsonParam.put("image", params[3]);

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

        if (params[0] == "getallprofiles") {

            try {

                URL url = new URL(baseURL+ "/getallprofiles");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));

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
                        JSONArray arr = obj.getJSONArray("profiles");
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String usernames_list = "";
                        for(int i = 0; i<length; i++){
                            String username = arr.getJSONObject(i).getString("username");
                            usernames_list = usernames_list + username + ",";
                        }
                        return usernames_list;
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

        if (params[0] == "sendmatch") {

            try {

                URL url = new URL(baseURL+ "/sendmatch");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("sender", params[1]);
                jsonParam.put("receiver", params[2]);

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

        if (params[0] == "getallwaiting") {

            try {

                URL url = new URL(baseURL+ "/getallwaiting");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));

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
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String usernames_list = "";
                        for(int i = 0; i<length; i++){
                            String username = arr.getJSONObject(i).getString("email");
                            usernames_list = usernames_list + username + ",";
                        }
                        return usernames_list;
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

        if (params[0] == "acceptmatch") {

            try {

                URL url = new URL(baseURL+ "/acceptmatch");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("sender", params[1]);
                jsonParam.put("receiver", params[2]);

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

        if (params[0] == "declinematch") {

            try {

                URL url = new URL(baseURL+ "/declinematch");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("sender", params[1]);
                jsonParam.put("receiver", params[2]);

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

        if (params[0] == "unmatch") {

            try {

                URL url = new URL(baseURL+ "/unmatch");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("sender", params[1]);
                jsonParam.put("receiver", params[2]);

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

        if (params[0] == "allmatches") {

            try {

                URL url = new URL(baseURL+ "/allmatches");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));

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
                        JSONArray arr = obj.getJSONArray("matches");
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String usernames_list = "";
                        for(int i = 0; i<length; i++){
                            String username = arr.getJSONObject(i).getString("Email");
                            usernames_list = usernames_list + username + ",";
                        }
                        return usernames_list;
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

        if (params[0] == "sortbytag") {

            try {

                URL url = new URL(baseURL+ "/sortbytag");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));
                jsonParam.put("tag", params[3]);

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
                        JSONArray arr = obj.getJSONArray("users");
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String usernames_list = "";
                        for(int i = 0; i<length; i++){
                            String username = arr.getJSONObject(i).getString("user_email");
                            usernames_list = usernames_list + username + ",";
                        }
                        return usernames_list;
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

        if (params[0] == "gettags") {

            try {

                URL url = new URL(baseURL+ "/gettags");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


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
                        JSONArray arr = obj.getJSONArray("tags");
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String tags_list = "";
                        for(int i = 0; i<length; i++){
                            String tag = arr.getJSONObject(i).getString("description");
                            tags_list = tags_list + tag + ",";
                        }
                        return tags_list;
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

        if (params[0] == "addtagtouser") {

            try {

                URL url = new URL(baseURL+ "/addtagtouser");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password, 4-tag
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));
                jsonParam.put("tag", params[3]);

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

        if (params[0] == "addschedule") {

            try {

                URL url = new URL(baseURL+ "/addschedule");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));
                jsonParam.put("schedule", params[3]);

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

        if (params[0] == "createsharedschedule") {

            try {

                URL url = new URL(baseURL+ "/createsharedschedule");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user1", params[1]);
                jsonParam.put("user2", params[2]);
                jsonParam.put("schedule", params[3]);

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

        if (params[0] == "addlocation") {

            try {

                URL url = new URL(baseURL+ "/addlocation");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password, 4-location
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));
                jsonParam.put("userlocation", params[3]);

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

        if (params[0] == "addmaxdistance") {

            try {

                URL url = new URL(baseURL+ "/addmaxdistance");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallprofiles: 2-email, 3-password, 4-max distance
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("password", hashPassword(params[2]));
                jsonParam.put("maxdistance", params[3]);

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


        if (params[0] == "block") {

            try {

                URL url = new URL(baseURL+ "/block");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //block: 2 - blocker, 3 - blockee
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("blocker", params[1]);
                jsonParam.put("blockee", params[2]);

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

        if (params[0] == "unblock") {

            try {

                URL url = new URL(baseURL+ "/unblock");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //unblock: 2 - blocker, 3 - blockee
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("blocker", params[1]);
                jsonParam.put("blockee", params[2]);

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

        if (params[0] == "getallblocked") {

            try {

                URL url = new URL(baseURL+ "/getallblocked");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getallblocked: 2-blocker
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("blocker", params[1]);

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
                        JSONArray arr = obj.getJSONArray("blocks");
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String usernames_list = "";
                        for(int i = 0; i<length; i++){
                            String username = arr.getJSONObject(i).getString("blockee");
                            usernames_list = usernames_list + username + ",";
                        }
                        return usernames_list;
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

        if (params[0] == "reportabuse") {

            try {

                URL url = new URL(baseURL+ "/reportabuse");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //reportabuse: 2 - reporter, 3 - abuser
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("reporter", params[1]);
                jsonParam.put("abuser", params[2]);

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

        if (params[0] == "sortbyname") {

            try {

                URL url = new URL(baseURL+ "/sortbyname");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //sortbyname: 2-name
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", params[1]);

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
                        JSONArray arr = obj.getJSONArray("users");
                        int length = arr.length();
                        if(length == 0){
                            return "empty";
                        }
                        String usernames_list = "";
                        for(int i = 0; i<length; i++){
                            String username = arr.getJSONObject(i).getString("name");
                            usernames_list = usernames_list + username + ",";
                        }
                        return usernames_list;
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

        if (params[0] == "createmonthlygoal") {

            try {

                URL url = new URL(baseURL+ "/createmonthlygoal");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //createmonthlygoal: 2 - email, 3 - currentgoal, 4 - completegoal
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("currentgoal", params[2]);
                jsonParam.put("completegoal", params[3]);

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

        if (params[0] == "editmonthlygoal") {

            try {

                URL url = new URL(baseURL+ "/editmonthlygoal");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //editmonthlygoal: 2 - email, 3 - currentgoal, 4 - completegoal
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);
                jsonParam.put("currentgoal", params[2]);
                jsonParam.put("completegoal", params[3]);

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

        if (params[0] == "getmonthlygoal") {

            try {

                URL url = new URL(baseURL+ "/getmonthlygoal");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getmonthlygoal: 2-email
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", params[1]);

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
                        if(obj.has("schedule")) {
                            JSONArray arr = obj.getJSONArray("schedule");
                            int length = arr.length();
                            if (length == 0) {
                                return "empty";
                            }
                            String goals = arr.getJSONObject(0).getString("current");
                            goals += "@@@";
                            goals += arr.getJSONObject(0).getString("finished");
                            return goals;
                        }
                        else{
                            return "empty";
                        }
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

        if (params[0] == "getsharedschedule") {

            try {

                URL url = new URL(baseURL+ "/getsharedschedule");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getmonthlygoal: 2-email
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user1", params[1]);
                jsonParam.put("user2", params[2]);

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
                            JSONArray arr = obj.getJSONArray("schedule");
                            int length = arr.length();
                            if (length == 0) {
                                return "empty";
                            }
                        if(obj.has("schedule")) {
                            String schedule = arr.getJSONObject(0).getString("schedule");
                            return schedule;
                        }
                        else{
                            return "empty";
                        }
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

        if (params[0] == "editsharedschedule") {

            try {

                URL url = new URL(baseURL+ "/editsharedschedule");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user1", params[1]);
                jsonParam.put("user2", params[2]);
                jsonParam.put("schedule", params[3]);

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

        if (params[0] == "sendmessage") {

            try {

                URL url = new URL(baseURL+ "/sendmessage");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //reportabuse: 2 - reporter, 3 - abuser
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("sender", params[1]);
                jsonParam.put("receiver", params[2]);
                jsonParam.put("message", params[3]);


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

        if (params[0] == "getallmessages") {

            try {

                URL url = new URL(baseURL+ "/getallmessages");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //getmonthlygoal: 2-email
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("sender", params[1]);
                jsonParam.put("receiver", params[2]);

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
                        JSONArray arr = obj.getJSONArray("messages");
                        int length = arr.length();
                        if (length == 0) {
                            return "empty";
                        }
                        else {
                            String message_list = "";
                            for (int i = 0; i < length; i++) {
                                String username = arr.getJSONObject(i).getString("message");
                                message_list = message_list + username + ";;;";
                            }
                            return message_list;
                        }

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



    public String hashPassword(String passwd){
        String hashedPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwd.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPass = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hashedPass;
    }


}

