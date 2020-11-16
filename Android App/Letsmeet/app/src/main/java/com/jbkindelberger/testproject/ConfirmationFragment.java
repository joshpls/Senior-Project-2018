package com.jbkindelberger.testproject;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationFragment extends Fragment {

    CustomAdapter adapter;
    ListView listContact;
    ArrayList<Contact> records;
    Globals userID = Globals.getInstance();
    Button clearBtn;
    Boolean undo;

    public ConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        records=new ArrayList<Contact>();

        listContact = (ListView) view.findViewById(R.id.confirmLV);
        clearBtn = (Button) view.findViewById(R.id.btn_clear);

        adapter = new CustomAdapter(getContext(),R.layout.item_list_contact,R.id.contact_name, records);

        listContact.setAdapter(adapter);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    undo = false;

                    Snackbar snackbar = Snackbar
                            .make(getView(), "Contacts Deleted.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.btnLogout))
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(ConfirmationFragment.this).attach(ConfirmationFragment.this).commit();
                                    undo = true;
                                }
                            });
                    snackbar.show();
                    snackbar.setCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                // Snackbar closed on its own
                                if (!undo) {
                                    String type = "clear_contacts";
                                    BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
                                    backgroundWorker.execute(type, userID.getValue());

                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(records.isEmpty()) {
            //execute background task
            String type = "get_contact";

            BackTask bt = new BackTask(getContext());

            bt.execute(type, userID.getValue());
        }
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
            /* --------- Local Host -----------
            String getContact_url = "http://10.0.2.2/get_contacts.php"; //192.168.1.105
            */
            String getContact_url = "http://wesmd.net/letsmeet/get_contacts.php"; //192.168.1.105

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
        }

        @Override
        protected void onPostExecute(String result){
            try {

                JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Contact c=new Contact();
                        String str = jsonArray.getString(i);
                        String delims = ": ";
                        String[] tokens = str.split(delims);
                        c.setcName(tokens[0].toString()+':');
                        if(tokens.length > 1)
                            c.setcDate(tokens[1].toString());
                        records.add(c);
                    }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
    }

}
