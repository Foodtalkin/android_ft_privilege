package in.foodtalk.privilege.fragment.experiences;

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

import com.android.volley.Request;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.Convert;

/**
 * Created by RetailAdmin on 14-11-2017.
 */

public class ExpePayment extends Fragment implements View.OnTouchListener, ApiCallback {
    View layout;

    TextView tvFoodtalk, tvFoodtalk1, btnRetry, btnDone, tvSuccess, tvTrnId;

    RelativeLayout errorView, successView, loaderView;

    DatabaseHandler db;
    CallbackFragOpen callbackFragOpen;

    String transactionId;





    public JSONObject infoObj;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.payment_expe, container, false);
        errorView = (RelativeLayout) layout.findViewById(R.id.error_view);
        successView = (RelativeLayout) layout.findViewById(R.id.success_view);
        loaderView = (RelativeLayout) layout.findViewById(R.id.loader_view);
        tvFoodtalk = (TextView) layout.findViewById(R.id.txt_foodtalk);
        tvFoodtalk1 = (TextView) layout.findViewById(R.id.txt_foodtalk1);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        btnDone = (TextView) layout.findViewById(R.id.btn_done);

        tvTrnId = (TextView) layout.findViewById(R.id.tv_trn_id);

        tvSuccess = (TextView) layout.findViewById(R.id.tv_success);

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
        try {
            getInfoToPayent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return layout;
    }
    String sId;
    private void getInfoToPayent() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        sId = AppController.getInstance().sessionId;
        setScreen("loader");
        //setScreen("success");
        Log.d("infoObj", infoObj.toString());
        try {
            jsonObject.put("total_tickets", infoObj.getString("totalTickets"));
            jsonObject.put("non_veg",infoObj.getString("nonVegSeats"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.URL_EXPERIENCES+"/"+infoObj.getString("expeId")+"/order?sessionid="+sId, "paytmOrder", this);
    }

    private void paymentStatus(String orderId){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCES+"/orderstatus/"+orderId+"?sessionid="+sId, "paymentStatus", this);
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

            try {
                tvSuccess.setText("You have successfully purchased "+infoObj.getString("totalTickets")+" tickets for event "+infoObj.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

           /*Answers.getInstance().logPurchase(new urchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(1200))
                    .putCurrency(Currency.getInstance("INR"))
                    .putItemName("Privilege Membership")
                    //.putItemType("Apparel")
                    //.putItemId("sku-350")P
                    .putSuccess(true));*/

        }else if (screen.equals("loader")){
            errorView.setVisibility(View.GONE);
            successView.setVisibility(View.GONE);
            loaderView.setVisibility(View.VISIBLE);
        }
    }

    private void startPayment(JSONObject response) throws JSONException {
        PaytmPGService Service = null;
        // Service = PaytmPGService.getStagingService();

        Service = PaytmPGService.getProductionService();

       // Map<String, Object> paramMap = Convert.jsonToMap(response.getJSONObject("result"));

        /*Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , response.getJSONObject("result").getString("MID"));
        paramMap.put( "ORDER_ID" , response.getJSONObject("result").getString("ORDER_ID"));
        paramMap.put( "CUST_ID" , response.getJSONObject("result").getString("CUST_ID"));
        paramMap.put( "INDUSTRY_TYPE_ID" , response.getJSONObject("result").getString("INDUSTRY_TYPE_ID"));
        paramMap.put( "CHANNEL_ID" , response.getJSONObject("result").getString("CHANNEL_ID"));
        paramMap.put( "TXN_AMOUNT" , response.getJSONObject("result").getString("TXN_AMOUNT"));
        paramMap.put( "WEBSITE" , response.getJSONObject("result").getString("WEBSITE"));
        paramMap.put( "CALLBACK_URL" , response.getJSONObject("result").getString("CALLBACK_URL"));*/

       // Log.d("convert to map", Convert.jsonToMap(response.getJSONObject("result")).toString());

        //PaytmOrder Order = new PaytmOrder(paramMap);
        PaytmOrder Order = new PaytmOrder(Convert.jsonToMap(response.getJSONObject("result")));
        Service.initialize(Order, null);

        Service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {
                if (bundle.getString("STATUS").equals("TXN_SUCCESS")){
                    paymentStatus(bundle.getString("ORDERID"));
                }else {
                    setScreen("retry");
                }
            }

            @Override
            public void networkNotAvailable() {
                setScreen("retry");
            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                setScreen("retry");
            }

            @Override
            public void onBackPressedCancelTransaction() {
                setScreen("retry");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                setScreen("retry");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_retry:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        try {
                            getInfoToPayent();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Log.d(TAG, "payment retry");
                        break;
                }
                break;
            case R.id.btn_done:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("ticketsFrag","");
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d("apiResponse", "tag: "+tag+" : "+response);
        if (tag.equals("paytmOrder")){
            if (response != null){
                Log.d("paytm order object", response+"");
                try {
                    if (response.getString("status").equals("OK")){
                        startPayment(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (tag.equals("paymentStatus")){
            Log.d("paymentStatus response", response+"");
           if (response != null){
               try {
                   if (response.getString("status").equals("OK")){
                       setScreen("success");
                       tvTrnId.setText("TRN ID - "+response.getJSONObject("result").getString("txn_id"));
                   }else {
                       setScreen("retry");
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }else {
               Log.e("response", "null");
           }

        }
    }
}
