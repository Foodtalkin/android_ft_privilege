package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.Login;
import in.foodtalk.privilege.MainActivity;
import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.CallbackKeypad;
import in.foodtalk.privilege.library.Keypad;
import in.foodtalk.privilege.library.PayNow;
import in.foodtalk.privilege.library.SaveLogin;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.LoginValue;

/**
 * Created by RetailAdmin on 18-05-2017.
 */

public class OtpVerifyFrag extends Fragment implements CallbackKeypad, ApiCallback, View.OnTouchListener {
    View layout;

    TextView tvOtp1, tvOtp2, tvOtp3, tvOtp4;

    CallbackFragOpen callbackFragOpen;
    String TAG = OtpVerifyFrag.class.getSimpleName();

    public String phone;

    DatabaseHandler db;

    TextView btnResendOtp, tvTimer, tvMsg;

    LinearLayout otpBox;
    Animation shakingAni;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.otp_verify, container, false);

        hideSoftKeyboard();

        otpBox = (LinearLayout) layout.findViewById(R.id.otp_box);

        Keypad keypad = new Keypad(layout, this);
        callbackFragOpen = (CallbackFragOpen) getActivity();

        db = new DatabaseHandler(getActivity());

        shakingAni = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shaking_anim);
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

        btnResendOtp = (TextView) layout.findViewById(R.id.btn_resend_otp);
        tvTimer = (TextView) layout.findViewById(R.id.tv_timer);

        tvMsg = (TextView) layout.findViewById(R.id.tv_msg);

        tvMsg.setText("Please enter 4 digit OTP\nsent to "+phone);

        btnResendOtp.setOnTouchListener(this);

        tvOtp1 = (TextView) layout.findViewById(R.id.tv_otp1);
        tvOtp2 = (TextView) layout.findViewById(R.id.tv_otp2);
        tvOtp3 = (TextView) layout.findViewById(R.id.tv_otp3);
        tvOtp4 = (TextView) layout.findViewById(R.id.tv_otp4);
        countDown();
        return layout;
    }
    public void hideSoftKeyboard() {
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
    @Override
    public void keyRead(String value) {
        typeOtpAdd(value);
    }

    private void typeOtpAdd(String value){
        if (tvOtp1.length() == 0){
            tvOtp1.setText(value);
        }else if (tvOtp2.length() == 0){
            tvOtp2.setText(value);
        }else if (tvOtp3.length() == 0){
            tvOtp3.setText(value);
        }else if (tvOtp4.length() == 0){
            tvOtp4.setText(value);
            //gotoHome();
            try {
                sendOtp("otpVerify");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // callbackFragOpen.openFrag("successFrag", "");
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

    private void resendOtp(){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.RESEND_OTP+"/"+phone, "resendOtp", this);
    }

    private void sendOtp(String tag) throws JSONException {
        String otp = tvOtp1.getText().toString()
                +tvOtp2.getText().toString()
                +tvOtp3.getText().toString()
                +tvOtp4.getText().toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("otp", otp);
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.USER_LOGIN, tag, this);

        Log.d(TAG, "sendOtp to server");
    }
    private void getInfoToPayent(){
        JSONObject jsonObject = new JSONObject();
        String sId = db.getUserDetails().get("sessionId");
        try {
            jsonObject.put("subscription_type_id", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.SUBSCRIPTION_PYMENT+"?sessionid="+sId, "subscriptionPayment", this);
    }



    private void userLogin(JSONObject response) throws JSONException {


        String status = response.getString("status");
        String message = response.getString("message");
        if (status.equals("ERROR")){
            otpBox.startAnimation(shakingAni);
            ToastShow.showToast(getActivity(),"wrong OTP");
            tvOtp1.setText("");
            tvOtp2.setText("");
            tvOtp3.setText("");
            tvOtp4.setText("");
        }else {
            //SaveLogin.addUser(getActivity(), response, "");
            String sessionId = response.getJSONObject("result").getJSONObject("session").getString("session_id");
            AppController.getInstance().sessionId = sessionId;
            AppController.getInstance().loginResponse = response;
            if (AppController.getInstance().signuptype.equals("payment")){
                callbackFragOpen.openFrag("paymentFlow","");
                Log.d(TAG,"call payment flow");
            }else {
                Log.d(TAG, "call trial api");
                ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_TRIAL+"?sessionid="+sessionId, "trialApi", this);
            }


            Answers.getInstance().logSignUp(new SignUpEvent()
                    .putMethod("Digits")
                    .putSuccess(true));
        }
    }

    private void saveUser(JSONObject response){
        try {
            JSONObject savedResponse = AppController.getInstance().loginResponse;
            savedResponse.getJSONObject("result").getJSONArray("subscription").put(response.getJSONObject("result").getJSONArray("subscription").getJSONObject(0));
            SaveLogin.addUser(getActivity(), savedResponse, "");
            Log.d(TAG, "updated response: " +savedResponse);
            ((MainActivity)getActivity()).loginView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("otpVerify")){
                Log.d(TAG,"response: "+ response);
                try {
                    userLogin(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //getInfoToPayent();
            }
            if (tag.equals("trialApi")){
                Log.d(TAG,"response trial: "+ response);
                try {
                    if (response.getString("status").equals("OK")){
                        saveUser(response);
                        callbackFragOpen.openFrag("homeFrag","");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            /*if (tag.equals("subscriptionPayment")){
                Log.d(TAG, "response: "+ response);
                try {
                    if (response.getString("status").equals("OK")){
                        paymentProcess(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.btn_resend_otp:
                        resendOtp();
                        Log.d(TAG,"resend otp");
                        countDown();
                        break;
                }
                break;
        }
        return false;
    }
}
