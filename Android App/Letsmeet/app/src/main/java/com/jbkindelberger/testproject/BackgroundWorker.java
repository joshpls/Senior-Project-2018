package com.jbkindelberger.testproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    Globals userID = Globals.getInstance();
    GlobalContact contactGlobal = GlobalContact.getInstance();

    BackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        /* --------- Local Host -------------------
        String login_url = "http://10.0.2.2/login.php"; //192.168.1.105
        String register_url = "http://10.0.2.2/register.php";
        String settings_url = "http://10.0.2.2/send_settings.php";
        String clearContacts_url= "http://10.0.2.2/clear_contacts.php";
        */
        String login_url = "http://wesmd.net/letsmeet/login.php"; //192.168.1.105
        String register_url = "http://wesmd.net/letsmeet/register.php";
        String settings_url = "http://wesmd.net/letsmeet/send_settings.php";
        String clearContacts_url= "http://wesmd.net/letsmeet/clear_contacts.php";

        if(type.equals("login")){
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                if(!result.equals("Login Unsuccessful")) {
                    userID.setValue(result);
                    result = "Login Success";
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("register")){
            try {
                String user_name = params[1];
                String password = params[2];
                String user_email = params[3];
                String user_number = params[4];
                String user_firstName = params[5];
                String user_lastName = params[6];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("user_email", "UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"
                        +URLEncoder.encode("user_number", "UTF-8")+"="+URLEncoder.encode(user_number,"UTF-8")+"&"
                        +URLEncoder.encode("user_firstName", "UTF-8")+"="+URLEncoder.encode(user_firstName,"UTF-8")+"&"
                        +URLEncoder.encode("user_lastName", "UTF-8")+"="+URLEncoder.encode(user_lastName,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("send_settings")){
            try {
                String user_id = params[1];
                String default_message = params[2];
                String user_school = params[3];

                URL url = new URL(settings_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                        +URLEncoder.encode("default_message", "UTF-8")+"="+URLEncoder.encode(default_message,"UTF-8")+"&"
                        +URLEncoder.encode("user_school", "UTF-8")+"="+URLEncoder.encode(user_school,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("clear_contacts")){
            try {
                String user_id = params[1];

                URL url = new URL(clearContacts_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute(){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result){
        switch (result) {
            case "Login Success":
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
                break;
            case "Register Success":
                Intent j = new Intent(context, loginActivity.class);
                context.startActivity(j);
                break;
            case "Settings Updated":

                break;
            case "Contacts Deleted":

                break;
            default:
                alertDialog.setMessage(result);
                alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }
}
