package in.foodtalk.privilege.fragment.history;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.models.HistoryObj;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class HistoryFrag extends Fragment implements ApiCallback, View.OnTouchListener {

    String TAG = HistoryFrag.class.getSimpleName();
    DatabaseHandler db;
    View layout;
    List<HistoryObj>historyList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    CallbackFragOpen callbackFragOpen;

    LinearLayout progressBar;
    LinearLayout placeholderInternet, placeholderEmpty;
    TextView btnRetry, tvMsg, btnOffers;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.history_frag, container, false);
        db = new DatabaseHandler(getActivity());

        callbackFragOpen = (CallbackFragOpen) getActivity();

        btnOffers = (TextView) layout.findViewById(R.id.btn_offers);
        placeholderEmpty = (LinearLayout) layout.findViewById(R.id.placeholder_empty);
        placeholderEmpty.setVisibility(View.GONE);
        btnOffers.setOnTouchListener(this);

        Typeface typefaceFmedium= Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        placeholderInternet.setVisibility(View.GONE);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        tvMsg = (TextView) layout.findViewById(R.id.tv_msg);
        btnRetry.setTypeface(typefaceFmedium);
        tvMsg.setTypeface(typefaceFmedium);
        btnRetry.setOnTouchListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        loadData("redeemHistory");

        return layout;
    }

    private void loadData(String tag){
        if (db.getRowCount() > 0){
            progressBar.setVisibility(View.VISIBLE);
            placeholderInternet.setVisibility(View.GONE);
            String sessionId = db.getUserDetails().get("sessionId");
            Log.d(TAG, "url: "+ Url.BOOKMARK+"?sessionid="+sessionId);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.REDEEM_HISTORY+"?sessionid="+sessionId, tag, this);
        }else {
            Log.e(TAG, "user not loggedin");
        }
    }

    private void setAdapter(JSONObject response) throws JSONException {
        historyList.clear();
        progressBar.setVisibility(View.GONE);
        JSONArray result = response.getJSONArray("result");
        if (result.length() == 0){
            placeholderEmpty.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < result.length(); i++){
            HistoryObj historyObj = new HistoryObj();
            historyObj.name = result.getJSONObject(i).getString("name");
            historyObj.createdAt = result.getJSONObject(i).getString("created_at");
            historyObj.id = result.getJSONObject(i).getString("id");
            historyObj.offerRedeemed = result.getJSONObject(i).getString("offers_redeemed");
            historyList.add(historyObj);
        }
        if (getActivity() != null){
            HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), historyList);
            recyclerView.setAdapter(historyAdapter);
        }

    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null ){
            if (tag.equals("redeemHistory")){
                Log.d(TAG, "response: "+ response);
                try {
                    setAdapter(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            progressBar.setVisibility(View.GONE);
            placeholderInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.btn_retry:
                        loadData("outletOffer");
                        break;
                    case R.id.btn_offers:
                        callbackFragOpen.openFrag("homeFrag","");
                        break;
                }
                break;
        }
        return false;
    }
}
