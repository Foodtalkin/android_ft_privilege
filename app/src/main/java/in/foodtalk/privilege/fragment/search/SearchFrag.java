package in.foodtalk.privilege.fragment.search;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.models.SearchObj;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class SearchFrag extends Fragment implements View.OnTouchListener, ApiCallback, ValueCallback {
    View layout;

    String TAG = SearchFrag.class.getSimpleName();

    LinearLayout btnLocation1, btnLocation2, btnLocation3, btnLocation4, btnLocation5, btnLocation6, btnLocation7,
            btnCost1, btnCost2, btnCost3, btnOffer1, btnOffer2, btnOffer3, btnOffer4, btnOffer5, btnOffer6;

    EditText etSearch;

    View circleL1, circleL2, circleL3, circleL4, circleL5, circleL6, circleL7, circleCost1, circleCost2, circleCost3,
    circleOffer1, circleOffer2, circleOffer3, circleOffer4, circleOffer5, circleOffer6;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView recyclerViewFilters;
    RecyclerView.LayoutManager layoutManager1;

    ImageView iconClear;

    RelativeLayout searchHolder;

    LinearLayout btnApplyFilters;

    List<SearchObj> searchList = new ArrayList<>();

    List<String> cityZoneIds = new ArrayList<>();
    List<String> cuisineIds = new ArrayList<>();
    List<String> cost = new ArrayList<>();
    List<String> offer = new ArrayList<>();

    String[] cityZone = {"Gurgaon", "Noida", "South Delhi", "North Delhi", "East Delhi", "West Delhi", "Central Delhi"};



    CallbackFragOpen callbackFragOpen;

    LinearLayout progressBar;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.search_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        btnLocation1 = (LinearLayout) layout.findViewById(R.id.btn_location1);
        btnLocation2 = (LinearLayout) layout.findViewById(R.id.btn_location2);
        btnLocation3 = (LinearLayout) layout.findViewById(R.id.btn_location3);
        btnLocation4 = (LinearLayout) layout.findViewById(R.id.btn_location4);
        btnLocation5 = (LinearLayout) layout.findViewById(R.id.btn_location5);
        btnLocation6 = (LinearLayout) layout.findViewById(R.id.btn_location6);
        btnLocation7 = (LinearLayout) layout.findViewById(R.id.btn_location7);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);

        cityZoneIds.clear();
        cuisineIds.clear();
        cost.clear();
        offer.clear();


        btnApplyFilters = (LinearLayout) layout.findViewById(R.id.btn_apply_filters);
        btnApplyFilters.setOnTouchListener(this);

        btnLocation1.setOnTouchListener(this);
        btnLocation2.setOnTouchListener(this);
        btnLocation3.setOnTouchListener(this);
        btnLocation4.setOnTouchListener(this);
        btnLocation5.setOnTouchListener(this);
        btnLocation6.setOnTouchListener(this);
        btnLocation7.setOnTouchListener(this);

        circleL1 = layout.findViewById(R.id.circle_location1);
        circleL2 = layout.findViewById(R.id.circle_location2);
        circleL3 = layout.findViewById(R.id.circle_location3);
        circleL4 = layout.findViewById(R.id.circle_location4);
        circleL5 = layout.findViewById(R.id.circle_location5);
        circleL6 = layout.findViewById(R.id.circle_location6);
        circleL7 = layout.findViewById(R.id.circle_location7);

        circleCost1 = layout.findViewById(R.id.circle_cost1);
        circleCost2 = layout.findViewById(R.id.circle_cost2);
        circleCost3 = layout.findViewById(R.id.circle_cost3);

        circleOffer1 = layout.findViewById(R.id.circle_offer1);
        circleOffer2 = layout.findViewById(R.id.circle_offer2);
        circleOffer3 = layout.findViewById(R.id.circle_offer3);
        circleOffer4 = layout.findViewById(R.id.circle_offer4);
        circleOffer5 = layout.findViewById(R.id.circle_offer5);
        circleOffer6 = layout.findViewById(R.id.circle_offer6);

        btnOffer1 = (LinearLayout) layout.findViewById(R.id.btn_offer1);
        btnOffer2 = (LinearLayout) layout.findViewById(R.id.btn_offer2);
        btnOffer3 = (LinearLayout) layout.findViewById(R.id.btn_offer3);
        btnOffer4 = (LinearLayout) layout.findViewById(R.id.btn_offer4);
        btnOffer5 = (LinearLayout) layout.findViewById(R.id.btn_offer5);
        btnOffer6 = (LinearLayout) layout.findViewById(R.id.btn_offer6);

        btnOffer1.setOnTouchListener(this);
        btnOffer2.setOnTouchListener(this);
        btnOffer3.setOnTouchListener(this);
        btnOffer4.setOnTouchListener(this);
        btnOffer5.setOnTouchListener(this);
        btnOffer6.setOnTouchListener(this);

        callbackFragOpen = (CallbackFragOpen) getActivity();







        iconClear = (ImageView) layout.findViewById(R.id.icon_clear);
        iconClear.setOnTouchListener(this);

        btnCost1 = (LinearLayout) layout.findViewById(R.id.btn_cost1);
        btnCost2 = (LinearLayout) layout.findViewById(R.id.btn_cost2);
        btnCost3 = (LinearLayout) layout.findViewById(R.id.btn_cost3);

        btnCost1.setOnTouchListener(this);
        btnCost2.setOnTouchListener(this);
        btnCost3.setOnTouchListener(this);

        etSearch = (EditText) layout.findViewById(R.id.et_search);
        searchHolder = (RelativeLayout) layout.findViewById(R.id.search_holder);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewFilters = (RecyclerView) layout.findViewById(R.id.recycler_view_filters);
        recyclerViewFilters.setNestedScrollingEnabled(true);
        layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerViewFilters.setLayoutManager(layoutManager1);

        textListener();
        loadCuisineData ();

        return layout;
    }

    private void textListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG,"key: "+ charSequence.toString());
                if (charSequence.length() > 1){
                    loadData(charSequence.toString());
                    searchHolder.setVisibility(View.VISIBLE);
                }else {
                    searchHolder.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadData(String key){
        String query = "";
        try {
            query = URLEncoder.encode(key, "utf-8").replace("+","%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "key: "+ query);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.SEARCH_TEXT+"/"+query, "search", this);
    }
    private void loadCuisineData (){
        progressBar.setVisibility(View.VISIBLE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.GET_CUISINE,"getCuisine", this);
    }

    private void setCuisineAdapter(JSONObject response) throws JSONException {
        String status = response.getString("status");
        JSONArray list = response.getJSONArray("result");
        progressBar.setVisibility(View.GONE);
        if (status.equals("OK")){
            SearchFilterAdapter searchFilterAdapter = new SearchFilterAdapter(getActivity(), this, list);
            recyclerViewFilters.setAdapter(searchFilterAdapter);
        }
    }

    private void setAdapter(JSONObject response) throws JSONException {


        searchList.clear();
        JSONArray jsonArray = response.getJSONObject("result").getJSONArray("hits");
        Log.d(TAG, "jsonArray length: "+ jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++){
            SearchObj searchObj = new SearchObj();
            /*searchObj._index = jsonArray.getJSONObject(i).getString("_intex");
            searchObj._type = jsonArray.getJSONObject(i).getString("_type");
            searchObj._id = jsonArray.getJSONObject(i).getString("_id");
            searchObj._score = jsonArray.getJSONObject(i).getString("_score");*/

            searchObj.offerCount = jsonArray.getJSONObject(i).getJSONObject("_source").getString("offer_count");
            searchObj.offerIds = jsonArray.getJSONObject(i).getJSONObject("_source").getString("offer_ids");
            searchObj.outletCount = jsonArray.getJSONObject(i).getJSONObject("_source").getString("outlet_count");
            searchObj.outletIds = jsonArray.getJSONObject(i).getJSONObject("_source").getString("outlet_ids");
            searchObj.id = jsonArray.getJSONObject(i).getJSONObject("_source").getString("rid");
            searchObj.name = jsonArray.getJSONObject(i).getJSONObject("_source").getString("name");
            searchObj.cost = jsonArray.getJSONObject(i).getJSONObject("_source").getString("cost");
            searchObj.oneLiner = jsonArray.getJSONObject(i).getJSONObject("_source").getString("one_liner");
//            searchObj.description = jsonArray.getJSONObject(i).getJSONObject("_source").getString("description");
//            searchObj.coverImage = jsonArray.getJSONObject(i).getJSONObject("_source").getString("cover_image");
            searchObj.cardImage = jsonArray.getJSONObject(i).getJSONObject("_source").getString("card_image");

            searchList.add(searchObj);
            Log.d(TAG,"add to search list: "+i);

            if (getActivity() != null){
                SearchAdapter searchAdapter = new SearchAdapter(getActivity(), searchList);
                recyclerView.setAdapter(searchAdapter);
            }
        }
    }
    Boolean location1 = false; Boolean location2 = false; Boolean location3 = false;
    Boolean location4 = false; Boolean location5 = false; Boolean location6 = false;
    Boolean location7 = false;
    Boolean cost1 = false; Boolean cost2 = false; Boolean cost3 = false;

    Boolean offer1 = false; Boolean offer2 = false; Boolean offer3 = false;
    Boolean offer4 = false; Boolean offer5 = false; Boolean offer6 = false;

    private void offerFilter(String offerV){
        if (offerV.equals("1")){
            if (offer1 == false){
                offer1 = true;
                circleOffer1.setBackgroundResource(R.drawable.circle_selected);
                offer.add("1");
            }else {
                offer1 = false;
                circleOffer1.setBackgroundResource(R.drawable.circle_select);
                offer.remove("1");
            }
        }
        if (offerV.equals("2")){
            if (offer2 == false){
                offer2 = true;
                circleOffer2.setBackgroundResource(R.drawable.circle_selected);
                offer.add("2");
            }else {
                offer2 = false;
                circleOffer2.setBackgroundResource(R.drawable.circle_select);
                offer.remove("2");
            }
        }
        if (offerV.equals("3")){
            if (offer3 == false){
                offer3 = true;
                circleOffer3.setBackgroundResource(R.drawable.circle_selected);
                offer.add("3");
            }else {
                offer3 = false;
                circleOffer3.setBackgroundResource(R.drawable.circle_select);
                offer.remove("3");
            }
        }
        if (offerV.equals("4")){
            if (offer4 == false){
                offer4 = true;
                circleOffer4.setBackgroundResource(R.drawable.circle_selected);
                offer.add("4");
            }else {
                offer4 = false;
                circleOffer4.setBackgroundResource(R.drawable.circle_select);
                offer.remove("4");
            }
        }
        if (offerV.equals("5")){
            if (offer5 == false){
                offer5 = true;
                circleOffer5.setBackgroundResource(R.drawable.circle_selected);
                offer.add("5");
            }else {
                offer5 = false;
                circleOffer5.setBackgroundResource(R.drawable.circle_select);
                offer.remove("5");
            }
        }
        if (offerV.equals("6")){
            if (offer6 == false){
                offer6 = true;
                circleOffer6.setBackgroundResource(R.drawable.circle_selected);
                offer.add("6");
            }else {
                offer6 = false;
                circleOffer6.setBackgroundResource(R.drawable.circle_select);
                offer.remove("6");
            }
        }
    }

    private void costFilter(String costP){
        if (costP.equals("1")){
            if (cost1 == false){
                cost1 = true;
                circleCost1.setBackgroundResource(R.drawable.circle_selected);
                cost.add("budget");
            }else {
                cost1 = false;
                circleCost1.setBackgroundResource(R.drawable.circle_select);
                cost.remove("budget");
            }
        }
        if (costP.equals("2")){
            if (cost2 == false){
                cost2 = true;
                circleCost2.setBackgroundResource(R.drawable.circle_selected);
                cost.add("midrange");
            }else {
                cost2 = false;
                circleCost2.setBackgroundResource(R.drawable.circle_select);
                cost.remove("midrange");
            }
        }
        if (costP.equals("3")){
            if (cost3 == false){
                cost3 = true;
                circleCost3.setBackgroundResource(R.drawable.circle_selected);
                cost.add("splurge");
            }else {
                cost3 = false;
                circleCost3.setBackgroundResource(R.drawable.circle_select);
                cost.remove("splurge");
            }
        }
    }
    private void locationFilter(String location){
        if (location.equals("1")){
            if (location1 == false){
                location1 = true;
                circleL1.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("1");
                Log.d(TAG, "location selected");
            }else {
                location1 = false;
                circleL1.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("1");
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("2")){
            if (location2 == false){
                location2 = true;
                circleL2.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("2");
                Log.d(TAG, "location selected");
            }else {
                location2 = false;
                circleL2.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("2");
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("3")){
            if (location3 == false){
                location3 = true;
                circleL3.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("3");
                Log.d(TAG, "location selected");
            }else {
                location3 = false;
                circleL3.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("3");
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("4")){
            if (location4 == false){
                location4 = true;
                circleL4.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("4");
                Log.d(TAG, "location selected");
            }else {
                location4 = false;
                circleL4.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("4");
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("5")){
            if (location5 == false){
                location5 = true;
                circleL5.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("5");
                Log.d(TAG, "location selected");
            }else {
                location5 = false;
                circleL5.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("5");
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("6")){
            if (location6 == false){
                location6 = true;
                circleL6.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("6");
                Log.d(TAG, "location selected");
            }else {
                location6 = false;
                circleL6.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("6");
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("7")){
            if (location7 == false){
                location7 = true;
                circleL7.setBackgroundResource(R.drawable.circle_selected);
                cityZoneIds.add("7");
                Log.d(TAG, "location selected");
            }else {
                location7 = false;
                circleL7.setBackgroundResource(R.drawable.circle_select);
                cityZoneIds.remove("7");
                Log.d(TAG, "location remove");
            }
        }

        Log.d(TAG,"cityZoneIds: "+ cityZoneIds.toString());
    }

    private void applyFilters(){
        //http://stg-api.foodtalk.in/offers?city_zone_id=3&cuisine=2,1&cost=budget
        String urlParams = "";
        if (cityZoneIds.size() > 0){
            urlParams = cityZoneIds.toString();
            urlParams = urlParams.replaceAll("\\s","");
            urlParams = "city_zone_id="+urlParams.substring(1, urlParams.length()-1);
            //urlParams = urlParams.replaceAll("]","");
        }else {
            //urlParams = "city_zone_id=";
        }
        if (cuisineIds.size() > 0){
            String cIds = cuisineIds.toString();
            cIds = cIds.replaceAll("\\s","");
            if (urlParams.equals("")){
                cIds = "cuisine="+cIds.substring(1, cIds.length()-1);
            }else {
                cIds = "&cuisine="+cIds.substring(1, cIds.length()-1);
            }
            urlParams = urlParams+cIds;
        }else {
            //urlParams = urlParams+"&cuisine=";
        }
        if (cost.size() > 0){
            String cost1 = cost.toString();
            cost1 = cost1.replaceAll("\\s","");
            if (urlParams.equals("")){
                cost1 = "cost="+cost1.substring(1, cost1.length()-1);
            }else {
                cost1 = "&cost="+cost1.substring(1, cost1.length()-1);
            }

            urlParams = urlParams+cost1;
        }
        if (offer.size() > 0){
            String offerIds = offer.toString();
            offerIds = offerIds.replaceAll("\\s","");
            if (urlParams.equals("")){
                offerIds = "cost="+offerIds.substring(1, offerIds.length()-1);
            }else {
                offerIds = "&cost="+offerIds.substring(1, offerIds.length()-1);
            }

            urlParams = urlParams+offerIds;
        }
        Log.d(TAG, "url param: "+urlParams);
        callbackFragOpen.openFrag("searchResult", urlParams);

        Bundle params = new Bundle();
        params.putString("query", urlParams);
        AppController.getInstance().fbLogEvent("search", params);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.icon_clear:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        etSearch.setText("");
                        Log.d(TAG, "clear search");
                        break;
                }
                break;
            case R.id.btn_apply_filters:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        applyFilters();
                        break;
                }
                break;
            case R.id.btn_location1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("1");
                        break;
                }
                break;
            case R.id.btn_location2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("2");
                        break;
                }
                break;
            case R.id.btn_location3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("3");
                        break;
                }
                break;
            case R.id.btn_location4:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("4");
                        break;
                }
                break;
            case R.id.btn_location5:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("5");
                        break;
                }
                break;
            case R.id.btn_location6:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("6");
                        break;
                }
                break;
            case R.id.btn_location7:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        locationFilter("7");
                        break;
                }
                break;
            case R.id.btn_cost1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        costFilter("1");
                        break;
                }
                break;
            case R.id.btn_cost2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        costFilter("2");
                        break;
                }
                break;
            case R.id.btn_cost3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        costFilter("3");
                        break;
                }
                break;
            case R.id.btn_offer1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        offerFilter("1");
                        break;
                }
                break;
            case R.id.btn_offer2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        offerFilter("2");
                        break;
                }
                break;
            case R.id.btn_offer3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        offerFilter("3");
                        break;
                }
                break;
            case R.id.btn_offer4:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        offerFilter("4");
                        break;
                }
                break;
            case R.id.btn_offer5:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        offerFilter("5");
                        break;
                }
                break;
            case R.id.btn_offer6:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        offerFilter("6");
                        break;
                }
                break;

        }
        return false;
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("search")){
                Log.d(TAG, "response: "+ response);
                try {
                    setAdapter(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (tag.equals("getCuisine")){
                try {
                    setCuisineAdapter(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setValue(String v1, String v2) {
        if (v1.equals("add")){
            cuisineIds.add(v2);
        }else if (v1.equals("remove")){
            cuisineIds.remove(v2);
        }
    }
}
