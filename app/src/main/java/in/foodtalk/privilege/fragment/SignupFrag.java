package in.foodtalk.privilege.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.Login;
import in.foodtalk.privilege.MainActivity;
import in.foodtalk.privilege.R;
import in.foodtalk.privilege.Splash_activity;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.ToastShow;

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

    RelativeLayout progressBar;

    View lineName, lineEmail, linePhone;
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

        progressBar = (RelativeLayout) layout.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        lineName = layout.findViewById(R.id.line_name);
        lineEmail = layout.findViewById(R.id.line_email);
        linePhone = layout.findViewById(R.id.line_phone);
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
                    progressBar.setVisibility(View.VISIBLE);
                    linePhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warm_grey));
                }else {
                    linePhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                    Log.d(TAG, "phone number length");
                    lineName.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                    ToastShow.showToast(getActivity(), "Enter valid phone no.");
                }
                lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warm_grey));
            }else {
                Log.d(TAG, "email id problem");
                lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                ToastShow.showToast(getActivity(), "Enter valid email");
            }
            lineName.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warm_grey));
        }else {
            Log.d(TAG, "name length less then 2");
            lineName.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            ToastShow.showToast(getActivity(), "Enter valid name");
        }
    }
    private void gotoLogin(JSONObject response) throws JSONException {
        progressBar.setVisibility(View.GONE);
        String message = response.getString("message");
        String status = response.getString("status");
        if (status.equals("OK")){
            callbackFragOpen.openFrag("otpVerify",phone);
        }else {
            String error = response.getJSONObject("result").getString("error");
            if (error.equals("email")){
                ToastShow.showToast(getActivity(), message);
                lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                linePhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warm_grey));
            }else if (error.equals("phone")){
                //ToastShow.showToast(getActivity(), message);
                numberAlreadyRegisterDialog();
                lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warm_grey));
                linePhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            }
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
        }else {
            progressBar.setVisibility(View.GONE);
            ToastShow.showToast(getActivity(),"Check internet connection");
        }
    }

    Dialog dialog;
    private void numberAlreadyRegisterDialog(){
        // custom dialog

        dialog = new Dialog(getActivity());
        //dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.number_already_register_dialog);
        TextView msg = (TextView) dialog.findViewById(R.id.tv_msg);
        msg.setText("Looks like "+etPhone.getText().toString()+" is already registered with us. Would you like to login?");

        TextView login = (TextView) dialog.findViewById(R.id.btn_login);
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.putExtra("phone", etPhone.getText().toString());
                startActivity(intent);
                //tvVeg.setText("No");
            }
        });
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Intent intent = new Intent(getActivity(), Splash_activity.class);
                //startActivity(intent);
                //logOut();
                //tvVeg.setText("Yes");
            }
        });

        dialog.show();
    }
}
