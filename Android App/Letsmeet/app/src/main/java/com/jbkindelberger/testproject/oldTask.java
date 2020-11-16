package com.jbkindelberger.testproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;
import com.jbkindelberger.testproject.R;

public class oldTask extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    ArrayList<Contact> records;
    CustomAdapter adapter;
    ListView contactList;

    oldTask(Context ctx){
        context = ctx;
        records=new ArrayList<Contact>();
        adapter = new CustomAdapter(context,R.layout.item_list_contact,R.id.contact_name, records);

    }


    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String getContact_url = "http://10.0.2.2/get_contacts.php"; //192.168.1.105
        String result = "";

        if(type.equals("get_contact")){
            try {
                String user_id = params[1];
                URL url = new URL(getContact_url);
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
        alertDialog.setTitle("Contacts:");
    }

    @Override
    protected void onPostExecute(String result){
        Contact c=new Contact();

        try {

            JSONArray jsonArray = new JSONArray(result);

            if(jsonArray.length() > 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String str = jsonArray.getString(i);
                    String delims = ",";
                    String delims2 = ":";
                    String[] tokens = str.split(delims);
                    String[] inner;
                    for(int j = 0; j < tokens.length; j++) {
                        inner = tokens[j].split(delims2);
                        c.setcName(inner[0].toString());
                        c.setcDate(inner[1].toString().replaceAll("[-+ ().^:,]", ""));
                    }
                }
            }
            records.add(c);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        alertDialog.setMessage(c.getcName() + c.getcDate());
        alertDialog.show();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }
}
