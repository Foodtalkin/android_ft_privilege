package in.foodtalk.privilege.fragment.offerlist;


import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
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
import in.foodtalk.privilege.fragment.OutletList.OutletAdapter;
import in.foodtalk.privilege.models.OfferCardObj;
import in.foodtalk.privilege.models.OutletCardObj;
import in.foodtalk.privilege.models.SelectOfferObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOfferFrag extends Fragment implements ApiCallback {

    View layout;
    public String outletId;
    RecyclerView recyclerView;

    String TAG = SelectOfferFrag.class.getSimpleName();

    RecyclerView.LayoutManager layoutManager;

    TextView tvOfferName,tvLocation, tvOfferLine;

    List<SelectOfferObj> offerCardList = new ArrayList<>();

    OfferAdapter offerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.select_offer_frag, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);

        tvOfferName = (TextView) layout.findViewById(R.id.tv_offer_name);
        tvOfferLine = (TextView) layout.findViewById(R.id.tv_offer_line);
        tvLocation = (TextView) layout.findViewById(R.id.tv_location);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Typeface typefaceAbril = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        tvOfferName.setTypeface(typefaceAbril);
        tvOfferLine.setTypeface(typefaceFutura);
        tvLocation.setTypeface(typefaceFutura);


        loadData("outletOffer");
        return layout;
    }

    private void loadData(String tag){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OUTLET_OFFER+"/"+outletId, tag, this);
    }

    private void sendToAdapter(JSONObject response, String tag)throws JSONException {
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");

        offerCardList.clear();
        for (int i = 0; i < listArray.length(); i++){
            SelectOfferObj selectOfferObj = new SelectOfferObj();
            selectOfferObj.id = listArray.getJSONObject(i).getString("id");
            selectOfferObj.title = listArray.getJSONObject(i).getString("title");
            selectOfferObj.shortDescription = listArray.getJSONObject(i).getString("short_description");
            offerCardList.add(selectOfferObj);
            //OfferCardObj offerCardObj = new OfferCardObj();
            //offerCardObj.id = listArray.getJSONObject(i).getString("id");

           // offerCardList.add(offerCardObj);
        }
        if (getActivity() != null){
            offerAdapter = new OfferAdapter(getActivity(), offerCardList);
            recyclerView.setAdapter(offerAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (tag.equals("outletOffer")){
            if (response != null){
                try {
                    sendToAdapter(response, tag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
        Log.d(TAG,"response: "+ response);
    }
}
