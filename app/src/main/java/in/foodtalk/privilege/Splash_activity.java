package in.foodtalk.privilege;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Splash_activity extends AppCompatActivity implements View.OnTouchListener {

    TextView txtLogo, txtLogin, txtExplore;
    final String TAG = "Splash_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        txtLogo = (TextView) findViewById(R.id.txt_logo);
        txtLogin = (TextView) findViewById(R.id.txt_login);
        txtExplore = (TextView) findViewById(R.id.txt_explore);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/futura_bold.otf");

        txtExplore.setTypeface(typeface1);
        txtLogin.setTypeface(typeface1);
        txtLogo.setTypeface(typeface);

        txtLogin.setOnTouchListener(this);
        txtExplore.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.txt_login:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        gotoLogin();
                        Log.d(TAG, "Login");
                        break;
                }
                break;
            case R.id.txt_explore:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        gotoMain();
                        Log.d(TAG, "Main");
                        break;
                }
                break;
        }
        return false;
    }

    private void gotoLogin(){
        Intent intent = new Intent(Splash_activity.this, Login.class);
        startActivity(intent);
    }
    private void gotoMain(){
        Intent intent = new Intent(Splash_activity.this, MainActivity.class);
        startActivity(intent);
    }
}
