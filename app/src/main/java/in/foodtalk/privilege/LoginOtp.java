package in.foodtalk.privilege;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.library.SaveLogin;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.LoginValue;

public class LoginOtp extends AppCompatActivity implements View.OnTouchListener, ApiCallback {


    Typeface typeface;
    TextView key1, key2, key3 ,key4, key5, key6, key7, key8, key9, key0;
    TextView tvOtp1, tvOtp2, tvOtp3, tvOtp4;
    ImageView keyBack;
    DatabaseHandler db;
    final String TAG = "LoginOtp";

    TextView btnResendOtp;

    TextView tvTimer, tvMsg;

    LinearLayout otpBox;

    Animation shakingAni;


    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/AbrilFatface_Regular.ttf");
        actionBar();

        shakingAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shaking_anim);
        shakingAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        otpBox = (LinearLayout) findViewById(R.id.otp_box);

        db = new DatabaseHandler(this);

        phone = getIntent().getStringExtra("phone");
        tvMsg = (TextView) findViewById(R.id.tv_msg);

        tvMsg.setText("Please enter 4 digit OTP \n sent to "+phone);

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

        btnResendOtp = (TextView) findViewById(R.id.btn_resend_otp);
        btnResendOtp.setOnTouchListener(this);

        tvTimer = (TextView) findViewById(R.id.tv_timer);

        try {
            getOtp("getOtp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        countDown();
    }

    private void countDown(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setVisibility(View.VISIBLE);
                tvTimer.setText("Request another OTP in " + millisUntilFinished / 1000+" seconds");
            }

            public void onFinish() {
                //mTextField.setText("done!");
                tvTimer.setVisibility(View.GONE);
            }
        }.start();
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
        title.setText("Login");
        title.setTypeface(typeface);
    }
    private void gotoHome(JSONObject response) throws JSONException {

        String status = response.getString("status");
        String message = response.getString("message");
        JSONArray subscription = response.getJSONObject("result").getJSONArray("subscription");
        if (status.equals("ERROR")){
            otpBox.startAnimation(shakingAni);
            ToastShow.showToast(this,"wrong OTP");
            tvOtp1.setText("");
            tvOtp2.setText("");
            tvOtp3.setText("");
            tvOtp4.setText("");
        }else {
            if (subscription.length() > 0){
                SaveLogin.addUser(this, response, "homeFrag");
            }else {
                AppController.getInstance().sessionId = response.getJSONObject("result").getJSONObject("session").getString("session_id");
                AppController.getInstance().loginResponse = response;
                paymentDueDialog();
            }
        }
        /*
        String status = response.getString("status");
        String message = response.getString("message");
        if (status.equals("OK")){
            //callbackFragOpen.openFrag("homeFrag","");
            LoginValue loginValue = new LoginValue();
            loginValue.sId = response.getJSONObject("result").getJSONObject("session").getString("session_id");
            loginValue.rToken = response.getJSONObject("result").getJSONObject("session").getString("refresh_token");
            loginValue.uId = response.getJSONObject("result").getJSONObject("session").getString("user_id");
            //loginValue.createAt = response.getJSONObject("result").getJSONObject("session").getString("created_at");
            //loginValue.updateAt = response.getJSONObject("result").getJSONObject("session").getString("updated_at");


            JSONObject result = response.getJSONObject("result");
            JSONArray subscription = result.getJSONArray("subscription");

            loginValue.name = ((result.isNull("name")) ? "N/A" : result.getString("name"));
            loginValue.email = ((result.isNull("email")) ? "N/A" : result.getString("email"));
            loginValue.phone = ((result.isNull("phone")) ? "N/A" : result.getString("phone"));
            loginValue.gender = ((result.isNull("gender")) ? "N/A" : result.getString("gender"));
            loginValue.dob = ((result.isNull("dob")) ? "N/A" : result.getString("dob"));

            loginValue.subscription = subscription.toString();
            db.addUser(loginValue);

            //name = ((result.isNull("name")) ? "N/A" : result.getString("name"));

            Intent intent = new Intent(LoginOtp.this, MainActivity.class);
            startActivity(intent);
        }else {
            Log.e(TAG, "message: "+ message);
        }*/
    }
    private void getOtp(String tag) throws JSONException {



            Log.d(TAG, "getOtp");
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("phone", phone);

            ApiCall.jsonObjRequest(Request.Method.POST, this, jsonObject, Url.GET_OTP, tag, this);

    }
    private void sendOtp(String tag) throws JSONException {
        String otp = tvOtp1.getText().toString()
                +tvOtp2.getText().toString()
                +tvOtp3.getText().toString()
                +tvOtp4.getText().toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("otp", otp);
        ApiCall.jsonObjRequest(Request.Method.POST, this, jsonObject, Url.USER_LOGIN, tag, this);

        Log.d(TAG, "sendOtp to server");
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
            try {
                sendOtp("sendOtp");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    Dialog dialog;
    private void paymentDueDialog(){
        // custom dialog

        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_due_dialog);


        TextView paynow = (TextView) dialog.findViewById(R.id.btn_paynow);
        TextView logout = (TextView) dialog.findViewById(R.id.btn_logout);
        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(LoginOtp.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.e(TAG, "flag activity clear top");
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("fragment", "paymentFlow");
                startActivity(intent);
                finish();
                //tvVeg.setText("No");
            }
        });
        // if button is clicked, close the custom dialog
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(LoginOtp.this, Splash_activity.class);
                startActivity(intent);
                //logOut();
                //tvVeg.setText("Yes");
            }
        });

        dialog.show();
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
                    case R.id.btn_resend_otp:
                        Log.d(TAG, "resendOtp");
                        try {
                            getOtp("getOtp");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        countDown();
                        break;
                }
                break;
        }
        return false;
    }
    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null ){
            if (tag.equals("getOtp")){
                Log.d(TAG, "response: "+ response);

            }
            if (tag.equals("sendOtp")){
                try {
                    gotoHome(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (tag.equals("getOtp")){
                Log.e(TAG,"check internet");
            }
        }
    }
}
