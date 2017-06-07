package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.PayNow;

/**
 * Created by RetailAdmin on 06-06-2017.
 */

public class PaymentFlow extends Fragment implements ApiCallback, View.OnTouchListener {

    View layout;
    String TAG;

    TextView tvFoodtalk, tvFoodtalk1, btnRetry, btnDone;

    RelativeLayout errorView, successView, loaderView;

    DatabaseHandler db;
    CallbackFragOpen callbackFragOpen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.payment_flow, container, false);
        errorView = (RelativeLayout) layout.findViewById(R.id.error_view);
        successView = (RelativeLayout) layout.findViewById(R.id.success_view);
        loaderView = (RelativeLayout) layout.findViewById(R.id.loader_view);
        tvFoodtalk = (TextView) layout.findViewById(R.id.txt_foodtalk);
        tvFoodtalk1 = (TextView) layout.findViewById(R.id.txt_foodtalk1);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        btnDone = (TextView) layout.findViewById(R.id.btn_done);

        btnDone.setOnTouchListener(this);
        btnDone.setOnTouchListener(this);

        callbackFragOpen = (CallbackFragOpen) getActivity();

        Typeface futuraMedium = Typeface.createFromAsset(getActivity().getAssets(),"fonts/futura_medium.ttf");
        Typeface futuraBold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/futura_bold.otf");
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        tvFoodtalk.setTypeface(typeface);
        tvFoodtalk1.setTypeface(typeface);
        btnRetry.setTypeface(futuraMedium);
        btnDone.setTypeface(futuraMedium);
        db = new DatabaseHandler(getActivity());
        getInfoToPayent();
        return layout;
    }

    public void instamojoResponse(String status, String orderId, String transactionId, String paymentId){
        loaderView.setVisibility(View.GONE);
        if (status.equals("ok")){
           // setScreen("success");
            checkPaymentStatus(transactionId);
        }else if (status.equals("error")){
            setScreen("retry");
        }
    }

    private void setScreen(String screen){
        if (screen.equals("retry")){
            errorView.setVisibility(View.VISIBLE);
            successView.setVisibility(View.GONE);
            loaderView.setVisibility(View.GONE);
        }else if (screen.equals("success")){
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.VISIBLE);
            loaderView.setVisibility(View.GONE);
        }else if (screen.equals("loader")){
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.GONE);
            loaderView.setVisibility(View.VISIBLE);
        }
    }

    private void checkPaymentStatus(String paymentId){
        JSONObject jsonObject = new JSONObject();
        String sId = db.getUserDetails().get("sessionId");
        try {
            jsonObject.put("payment_id",paymentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.SUBSCRIPTION+"?sessionid="+sId, "subscription", this);
    }

    private void getInfoToPayent(){
        JSONObject jsonObject = new JSONObject();
        String sId = db.getUserDetails().get("sessionId");
        setScreen("loader");
        try {
            jsonObject.put("subscription_type_id", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.SUBSCRIPTION_PYMENT+"?sessionid="+sId, "subscriptionPayment", this);
    }

    private void paymentProcess(JSONObject response) throws JSONException {
        String accessToken = response.getJSONObject("result").getString("access_token");
        String paymentid = response.getJSONObject("result").getString("paymentid");
        String orderId = response.getJSONObject("result").getJSONObject("order").getString("order_id");
        String name = response.getJSONObject("result").getJSONObject("transaction").getString("buyer_name");
        String email = response.getJSONObject("result").getJSONObject("transaction").getString("email");
        String phone = response.getJSONObject("result").getJSONObject("transaction").getString("phone");
        String amount = response.getJSONObject("result").getJSONObject("transaction").getString("amount");
       // Log.d(TAG,"accessToken: "+accessToken+" orderId: "+orderId);
        PayNow payNow = new PayNow(getActivity());
        payNow.paymentWithOrder(accessToken, orderId);
        //payNow.payment(accessToken, paymentid, name, email, phone, amount, "membership999FT");
        //payNow.paymentWithOrder(accessToken,orderId);
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("subscriptionPayment")){
                try {
                    if (response.getString("status").equals("OK")){
                        paymentProcess(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (tag.equals("subscriptionPayment")){
                Log.d(TAG, "response: "+ response);
            }
        }else{
            setScreen("retry");
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_retry:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        getInfoToPayent();
                        break;
                }
                break;
            case R.id.btn_done:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("homeFrag","");
                        break;
                }
                break;
        }
        return false;
    }
}
