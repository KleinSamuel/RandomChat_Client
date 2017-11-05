package com.samuel.klein.randomchat.debug;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sam on 05.11.17.
 */

public class Debug {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static Date date;

    public static void print(String m){
        date = new Date();
        System.out.println("[ OK ]("+dateFormat.format(date)+"):\t"+m);
    }

}
