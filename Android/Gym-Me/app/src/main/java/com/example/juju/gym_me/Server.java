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

public class Server extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... params) {

        try {

            URL url = new URL("http://10.0.2.2:8080/register");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", "nameis");
            jsonParam.put("email", "emailis");
            jsonParam.put("password", "passwordis");

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
                //return(result.toString());

            }else{

                //return("unsuccessful");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

