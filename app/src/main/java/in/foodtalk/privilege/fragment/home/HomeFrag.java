package in.foodtalk.privilege.fragment.home;


import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
import in.foodtalk.privilege.library.GridSpacingItemDecoration;
import in.foodtalk.privilege.models.OfferCardObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment implements ApiCallback, View.OnTouchListener {

    View layout;

    private String TAG = HomeFrag.class.getSimpleName();

    List<OfferCardObj> offerCardList = new ArrayList<>();

    CallbackFragOpen callbackFragOpen;
    HomeAdapter homeAdapter;

    RecyclerView recyclerView;
    TextView btnBuy, tvHeader;
    LinearLayout header;
    DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_frag, container, false);

        btnBuy = (TextView) layout.findViewById(R.id.btn_buy);
        tvHeader = (TextView) layout.findViewById(R.id.tv_header);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        header = (LinearLayout) layout.findViewById(R.id.header);

        db = new DatabaseHandler(getActivity());

        if (db.getRowCount() > 0){
            header.setVisibility(View.GONE);
        }else {
            header.setVisibility(View.VISIBLE);
        }


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
        Typeface typefaceFmedium= Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");
        tvHeader.setTypeface(typefaceFmedium);
        btnBuy.setTypeface(typefaceFutura);

        btnBuy.setOnTouchListener(this);


        callbackFragOpen = (CallbackFragOpen) getActivity();

        loadData("loadOffers");

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();



        /*CardView cardView = (CardView) layout.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HomeFrag","click");
                callbackFragOpen.openFrag("selectOfferFrag","");
            }
        });*/
        return layout;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loadData(String tag){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OFFERS, tag, this);
    }

    private void sendToAdapter(JSONObject response, String tag) throws JSONException {
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");
        offerCardList.clear();
        for (int i = 0; i< listArray.length(); i++ ){
            OfferCardObj offerCardObj = new OfferCardObj();
            offerCardObj.offerCount = listArray.getJSONObject(i).getString("offer_count");
            offerCardObj.offerIds = listArray.getJSONObject(i).getString("offer_ids");
            offerCardObj.outletCount = listArray.getJSONObject(i).getString("outlet_count");
            offerCardObj.outletIds = listArray.getJSONObject(i).getString("outlet_ids");
            offerCardObj.rId = listArray.getJSONObject(i).getString("rid");
            offerCardObj.name = listArray.getJSONObject(i).getString("name");
            offerCardObj.cost = listArray.getJSONObject(i).getString("cost");
            offerCardObj.cardImage = listArray.getJSONObject(i).getString("card_image");
            offerCardObj.oneLiner = listArray.getJSONObject(i).getString("one_liner");
            offerCardList.add(offerCardObj);
        }
        if (getActivity() != null){
            homeAdapter = new HomeAdapter(getActivity(), offerCardList);
            recyclerView.setAdapter(homeAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d(TAG, "response: "+ response);
        if (response != null){
            try {
                if (tag.equals("loadOffers")){
                    if (response.getString("status").equals("OK")){
                        sendToAdapter(response, tag);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_buy:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("signupAlert","");
                        break;
                }
                break;
        }
        return false;
    }
}
