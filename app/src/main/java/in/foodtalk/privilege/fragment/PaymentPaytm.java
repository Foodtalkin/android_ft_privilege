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

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;

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

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("paytmOrder")){
                Log.d(TAG, "response: "+ response);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
