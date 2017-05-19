package in.foodtalk.privilege;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import in.foodtalk.privilege.app.DatabaseHandler;

public class SplashActivity extends AppCompatActivity {

    DatabaseHandler db;
    private static int SPLASH_TIME_OUT = 1500;
    String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = new DatabaseHandler(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = db.getRowCount();
                Intent i;
                if (count > 0){
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }else {
                    i = new Intent(SplashActivity.this, Splash_activity.class);
                }
                Log.d(TAG, "count: "+ count);
                Log.d(TAG, "intent: "+ i.getPackage());
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
