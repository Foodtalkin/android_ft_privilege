package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.CallbackKeypad;
import in.foodtalk.privilege.library.Keypad;
import in.foodtalk.privilege.library.ToastShow;

/**
 * Created by RetailAdmin on 10-05-2017.
 */

public class RestaurantPin extends Fragment implements CallbackKeypad, ApiCallback {
    View layout;
    TextView tvOtp1, tvOtp2, tvOtp3, tvOtp4;

    CallbackFragOpen callbackFragOpen;

    public String jsonString;

    String TAG = RestaurantPin.class.getSimpleName();

    DatabaseHandler db;
    String sessionId;

    String offerId;
    String outletId;
    String offerRedeem;

    LinearLayout pinHolder;

    TextView tvError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.restaurant_pin, container, false);

        Keypad keypad = new Keypad(layout, this);
        callbackFragOpen = (CallbackFragOpen) getActivity();

        db = new DatabaseHandler(getActivity());
        sessionId = db.getUserDetails().get("sessionId");

        tvError = (TextView) layout.findViewById(R.id.tv_error);
        tvError.setVisibility(View.GONE);


        tvOtp1 = (TextView) layout.findViewById(R.id.tv_otp1);
        tvOtp2 = (TextView) layout.findViewById(R.id.tv_otp2);
        tvOtp3 = (TextView) layout.findViewById(R.id.tv_otp3);
        tvOtp4 = (TextView) layout.findViewById(R.id.tv_otp4);

        pinHolder = (LinearLayout) layout.findViewById(R.id.pin_holder);

        //Log.d(TAG, jsonString);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            offerId = jsonObject.getString("offer_id");
            outletId = jsonObject.getString("outlet_id");
            offerRedeem = jsonObject.getString("offers_redeemed");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("RestaurantPin","onResume");
    }

    @Override
    public void keyRead(String value) {
        typeOtpAdd(value);
    }

    private void redeemApi(){
        JSONObject jsonObject = new JSONObject();
        String pin  = tvOtp1.getText().toString()
                +tvOtp2.getText().toString()
                +tvOtp3.getText().toString()
                +tvOtp4.getText().toString();

        try {
            jsonObject.put("outlet_id", outletId);
            jsonObject.put("offer_id", offerId);
            jsonObject.put("offers_redeemed", offerRedeem);
            jsonObject.put("pin", pin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.REDEEM+"?sessionid="+sessionId, "redeem", this);
    }

    private void animatePinHolder(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.shaking_anim);
        pinHolder.setAnimation(animation);
        animation.startNow();
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
            //if (!tvOtp1.equals("") && !tvOtp2.equals("") && !tvOtp3.equals("") && !tvOtp4.equals("")){
                redeemApi();
                Log.d(TAG,"call redeem api");
            //}
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
            tvError.setVisibility(View.GONE);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("redeem")){
                try {
                    if (response.getString("status").equals("OK")){
                        callbackFragOpen.openFrag("successFrag", "");
                        tvError.setVisibility(View.GONE);
                    }else {
                        ToastShow.showToast(getActivity(), "Invalid PIN");
                        tvError.setVisibility(View.VISIBLE);
                        animatePinHolder();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "response: "+ response);
    }
}
