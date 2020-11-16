package com.jbkindelberger.testproject;

/**
 * Created by joshu on 4/1/2018.
 */

public class GlobalContact {

    private static GlobalContact instance = new GlobalContact();

    // Getter-Setters
    public static GlobalContact getInstance() {
        return instance;
    }

    public static void setInstance(GlobalContact instance) {
        GlobalContact.instance = instance;
    }

    private String notification_index;


    private GlobalContact() {

    }


    public String getValue() {
        return notification_index;
    }


    public void setValue(String notification_index) {
        this.notification_index = notification_index;
    }



}