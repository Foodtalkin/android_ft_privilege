package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import in.foodtalk.privilege.MainActivity;
import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.SaveLogin;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by RetailAdmin on 18-08-2017.
 */

public class PaymentPaytm extends Fragment implements ApiCallback, View.OnTouchListener {

    View layout;
    String TAG;

    TextView tvFoodtalk, tvFoodtalk1, btnRetry, btnDone;

    RelativeLayout errorView, successView, loaderView;

    DatabaseHandler db;
    CallbackFragOpen callbackFragOpen;
    String paymentId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.URL_PAYTM_ORDER+"?sessionid="+sId, "paytmOrder", this);
    }
    private void subscribe(String orderId){
        JSONObject jsonObject = new JSONObject();
        String sId = AppController.getInstance().sessionId;
        setScreen("loader");
        //setScreen("success");
        try {
            jsonObject.put("order_id", orderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.URL_PAYTM_SUBSCRIBE+"?sessionid="+sId, "subscribe", this);
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

            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(1200))
                    .putCurrency(Currency.getInstance("INR"))
                    .putItemName("Privilege Membership")
                    //.putItemType("Apparel")
                    //.putItemId("sku-350")
                    .putSuccess(true));

        }else if (screen.equals("loader")){
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.GONE);
            loaderView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("paytmOrder")){
                Log.d(TAG, "response: "+ response);
                try {
                    if (response.getString("status").equals("OK")){
                        startPayment(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (tag.equals("subscribe")){
                Log.d(TAG,"response: "+response);
                try {
                    if (response.getString("status").equals("OK")){
                        setScreen("success");
                        Log.d(TAG,"savedResponse: "+AppController.getInstance().loginResponse);
                        //SaveLogin.addUser(getActivity(), response, "");
                        // int amount = Integer.parseInt(response.getJSONObject("result").getString("amount"));
                        int amount = 1200;
                        Log.d(TAG,"payment success:"+ amount);
                        //-------fbEvents--
                        Bundle params = new Bundle();
                        params.putString("status", "success");
                        params.putInt("Amount",amount);
                        AppController.getInstance().fbLogEvent("purchase", params);
                        saveUser(response);
                    }else {
                        setScreen("error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if (tag.equals("paytmOrder")){
            Log.e(TAG, "retry");
            setScreen("retry");
        }
    }
    private void saveUser(JSONObject response){
        if (db.getRowCount() > 0) {
            JSONArray subscription;
            try {
                subscription = new JSONArray(db.getUserDetails().get("subscription"));
                if (subscription.getJSONObject(0).getString("subscription_type_id").equals("3")) {
                    //callbackFragOpen.openFrag("paymentFlow","");
                    db.updateSubscription(db.getUserDetails().get("userId"), response.getJSONObject("result").getJSONArray("subscription").toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
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
    }

    private void startPayment(JSONObject response) throws JSONException {
        PaytmPGService Service = null;
        Service = PaytmPGService.getStagingService();

        //Service = PaytmPGService.getProductionService();

        //Create new order Object having all order information.
        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , response.getJSONObject("result").getString("MID"));
        paramMap.put( "ORDER_ID" , response.getJSONObject("result").getString("ORDER_ID"));
        paramMap.put( "CUST_ID" , response.getJSONObject("result").getString("CUST_ID"));
        paramMap.put( "INDUSTRY_TYPE_ID" , response.getJSONObject("result").getString("INDUSTRY_TYPE_ID"));
        paramMap.put( "CHANNEL_ID" , response.getJSONObject("result").getString("CHANNEL_ID"));
        paramMap.put( "TXN_AMOUNT" , response.getJSONObject("result").getString("TXN_AMOUNT"));
        paramMap.put( "WEBSITE" , response.getJSONObject("result").getString("WEBSITE"));
        paramMap.put( "CALLBACK_URL" , response.getJSONObject("result").getString("CALLBACK_URL"));
        //paramMap.put( "EMAIL" , "abc@gmail.com");
       // paramMap.put( "MOBILE_NO" , "9999999999");
        paramMap.put( "CHECKSUMHASH" , response.getJSONObject("result").getString("CHECKSUMHASH"));

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        //Start the Payment Transaction. Before starting the transaction ensure that initialize method is called.

        Service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                Log.d("LOG", "UI Error Occur.");
                //Toast.makeText(getApplicationContext(), " UI Error Occur. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionResponse(Bundle inResponse) {
                Log.d("LOG", "Payment Transaction : " + inResponse);
                //Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Status: "+inResponse.getString("STATUS"));
                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                    subscribe(inResponse.getString("ORDERID"));
                }else {
                    setScreen("retry");
                }
            }

            @Override
            public void networkNotAvailable() {
                Log.e("LOG", "UI Error Occur.");
                setScreen("retry");
                //Toast.makeText(getApplicationContext(), " UI Error Occur. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                Log.e("LOG", "UI Error Occur.");
                //Toast.makeText(getApplicationContext(), " Severside Error "+ inErrorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode,
                                              String inErrorMessage, String inFailingUrl) {
                Log.e("LOG", "onErrorLoadingWebPage "+inErrorMessage+" : "+inFailingUrl);
                setScreen("retry");
            }
            @Override
            public void onBackPressedCancelTransaction() {
// TODO Auto-generated method stub
                Log.e("LOG", "onBackPressedCancelTransaction ");
                setScreen("retry");
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Log.e("LOG", "Payment Transaction Failed " + inErrorMessage);
                setScreen("retry");
                //Toast.makeText(getApplicationContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
            }

        });
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
