package com.jbkindelberger.testproject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    Globals userID = Globals.getInstance();
    GlobalMessage msgGlobal = GlobalMessage.getInstance();
    Handler handler;
    Timer timer=new Timer();//Used for a delay to provide user feedback
    private Button btnLogout;
    private ImageButton btnSubmit;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> settingsList = new ArrayList<>();
    private EditText nameET, messageET, schoolET;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnSubmit = (ImageButton) view.findViewById(R.id.btn_submit);
        btnLogout = (Button) view.findViewById(R.id.btn_logout);
        nameET = (EditText) view.findViewById(R.id.get_userFirstName);
        messageET = (EditText) view.findViewById(R.id.get_defaultMessage);
        schoolET = (EditText) view.findViewById(R.id.get_defaultSchool);
        handler = new Handler(callback);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkET = true;
                String type = "send_settings";
                if(nameET.getText().toString().trim().length() == 0) {
                    nameET.setHint("Name field can't be blank!");
                    nameET.setHintTextColor(getResources().getColor(R.color.errorMessage));
                    checkET = false;
                }
                if(messageET.getText().toString().trim().length() == 0) {
                    messageET.setHint("School field can't be blank!");
                    messageET.setHintTextColor(getResources().getColor(R.color.errorMessage));
                    checkET = false;
                }
                if(schoolET.getText().toString().trim().length() == 0) {
                    schoolET.setHint("School field can't be blank!");
                    schoolET.setHintTextColor(getResources().getColor(R.color.errorMessage));
                    checkET = false;
                }
                if(checkET){
                    msgGlobal.setValue("[AutoMSG] " + nameET.getText().toString().trim() + " from "
                            + schoolET.getText().toString().trim()
                            + " would like to setup a meeting with you. "
                            + messageET.getText().toString().trim() + ":");
                    checkET = false;
                    nameET.setHint("");
                    schoolET.setHint("");

                    BackgroundWorker bw = new BackgroundWorker(getActivity().getApplicationContext());
                    bw.execute(type,userID.getValue(), messageET.getText().toString().trim(), schoolET.getText().toString().trim());
                    Utils.showToast(getActivity().getApplicationContext(), "Settings Updated.");
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), loginActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        timer.schedule(new SmallDelay(), 100);
        return view;
    }

    class SmallDelay extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if(!settingsList.isEmpty()){
                //Utils.showToast(getActivity().getApplicationContext(), settingsList.size());
                messageET.setText(settingsList.get(0).toString().trim());
                if(settingsList.get(1) != null)
                    schoolET.setText(settingsList.get(1).toString().trim());
                if(settingsList.get(2) != null)
                    nameET.setText(settingsList.get(2).toString().trim());
                Snackbar snackbar = Snackbar
                        .make(getView(), "Settings Loaded.", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            return true;
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        //execute background task
        String type = "get_settings";

        BackTask bt=new BackTask(getContext());

        bt.execute(type, userID.getValue());
        handler = new Handler(callback);
        timer.schedule(new SmallDelay(), 200);

    }


    public class BackTask extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        BackTask(Context ctx){
            context = ctx;

        }


        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            /*------------Local Host---------------
            String getContact_url = "http://10.0.2.2/get_settings.php"; //192.168.1.105
            */
            String getContact_url = "http://wesmd.net/letsmeet/get_settings.php"; //192.168.1.105

            String result = "";

            if(type.equals("get_settings")){
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
        }

        @Override
        protected void onPostExecute(String result){
            if (!result.equals("settings_not_setup")) {
                String delims = ":";
                String[] tokens = result.split(delims);
                if(tokens.length > 0) {
                    if(tokens[0] != null)
                        settingsList.add(0, tokens[0].toString().replaceAll("[-+ ()^\"^\\[^\\]]", " "));
                    if (tokens[1] != null)
                        settingsList.add(1, tokens[1].toString().replaceAll("[-+ ()^\"^\\[^\\]]", " "));
                    if (tokens[2] != null)
                        settingsList.add(2, tokens[2].toString().replaceAll("[-+ ()^\"^\\[^\\]]", " "));
                    //Utils.showToast(getActivity().getApplicationContext(), settingsList.toString());
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
    }
}
