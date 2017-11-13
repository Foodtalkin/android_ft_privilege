package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Currency;

import in.foodtalk.privilege.MainActivity;
import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.PayNow;
import in.foodtalk.privilege.library.SaveLogin;
import in.foodtalk.privilege.library.ToastShow;

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
    String paymentId;

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

        btnRetry.setOnTouchListener(this);

        btnDone.setOnTouchListener(this);
        btnDone.setOnTouchListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

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
            checkPaymentStatus(this.paymentId);
        }else if (status.equals("error")){
            setScreen("retry");
        }
    }

    private void setScreen(String screen){
        if (screen.equals("retry")){
            errorView.setVisibility(View.VISIBLE);
            successView.setVisibility(View.GONE);
            loaderView.setVisibility(View.GONE);
            //-------fbEvents--
            Bundle params = new Bundle();
            params.putString("status", "faild");
            AppController.getInstance().fbLogEvent("purchase", params);
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
        String sId = AppController.getInstance().sessionId;
        try {
            jsonObject.put("payment_id",paymentId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.SUBSCRIPTION+"?sessionid="+sId, "subscription", this);
    }

    private void getInfoToPayent(){
        JSONObject jsonObject = new JSONObject();
        String sId = AppController.getInstance().sessionId;
        setScreen("loader");
        //setScreen("success");
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

        paymentId = paymentid;
       // PayNow payNow = new PayNow(getActivity());
       // payNow.paymentWithOrder(accessToken, orderId);
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
                    }else {
                        ToastShow.showToast(getActivity(), response.getJSONObject("result").getJSONArray("phone").getJSONArray(0).toString());
                        getFragmentManager().popBackStack();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (tag.equals("subscription")){
                Log.d(TAG, "response: "+ response);

                try {
                    if (response.getString("status").equals("OK")){
                        setScreen("success");
                        Log.d(TAG,"savedResponse: "+AppController.getInstance().loginResponse);
                        //SaveLogin.addUser(getActivity(), response, "");
                       // int amount = Integer.parseInt(response.getJSONObject("result").getString("amount"));
                        int amount = 00;
                        Log.d(TAG,"payment success:"+ amount);
                        //-------fbEvents--
                        Bundle params = new Bundle();
                        params.putString("status", "success");
                        params.putInt("Amount",amount);
                        AppController.getInstance().fbLogEvent("purchase", params);
                        saveUser(response);

                        //----fabric event----
                      Answers.getInstance().logPurchase(new PurchaseEvent()
                                .putItemPrice(BigDecimal.valueOf(1200))
                                .putCurrency(Currency.getInstance("INR"))
                                .putItemName("Privilege Membership")
                                .putItemType("Subscription")
                                .putSuccess(true));
                    }else {
                        setScreen("error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Log.e(TAG, "retry");
            setScreen("retry");
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_retry:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        getInfoToPayent();
                        Log.d(TAG, "payment retry");
                        break;
                }
                break;
            case R.id.btn_done:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("homeFrag","");
                        //((MainActivity)getActivity()).refreshUI();
                        break;
                }
                break;
        }
        return false;
    }
}
