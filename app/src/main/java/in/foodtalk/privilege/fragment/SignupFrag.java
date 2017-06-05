package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class SignupFrag extends Fragment implements View.OnTouchListener, ApiCallback{
    EditText etName, etEmail, etPhone;
    LinearLayout btnVerify;
    String TAG = SignupFrag.class.getSimpleName();
    CallbackFragOpen callbackFragOpen;
    View layout;

    String email;
    String name;
    String phone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.signup_frag, container, false);
        etName = (EditText) layout.findViewById(R.id.et_name);
        etEmail = (EditText) layout.findViewById(R.id.et_email);
        etPhone = (EditText) layout.findViewById(R.id.et_phone);
        btnVerify = (LinearLayout) layout.findViewById(R.id.btn_verify);
        btnVerify.setOnTouchListener(this);
        callbackFragOpen = (CallbackFragOpen) getActivity();
        return layout;
    }

    private void getOtp(String tag) throws JSONException {
        email = etEmail.getText().toString().trim();
        name = etName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        if (name.length() > 2){
            if (email.matches(getResources().getString(R.string.emailPattern))){
                if (phone.length() == 10){
                    Log.d(TAG, "getOtp");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name);
                    jsonObject.put("email", email);
                    jsonObject.put("phone", phone);
                    jsonObject.put("signup","1");
                    ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.GET_OTP, tag, this);
                }else {
                    Log.d(TAG, "phone number length");
                }
            }else {
                Log.d(TAG, "email id problem");
            }
        }else {
            Log.d(TAG, "name length less then 2");
        }
    }
    private void gotoLogin(JSONObject response) throws JSONException {
        String message = response.getString("message");
        if (message.equals("Success")){
            callbackFragOpen.openFrag("otpVerify",phone);
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_verify:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        try {
                            getOtp("getOtp");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            try {
                gotoLogin(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
