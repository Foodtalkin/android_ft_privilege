package in.foodtalk.privilege;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginOtp extends AppCompatActivity implements View.OnTouchListener {


    Typeface typeface;
    TextView key1, key2, key3 ,key4, key5, key6, key7, key8, key9, key0;
    TextView tvOtp1, tvOtp2, tvOtp3, tvOtp4;
    ImageView keyBack;
    final String TAG = "LoginOtp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/AbrilFatface_Regular.ttf");
        actionBar();

        key1 = (TextView) findViewById(R.id.key1);
        key2 = (TextView) findViewById(R.id.key2);
        key3 = (TextView) findViewById(R.id.key3);
        key4 = (TextView) findViewById(R.id.key4);
        key5 = (TextView) findViewById(R.id.key5);
        key6 = (TextView) findViewById(R.id.key6);
        key7 = (TextView) findViewById(R.id.key7);
        key8 = (TextView) findViewById(R.id.key8);
        key9 = (TextView) findViewById(R.id.key9);
        key0 = (TextView) findViewById(R.id.key0);
        keyBack = (ImageView) findViewById(R.id.key_back);

        tvOtp1 = (TextView) findViewById(R.id.tv_otp1);
        tvOtp2 = (TextView) findViewById(R.id.tv_otp2);
        tvOtp3 = (TextView) findViewById(R.id.tv_otp3);
        tvOtp4 = (TextView) findViewById(R.id.tv_otp4);

        key1.setOnTouchListener(this);
        key2.setOnTouchListener(this);
        key3.setOnTouchListener(this);
        key4.setOnTouchListener(this);
        key5.setOnTouchListener(this);
        key6.setOnTouchListener(this);
        key7.setOnTouchListener(this);
        key8.setOnTouchListener(this);
        key9.setOnTouchListener(this);
        key0.setOnTouchListener(this);
        keyBack.setOnTouchListener(this);
    }

    private void actionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_title, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        TextView title = (TextView) mCustomView.findViewById(R.id.title_text);
        title.setText("LoginOtp");
        title.setTypeface(typeface);
    }
    private void gotoHome(){
        Intent intent = new Intent(LoginOtp.this, MainActivity.class);
        startActivity(intent);
    }
    private void typeOtpAdd(String value){
        if (tvOtp1.length() == 0){
            tvOtp1.setText(value);
        }else if (tvOtp2.length() == 0){
            tvOtp2.setText(value);
        }else if (tvOtp3.length() == 0 ){
            tvOtp3.setText(value);
        }else if (tvOtp4.length() == 0){
            tvOtp4.setText(value);
            gotoHome();
        }

        if (value.equals("")){
            if (tvOtp4.length() > 0){
                tvOtp4.setText(value);
            }else if (tvOtp3.length() > 0){
                tvOtp3.setText(value);
            }else if (tvOtp2.length() > 0 ){
                tvOtp2.setText(value);
            }else if (tvOtp1.length() > 0){
                tvOtp1.setText(value);
            }
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.key1:
                        typeOtpAdd("1");
                        Log.d(TAG, "1");
                        break;
                    case R.id.key2:
                        typeOtpAdd("2");
                        Log.d(TAG, "2");
                        break;
                    case R.id.key3:
                        typeOtpAdd("3");
                        Log.d(TAG, "3");
                        break;
                    case R.id.key4:
                        typeOtpAdd("4");
                        Log.d(TAG, "4");
                        break;
                    case R.id.key5:
                        typeOtpAdd("5");
                        Log.d(TAG, "5");
                        break;
                    case R.id.key6:
                        typeOtpAdd("6");
                        Log.d(TAG, "6");
                        break;
                    case R.id.key7:
                        typeOtpAdd("7");
                        Log.d(TAG, "7");
                        break;
                    case R.id.key8:
                        typeOtpAdd("8");
                        Log.d(TAG, "8");
                        break;
                    case R.id.key9:
                        typeOtpAdd("9");
                        Log.d(TAG, "9");
                        break;
                    case R.id.key0:
                        typeOtpAdd("0");
                        Log.d(TAG, "0");
                        break;
                    case R.id.key_back:
                        typeOtpAdd("");
                        Log.d(TAG, "back");
                        break;
                }
                break;
        }
        return false;
    }
}
