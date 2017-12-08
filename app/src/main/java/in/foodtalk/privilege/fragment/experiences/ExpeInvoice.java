package in.foodtalk.privilege.fragment.experiences;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
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
import in.foodtalk.privilege.library.DateFunction;

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

    CallbackFragOpen callbackFragOpen;

    TextView tvTitle, tvTime, tvTime1, tvAddress, tvAddress1, tvVegNon, tvSubTotal, tvAmount, tvTax, tvFee, tvTotalAmount, tncLink;

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

        tncLink = (TextView) layout.findViewById(R.id.tnc_link);
        tncLink.setOnTouchListener(this);

        callbackFragOpen = (CallbackFragOpen) getActivity();

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
            //String date = DateFunction.convertFormat(infoObj.getString("start_time"), "yyyy-MM-dd HH:mm:ss", "MMM d 'at' h:mm a");
           // String date1 = DateFunction.convertFormat(infoObj.getString("end_time"), "yyyy-MM-dd HH:mm:ss", "h:mm a");
           // tvTime.setText(date+" - "+date1);
           // tvTime.setText(infoObj.getString("display_time"));

            String[] time = infoObj.getString("display_time").split("\n");
            if (time.length == 1){
                tvTime.setText(time[0]);
            }else if (time.length == 2){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvTime.setText(Html.fromHtml(time[0]+"<br/><small><font color='#8c8c8c'>"+time[1]+"</font></small>", Html.FROM_HTML_MODE_COMPACT));
                }else {
                    tvTime.setText(Html.fromHtml(time[0]+"<br/><small><font color='#8c8c8c'>"+time[1]+"</font></small>"));
                }
                // expeCard.tvTime.setText(time[1]);
            }else if (time.length == 0){
                tvTime.setText("");
            }

            if (infoObj.getString("nonveg_preference").equals("0")){
                tvVegNon.setVisibility(View.GONE);
            }

            tvVegNon.setText("VEG: "+infoObj.getString("vegSeats")+" | Non-Veg: "+infoObj.getString("nonVegSeats"));

            Picasso.with(getActivity())
                    .load(infoObj.getString("cover_image"))
                    //.fit()
                    .placeholder(R.drawable.ic_placeholder)
                    //.fit().centerCrop()
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
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d("btnpay", "clicked");
                        callbackFragOpen.openFrag("expePayment", infoObj.toString());
                        break;
                }
                break;
            case R.id.tnc_link:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("webViewFrag", "http://foodtalk.in/legal.html");
                        break;
                }
                break;
        }
        return false;
    }
}
