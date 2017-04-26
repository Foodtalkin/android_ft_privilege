package in.foodtalk.privilege;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Splash_activity extends AppCompatActivity {

    TextView txtLogo, txtLogin, txtExplore;
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

    }
}
