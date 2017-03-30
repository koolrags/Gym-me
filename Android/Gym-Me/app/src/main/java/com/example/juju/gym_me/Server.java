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
                URL url = new URL("http://10.0.2.2:8080/register");
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

                URL url = new URL("http://10.0.2.2:8080/login");
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
                URL url = new URL("http://10.0.2.2:8080/profile");
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

                URL url = new URL("http://10.0.2.2:8080/updateprofile");
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

                URL url = new URL("http://10.0.2.2:8080/updateprofilepicture");
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

                URL url = new URL("http://10.0.2.2:8080/getallprofiles");
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

                URL url = new URL("http://10.0.2.2:8080/sendmatch");
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

                URL url = new URL("http://10.0.2.2:8080/getallwaiting");
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

                URL url = new URL("http://10.0.2.2:8080/acceptmatch");
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

                URL url = new URL("http://10.0.2.2:8080/declinematch");
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

                URL url = new URL("http://10.0.2.2:8080/unmatch");
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

                URL url = new URL("http://10.0.2.2:8080/allmatches");
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

        //TODO
        if (params[0] == "sortbytag") {

            try {

                URL url = new URL("http://10.0.2.2:8080/sortbytag");
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
                        JSONArray arr = obj.getJSONArray("profile");
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

                URL url = new URL("http://10.0.2.2:8080/gettags");
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

                URL url = new URL("http://10.0.2.2:8080/addtagtouser");
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

                URL url = new URL("http://10.0.2.2:8080/addschedule");
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

