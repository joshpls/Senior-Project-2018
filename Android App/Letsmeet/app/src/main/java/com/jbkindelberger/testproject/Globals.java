package com.jbkindelberger.testproject;

/**
 * Created by joshu on 3/30/2018.
 */
import android.app.Application;

public class Globals {


    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private String notification_index;


    private Globals() {

    }


    public String getValue() {
        return notification_index;
    }


    public void setValue(String notification_index) {
        this.notification_index = notification_index;
    }



}