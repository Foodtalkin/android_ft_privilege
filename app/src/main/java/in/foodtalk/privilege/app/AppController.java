package in.foodtalk.privilege.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.instamojo.android.Instamojo;

import org.json.JSONObject;

import in.foodtalk.privilege.helper.ParseUtils;

/**
 * Created by RetailAdmin on 03-05-2017.
 */

public class AppController extends Application {
    private static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    private ParseUtils parseUtils;

    public FirebaseAnalytics firebaseAnalytics;





    //-----global vars-------------
    public String restaurantName;
    public String rOneLiner;

    public String sessionId;
    public JSONObject loginResponse;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Instamojo.initialize(this);
        Instamojo.setLogLevel(Log.DEBUG);

        mInstance = this;

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        parseUtils.registerParse(this);


    }

    public void logEvent(int id, String name, String type){
        // Obtain the Firebase Analytics instance.


        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);

        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }



    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        Log.i("AppController","requestQueue: "+tag);
        //sendEventByTag(tag);

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
