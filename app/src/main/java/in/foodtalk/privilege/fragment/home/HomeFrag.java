package in.foodtalk.privilege.fragment.home;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import in.foodtalk.privilege.library.EndlessRecyclerOnScrollListener;
import in.foodtalk.privilege.library.EndlessRecyclerViewScrollListener;
import in.foodtalk.privilege.library.GridSpacingItemDecoration;
import in.foodtalk.privilege.library.ToastShow;
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


    Boolean loadingMore = false;

    RecyclerView.LayoutManager mLayoutManager;

   // LinearLayoutManager linearLayoutManager;

    GridLayoutManager linearLayoutManager;

    LinearLayout progressBar;
    LinearLayout placeholderInternet;
    TextView btnRetry, tvMsg;

    String nextUrl;

    Boolean saveState = false;

    String sId;
    Boolean savingsLoaded = false;
    String savingsAmount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_frag, container, false);

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
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





        btnBuy = (TextView) layout.findViewById(R.id.btn_buy);
        tvHeader = (TextView) layout.findViewById(R.id.tv_header);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        header = (LinearLayout) layout.findViewById(R.id.header);

        db = new DatabaseHandler(getActivity());
        sId = db.getUserDetails().get("sessionId");
        savingsLoaded = false;



        if (db.getRowCount() > 0){
            header.setVisibility(View.GONE);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_PROFILE+"?sessionid="+sId, "savings", this);
        }else {
            header.setVisibility(View.VISIBLE);
        }





        tvHeader.setTypeface(typefaceFmedium);
        btnBuy.setTypeface(typefaceFutura);

        btnBuy.setOnTouchListener(this);


        callbackFragOpen = (CallbackFragOpen) getActivity();



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





    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Log.d(TAG, "ActivityCreated "+ "array length: "+offerCardList.size());

        if (saveState == false){
            loadData("loadOffers");
            Log.d(TAG,"loadOffers");
            saveState = true;
        }else {
            homeAdapter = new HomeAdapter(getActivity(), offerCardList);
            recyclerView.setAdapter(homeAdapter);
            setMethod();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState = true;
        Log.d(TAG,"onSaveInstanceState");
        //outState.putBoolean("saveState", true);
        //Save the fragment's state here
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loadData(String tag){
        if (tag.equals("loadOffersMore")){
            if (!nextUrl.equals("")){
                ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, nextUrl, tag, this);
                OfferCardObj offerCardObj = new OfferCardObj();
                offerCardObj.type = "loader";
                offerCardList.add(offerCardObj);

                recyclerView.post(new Runnable() {
                    public void run() {
                        homeAdapter.notifyItemInserted(offerCardList.size()-1);
                    }
                });
            }else {
                ToastShow.showToast(getActivity(), "No more offers!");

                //scrollListener.resetState();


            }
        }else if (tag.equals("loadOffers")){
            progressBar.setVisibility(View.VISIBLE);
            placeholderInternet.setVisibility(View.GONE);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OFFERS, tag, this);

        }
    }

    //-----remove function is used to remove progress bar using indexOf position of null objevt--------
    public void remove() {
        for (int i = 0; i < offerCardList.size(); i++){
            if (offerCardList.get(i).type.equals("loader")){
                offerCardList.remove(i);
                homeAdapter.notifyItemRemoved(i);
            }
        }
        //int position = offerCardList.indexOf(data);
        //Log.d("position for remove", position+"");

    }

    private void sendToAdapter(JSONObject response, String tag) throws JSONException {
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");

        progressBar.setVisibility(View.GONE);

        if (tag.equals("loadOffers")){

            offerCardList.clear();
            if (savingsLoaded){
                if (!savingsAmount.equals("0") && !savingsAmount.equals("")){
                    OfferCardObj offerCardObj = new OfferCardObj();
                    offerCardObj.type = "savings";
                    offerCardObj.savingAmount = savingsAmount;
                    offerCardList.add(offerCardObj);
                }
            }
        }else {
            loadingMore = false;
            remove();
        }



        nextUrl = response.getJSONObject("result").getString("next_page_url");


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
            offerCardObj.type = "offer";
            offerCardObj.primaryCuisine = listArray.getJSONObject(i).getString("primary_cuisine");
            offerCardList.add(offerCardObj);
        }
        if (getActivity() != null){
            if (tag.equals("loadOffers")){
                homeAdapter = new HomeAdapter(getActivity(), offerCardList);
                recyclerView.setAdapter(homeAdapter);
            }else if (tag.equals("loadOffersMore")){
                homeAdapter.notifyDataSetChanged();
            }
        }
        setMethod();

    }
    private void setMethod(){
        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(homeAdapter.getItemViewType(position)){
                    case 0:
                        // Log.d(TAG, "getSpan 0");
                        return 2;
                    case 2:
                        // Log.d(TAG, "getSpan 2");
                        return 2;

                    case 1:
                        // Log.d(TAG, "getSpan 1");
                        return 1;
                    default:
                        //  Log.d(TAG, "getSpan default");
                        return -1;
                }
            }
        });
        endlessScrolling();
    }
    EndlessRecyclerViewScrollListener scrollListener;
    private void endlessScrolling(){
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (loadingMore == false){
                    if (!nextUrl.equals("")){
                        loadData("loadOffersMore");
                        loadingMore = true;
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d(TAG, "response: "+ response);
        if (response != null){
            try {
                if (tag.equals("loadOffers") || tag.equals("loadOffersMore")){
                    if (response.getString("status").equals("OK")){
                        sendToAdapter(response, tag);
                    }
                }else if (tag.equals("savings")){
                    if (response.getString("status").equals("OK")){
                        savingsLoaded = true;
                        savingsAmount = response.getJSONObject("result").getString("saving");
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            if (tag.equals("loadOffers")){
                progressBar.setVisibility(View.GONE);
                placeholderInternet.setVisibility(View.VISIBLE);
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
            case R.id.btn_retry:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        loadData("loadOffers");
                        break;
                }
                break;
        }
        return false;
    }
}
