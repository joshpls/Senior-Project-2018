package com.jbkindelberger.testproject;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment{

    public final int PICK_CONTACT = 2015;
    public String thisMessage = "";
    ImageButton contactsButton;
    Handler handler;
    Timer timer=new Timer();//Used for a delay to provide user feedback
    Button sendButton;
    TextView messageTV;
    TextView contactsTV;
    EditText messageET;
    ListView contactsList;
    Globals userID = Globals.getInstance();
    GlobalMessage msgGlobal = GlobalMessage.getInstance();
    public String contact_name, contact_number;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    public Boolean undo;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        messageTV = (TextView) view.findViewById(R.id.messageTV);
        contactsTV = (TextView) view.findViewById(R.id.messageTV);
        messageET = (EditText) view.findViewById(R.id.messageET);
        int maxLengthofEditText = 160;
        messageET.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});

        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String SMSText = messageET.getText().toString();
                String SMSLimit = "160";
                messageTV.setText(messageTV.getText() + " [" + String.valueOf(SMSText.length()) + " / " + SMSLimit + "]");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                messageTV.setText("Message:");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String SMSText = messageET.getText().toString();
                String SMSLimit = "120";
                SmsManager manager = SmsManager.getDefault();
                ArrayList<String> messageParts = manager.divideMessage(SMSText);
                if(messageParts.size() > 1){

                    String smsStr = messageParts.get(0);
                    if(!isASCII(smsStr)) SMSLimit = "70";
                }
                messageTV.setText(messageTV.getText() + " [" + String.valueOf(SMSText.length()) + " / " + SMSLimit + "]");
            }
        });
        contactsList = (ListView) view.findViewById(R.id.contactsLV);
        contactsButton = (ImageButton) view.findViewById(R.id.contactsButton);
        sendButton = (Button) view.findViewById(R.id.sendButton);

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!arrayList.isEmpty()) {
                    sendButton.setEnabled(false);
                    //sendButton.setText("...Sending");
                    undo = false;

                    Snackbar snackbar = Snackbar
                            .make(sendButton, "Sending Message", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
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
                                    thisMessage = messageET.getEditableText().toString();
                                    //thisMessage = messageET.getText().toString();
                                    String user_id;
                                    // Temp
                                    user_id = userID.getValue();
                                    String type = "post_contact";

                                    for (int i = 0; i < arrayList.size(); i++) {
                                        String str = arrayList.get(i);
                                        String delims = ":";
                                        String[] tokens = str.split(delims);
                                        contact_name = tokens[0].toString();
                                        contact_number = tokens[1].toString().replaceAll("[-+ ().^:,]", "");

                                        ContactWorker contactWorker = new ContactWorker(getContext());
                                        contactWorker.execute(type, user_id, contact_name, contact_number);

                                    }

                                    sendButton.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //sendButton.setText("Send");
                                            Snackbar snackbar1 = Snackbar
                                                    .make(sendButton, "Sent", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                            arrayList.clear();
                                            adapter.notifyDataSetChanged();
                                        }
                                    }, 5000);

                                    //Utils.showToast(getActivity().getApplicationContext(), arrayList.toString());
                                }
                            }
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            sendButton.setEnabled(true);
                        }
                    });
                }
            }
        });

        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        contactsList.setAdapter(adapter);

        removeListItem();

        handler = new Handler(callback);
        timer.schedule(new SmallDelay(), 100);

        // Inflate the layout for this fragment
        return view;
    }

    class SmallDelay extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if(msgGlobal.getValue() != null) {
                messageET.setText(msgGlobal.getValue());
                msgGlobal.setValue(null);
            }
            return true;
        }
    };

    private static CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();

    boolean isASCII(String str){
        return encoder.canEncode(str);
    }


    public void removeListItem(){
        if(arrayList != null) {
            contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Snackbar snackbar = Snackbar
                            .make(contactsList, "Delete this contact?", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .setAction("Delete", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    arrayList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    snackbar.show();
                }
            });
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == getActivity().RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY);
            //(new normalizePhoneNumberTask()).execute(cursor.getString(column));
            //Log.d("phone number", cursor.getString(column));

            //Utils.showToast(getActivity().getApplicationContext(), cursor.getString(name));
            if(!arrayList.contains(cursor.getString(name)+": "+ cursor.getString(number)))
                arrayList.add(cursor.getString(name) + ": " + cursor.getString(number));
            else
                Utils.showToast(getActivity().getApplicationContext(),"Contact already selected.");
            adapter.notifyDataSetChanged();
        }
    }

    //Content Background Worker

    public class ContactWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;
        String this_name, this_number;
        String contactID = "";

        ContactWorker(Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
        /* --------- Local Host -------------------
        String postContact_url = "http://10.0.2.2/send_contacts.php";
        */
            String postContact_url = "http://wesmd.net/letsmeet/send_contacts.php";

            if(type.equals("post_contact")){
                try {
                    String user_id = params[1];
                    this_name = params[2];
                    this_number = params[3];
                    URL url = new URL(postContact_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                            +URLEncoder.encode("contact_name", "UTF-8")+"="+URLEncoder.encode(this_name,"UTF-8")+"&"
                            +URLEncoder.encode("contact_number", "UTF-8")+"="+URLEncoder.encode(this_number,"UTF-8");
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
            String delims = "=";
            String[] tokens = result.split(delims);
            if(tokens.length > 1) {
                contactID = tokens[1].toString();
            }

            /*-------------Emulator Numbers--------------
            sendSMS("5554","Hello!" + thisMessage);
            sendSMS("5556", "Hello " + contact_name);
            */

            if(contactID != null)
                sendSMS(this_number, thisMessage + " http://wesmd.net/letsmeet.php?id=" + userID.getValue() + "&c=" + contactID);
        }

        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
    }



}
