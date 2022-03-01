package com.neko2mizu.instaounce;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    private static final String APP_ID = "WMoaIeVL6GLL0L7aZSTqjBIVlPSCuoBsuEn9XJVD";
    private static final String CLIENT_KEY = "aAMo0IadU6X3wpmz4TInzc44VQCpTmUQ170p7YRp";

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();



        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APP_ID)
                .clientKey(CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo","bar");
        testObject.saveInBackground();
    }
}