package com.jbkindelberger.testproject;

/**
 * Created by joshu on 3/30/2018.
 */

public class GlobalMessage {

    private static GlobalMessage instance = new GlobalMessage();

    // Getter-Setters
    public static GlobalMessage getInstance() {
        return instance;
    }

    public static void setInstance(GlobalMessage instance) {
        GlobalMessage.instance = instance;
    }

    private String notification_index;


    private GlobalMessage() {

    }


    public String getValue() {
        return notification_index;
    }


    public void setValue(String notification_index) {
        this.notification_index = notification_index;
    }



}
