package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.Login;
import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.CallbackKeypad;
import in.foodtalk.privilege.library.Keypad;
import in.foodtalk.privilege.models.LoginValue;

/**
 * Created by RetailAdmin on 18-05-2017.
 */

public class OtpVerifyFrag extends Fragment implements CallbackKeypad, ApiCallback {
    View layout;

    TextView tvOtp1, tvOtp2, tvOtp3, tvOtp4;

    CallbackFragOpen callbackFragOpen;
    String TAG = OtpVerifyFrag.class.getSimpleName();

    public String phone;

    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.otp_verify, container, false);

        Keypad keypad = new Keypad(layout, this);
        callbackFragOpen = (CallbackFragOpen) getActivity();

        db = new DatabaseHandler(getActivity());

        tvOtp1 = (TextView) layout.findViewById(R.id.tv_otp1);
        tvOtp2 = (TextView) layout.findViewById(R.id.tv_otp2);
        tvOtp3 = (TextView) layout.findViewById(R.id.tv_otp3);
        tvOtp4 = (TextView) layout.findViewById(R.id.tv_otp4);
        return layout;
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
        }else if (tvOtp3.length() == 0 ){
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

    private void gotoHome(JSONObject response) throws JSONException {
        String message = response.getString("message");
        if (message.equals("OTP Accepted")){
            callbackFragOpen.openFrag("homeFrag","");
            LoginValue loginValue = new LoginValue();
            loginValue.sId = response.getJSONObject("result").getJSONObject("session").getString("session_id");
            loginValue.rToken = response.getJSONObject("result").getJSONObject("session").getString("refresh_token");
            loginValue.uId = response.getJSONObject("result").getJSONObject("session").getString("user_id");
            loginValue.createAt = response.getJSONObject("result").getJSONObject("session").getString("created_at");
            loginValue.updateAt = response.getJSONObject("result").getJSONObject("session").getString("updated_at");
            db.addUser(loginValue);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("otpVerify")){
                Log.d(TAG,"response: "+ response);
                try {
                    gotoHome(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
