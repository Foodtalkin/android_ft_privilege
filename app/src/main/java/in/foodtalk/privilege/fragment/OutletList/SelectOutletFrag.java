package in.foodtalk.privilege.fragment.OutletList;


import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
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
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.models.OutletCardObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOutletFrag extends Fragment implements ApiCallback, View.OnTouchListener {

    View layout;
    public String rId;
    String TAG = SelectOutletFrag.class.getSimpleName();

    RecyclerView recyclerView;

    OutletAdapter outletAdapter;

    RecyclerView.LayoutManager layoutManager;

    List<OutletCardObj> outletCardList = new ArrayList<>();

    TextView tvOutletName, tvOutletLine;

    LinearLayout progressBar;
    LinearLayout placeholderInternet;
    TextView btnRetry, tvMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.select_outlet_frag, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        tvOutletName = (TextView) layout.findViewById(R.id.tv_outlet_name);
        tvOutletLine = (TextView) layout.findViewById(R.id.tv_outlet_line);


        Typeface typefaceAbril = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        tvOutletName.setTypeface(typefaceAbril);
        tvOutletLine.setTypeface(typefaceFutura);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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

        loadData("restaurantOutlets");

        return layout;
    }

    private void loadData(String tag){
        progressBar.setVisibility(View.VISIBLE);
        placeholderInternet.setVisibility(View.GONE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.RESTAURANT_OUTLETS+"/"+rId, tag, this);
    }

    private void sendToAdapter(JSONObject response, String tag)throws JSONException{
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");
        progressBar.setVisibility(View.GONE);
        outletCardList.clear();
        for (int i = 0; i < listArray.length(); i++){
            OutletCardObj outletCardObj = new OutletCardObj();
            outletCardObj.offerCount = listArray.getJSONObject(i).getString("offer_count");
            outletCardObj.offerIds = listArray.getJSONObject(i).getString("offer_ids");
            outletCardObj.id = listArray.getJSONObject(i).getString("id");
            outletCardObj.name = listArray.getJSONObject(i).getString("name");
            outletCardObj.cityId = listArray.getJSONObject(i).getString("city_id");
            outletCardObj.cityZone_id = listArray.getJSONObject(i).getString("city_zone_id");
            outletCardObj.address = listArray.getJSONObject(i).getString("address");
            outletCardObj.postcode = listArray.getJSONObject(i).getString("postcode");
           // outletCardObj.description = listArray.getJSONObject(i).getString("description");
            outletCardObj.workHours = listArray.getJSONObject(i).getString("work_hours");
            outletCardList.add(outletCardObj);
        }
        if (getActivity() != null){
            outletAdapter = new OutletAdapter(getActivity(), outletCardList);
            recyclerView.setAdapter(outletAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("restaurantOutlets")){
                Log.d(TAG, "respnse: "+response);
                try {
                    sendToAdapter(response, tag);
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
        switch (view.getId()){
            case R.id.btn_retry:
                loadData("outletOffer");
                break;
        }
        return false;
    }
}
