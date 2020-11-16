package com.jbkindelberger.testproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class registerActivity extends AppCompatActivity {
    EditText register_username, register_password, register_number, register_email, register_firstName, register_lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_username = (EditText) findViewById(R.id.register_username);
        register_password = (EditText) findViewById(R.id.register_password);
        register_email = (EditText) findViewById(R.id.register_email);
        register_number = (EditText) findViewById(R.id.register_number);
        register_firstName = (EditText) findViewById(R.id.register_firstName);
        register_lastname = (EditText) findViewById(R.id.register_lastName);
    }

    public void onRegister(View view){
        String username = register_username.getText().toString();
        String password = register_password.getText().toString();
        String email = register_email.getText().toString();
        String number = register_number.getText().toString();
        String firstName = register_firstName.getText().toString();
        String lastName = register_lastname.getText().toString();
        Boolean isTrue = true;

        if(username.isEmpty()) {
            register_username.setHint("Username can't be blank.");
            register_username.setHintTextColor(getColor(R.color.btnLogout));
            isTrue = false;
        }
        if(password.isEmpty())
        {
            register_password.setHint("Password can't be blank.");
            register_password.setHintTextColor(getColor(R.color.btnLogout));
            isTrue = false;
        }
        if(number.isEmpty())
        {
            register_number.setHint("Number can't be blank.");
            register_number.setHintTextColor(getColor(R.color.btnLogout));
            isTrue = false;
        }
        if(firstName.isEmpty())
        {
            register_firstName.setHint("First Name can't be blank.");
            register_firstName.setHintTextColor(getColor(R.color.btnLogout));
            isTrue = false;
        }
        if(lastName.isEmpty())
        {
            register_lastname.setHint("Last Name can't be blank.");
            register_lastname.setHintTextColor(getColor(R.color.btnLogout));
            isTrue = false;
        }
        if(isTrue) {
            String type = "register";

            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, username, password, email, number, firstName, lastName);
        }
    }
}
