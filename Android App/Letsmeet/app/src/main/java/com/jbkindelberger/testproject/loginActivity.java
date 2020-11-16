package com.jbkindelberger.testproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class loginActivity extends AppCompatActivity {

    EditText input_username, input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);
    }

    public void onLogin(View view) {

        String username = input_username.getText().toString();
        String password = input_password.getText().toString();
        String type = "login";

        if (username.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(view, "Username can't be Empty.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getColor(R.color.btnLogout))
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        } else if (password.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(view, "Password can't be Empty.", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getColor(R.color.btnLogout))
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        }
        else
        {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, username, password);

            Snackbar snackbar = Snackbar
                    .make(view, "Logging In...", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }
    public void onReg(View view){
        Intent i = new Intent(this, registerActivity.class);
        startActivity(i);
    }

    public void onAbout(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }
}
