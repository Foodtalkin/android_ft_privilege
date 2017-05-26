package in.foodtalk.privilege.fragment.history;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import in.foodtalk.privilege.models.HistoryObj;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class HistoryFrag extends Fragment implements ApiCallback {

    String TAG = HistoryFrag.class.getSimpleName();
    DatabaseHandler db;
    View layout;
    List<HistoryObj>historyList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.history_frag, container, false);
        db = new DatabaseHandler(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        loadData("redeemHistory");

        return layout;
    }

    private void loadData(String tag){
        if (db.getRowCount() > 0){
            String sessionId = db.getUserDetails().get("sessionId");
            Log.d(TAG, "url: "+ Url.BOOKMARK+"?sessionid="+sessionId);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.REDEEM_HISTORY+"?sessionid="+sessionId, tag, this);
        }else {
            Log.e(TAG, "user not loggedin");
        }
    }

    private void setAdapter(JSONObject response) throws JSONException {
        historyList.clear();
        JSONArray result = response.getJSONArray("result");
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
        }
    }
}
