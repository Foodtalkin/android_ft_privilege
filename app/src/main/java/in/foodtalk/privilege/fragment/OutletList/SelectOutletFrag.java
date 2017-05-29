package in.foodtalk.privilege.fragment.OutletList;


import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SelectOutletFrag extends Fragment implements ApiCallback {

    View layout;
    public String rId;
    String TAG = SelectOutletFrag.class.getSimpleName();

    RecyclerView recyclerView;

    OutletAdapter outletAdapter;

    RecyclerView.LayoutManager layoutManager;

    List<OutletCardObj> outletCardList = new ArrayList<>();

    TextView tvOutletName, tvOutletLine;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.select_outlet_frag, container, false);
        loadData("restaurantOutlets");

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

        return layout;
    }

    private void loadData(String tag){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.RESTAURANT_OUTLETS+"/"+rId, tag, this);
    }

    private void sendToAdapter(JSONObject response, String tag)throws JSONException{
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");

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
        if (tag.equals("restaurantOutlets")){
            Log.d(TAG, "respnse: "+response);
            try {
                sendToAdapter(response, tag);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
