package in.foodtalk.privilege.fragment.offerlist;


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
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.fragment.OutletList.OutletAdapter;
import in.foodtalk.privilege.models.OfferCardObj;
import in.foodtalk.privilege.models.OutletCardObj;
import in.foodtalk.privilege.models.SelectOfferObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOfferFrag extends Fragment implements ApiCallback, View.OnTouchListener {

    View layout;
    public String outletId;
    RecyclerView recyclerView;

    String TAG = SelectOfferFrag.class.getSimpleName();

    RecyclerView.LayoutManager layoutManager;

    TextView tvOfferName,tvLocation, tvOfferLine;

    List<SelectOfferObj> offerCardList = new ArrayList<>();

    OfferAdapter offerAdapter;

    LinearLayout progressBar;
    LinearLayout placeholderInternet;
    TextView btnRetry, tvMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.select_offer_frag, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);

        tvOfferName = (TextView) layout.findViewById(R.id.tv_offer_name);
        tvOfferLine = (TextView) layout.findViewById(R.id.tv_offer_line);
        tvLocation = (TextView) layout.findViewById(R.id.tv_location);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Typeface typefaceAbril = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        tvOfferName.setTypeface(typefaceAbril);
        tvOfferLine.setTypeface(typefaceFutura);
        tvLocation.setTypeface(typefaceFutura);

        tvOfferName.setText("--");
        tvLocation.setText("--");
        tvOfferLine.setText("--");



        //Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
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


        loadData("outletOffer");
        return layout;
    }

    private void loadData(String tag){
        progressBar.setVisibility(View.VISIBLE);
        placeholderInternet.setVisibility(View.GONE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OUTLET_OFFER+"/"+outletId, tag, this);
    }

    private void sendToAdapter(JSONObject response, String tag)throws JSONException {

        JSONObject outlet = response.getJSONObject("result").getJSONObject("outlet");

        tvOfferName.setText(outlet.getString("name"));
        tvOfferLine.setText(AppController.getInstance().rOneLiner);
        tvLocation.setText(outlet.getString("area"));


        JSONArray listArray = response.getJSONObject("result").getJSONArray("offers");
        progressBar.setVisibility(View.GONE);
        offerCardList.clear();
        for (int i = 0; i < listArray.length(); i++){
            SelectOfferObj selectOfferObj = new SelectOfferObj();
            selectOfferObj.id = listArray.getJSONObject(i).getString("id");
            selectOfferObj.title = listArray.getJSONObject(i).getString("title");
            selectOfferObj.offerId = listArray.getJSONObject(i).getString("offer_id");
            selectOfferObj.outletId = listArray.getJSONObject(i).getString("outlet_id");
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
        if (response != null){
            if (tag.equals("outletOffer")){

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

        Log.d(TAG,"response: "+ response);
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
