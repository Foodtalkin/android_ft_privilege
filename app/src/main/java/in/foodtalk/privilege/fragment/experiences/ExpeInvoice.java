package in.foodtalk.privilege.fragment.experiences;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 13-11-2017.
 */

public class ExpeInvoice extends Fragment implements ApiCallback, View.OnTouchListener {
    View layout;

    public String nonVegSeats;
    public String totalTickets;

    public JSONObject infoObj;

    DatabaseHandler db;
    LinearLayout btnPay;

    ImageView imgView;

    TextView tvTitle, tvTime, tvTime1, tvAddress, tvAddress1, tvVegNon, tvSubTotal, tvAmount, tvTax, tvFee, tvTotalAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experiences_invoice, container, false);
        imgView = (ImageView) layout.findViewById(R.id.img_view);
        tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTime = (TextView) layout.findViewById(R.id.tv_time);
        tvTime1 = (TextView) layout.findViewById(R.id.tv_time1);
        tvAddress = (TextView) layout.findViewById(R.id.tv_address);
        tvAddress1 = (TextView) layout.findViewById(R.id.tv_address1);
        tvVegNon = (TextView) layout.findViewById(R.id.tv_veg_non);
        tvSubTotal = (TextView) layout.findViewById(R.id.tv_subtotal);
        tvAmount = (TextView) layout.findViewById(R.id.tv_amount);
        tvTax = (TextView) layout.findViewById(R.id.tv_tax);
        tvFee = (TextView) layout.findViewById(R.id.tv_fee);
        tvTotalAmount = (TextView) layout.findViewById(R.id.tv_total_amount);
        btnPay = (LinearLayout) layout.findViewById(R.id.btn_pay);
        btnPay.setOnTouchListener(this);

        db = new DatabaseHandler(getActivity());

        loadData();
        return layout;
    }

    private void loadData(){
        JSONObject jsonObject = new JSONObject();
        String expeId = "";
        try {
            jsonObject.put("total_tickets",infoObj.getString("totalTickets"));
            jsonObject.put("non_veg", infoObj.getString("nonVegSeats"));
            expeId = infoObj.getString("expeId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String sessionId = db.getUserDetails().get("sessionId");
        ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), jsonObject, Url.URL_EXPERIENCES+"/"+expeId+"/order/estimate?sessionid="+sessionId, "invoice", this);
    }

    private void setData(JSONObject response) throws JSONException {
        if (response.getString("status").equals("OK")){
            tvSubTotal.setText("Subtotal "+response.getJSONObject("result").getString("tickets")+" x "+response.getJSONObject("result").getString("cost_for_one"));
            tvAmount.setText("Rs "+response.getJSONObject("result").getString("cost"));
            tvTax.setText("Rs "+response.getJSONObject("result").getString("taxes"));
            tvFee.setText("Rs "+response.getJSONObject("result").getString("convenience_fee"));
            tvTotalAmount.setText("Rs "+response.getJSONObject("result").getString("amount"));

            tvTitle.setText(infoObj.getString("title"));
            tvAddress.setText(infoObj.getString("address"));

            tvVegNon.setText("VEG: "+infoObj.getString("vegSeats")+" | Non-Veg: "+infoObj.getString("nonVegSeats"));

            Picasso.with(getActivity())
                    .load(infoObj.getString("cover_image"))
                    //.fit()
                    .placeholder(R.drawable.ic_placeholder)
                    .fit().centerCrop()
                    .into(imgView);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (tag.equals("invoice")){
            if (response != null){
                try {
                    setData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("ExpeInvoice", "response "+ response);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_pay:
                Log.d("btnpay", "clicked");
                break;
        }
        return false;
    }
}
