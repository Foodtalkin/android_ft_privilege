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
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.models.SearchObj;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class SearchFrag extends Fragment implements View.OnTouchListener, ApiCallback {
    View layout;

    String TAG = SearchFrag.class.getSimpleName();

    LinearLayout btnLocation1, btnLocation2, btnLocation3, btnLocation4, btnLocation5, btnLocation6, btnLocation7,
            btnCost1, btnCost2, btnCost3;

    EditText etSearch;

    View circleL1, circleL2, circleL3, circleL4, circleL5, circleL6, circleL7;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ImageView iconClear;

    RelativeLayout searchHolder;

    List<SearchObj> searchList = new ArrayList<>();

    List<String> cityZoneIds = new ArrayList<>();
    List<String> cuisineIds = new ArrayList<>();
    List<String> cost = new ArrayList<>();


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



        iconClear = (ImageView) layout.findViewById(R.id.icon_clear);
        iconClear.setOnTouchListener(this);

        btnCost1 = (LinearLayout) layout.findViewById(R.id.btn_cost1);
        btnCost2 = (LinearLayout) layout.findViewById(R.id.btn_cost2);
        btnCost3 = (LinearLayout) layout.findViewById(R.id.btn_cost3);

        etSearch = (EditText) layout.findViewById(R.id.et_search);
        searchHolder = (RelativeLayout) layout.findViewById(R.id.search_holder);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        textListener();

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
                if (charSequence.length() > 2){
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

    private void setAdapter(JSONObject response) throws JSONException {
        searchList.clear();
        JSONArray jsonArray = response.getJSONObject("result").getJSONArray("hits");
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
            searchObj.description = jsonArray.getJSONObject(i).getJSONObject("_source").getString("description");
            searchObj.coverImage = jsonArray.getJSONObject(i).getJSONObject("_source").getString("cover_image");
            searchObj.cardImage = jsonArray.getJSONObject(i).getJSONObject("_source").getString("card_image");

            searchList.add(searchObj);

            if (getActivity() != null){
                SearchAdapter searchAdapter = new SearchAdapter(getActivity(), searchList);
                recyclerView.setAdapter(searchAdapter);
            }
        }
    }
    Boolean location1 = false; Boolean location2 = false; Boolean location3 = false;
    Boolean location4 = false; Boolean location5 = false; Boolean location6 = false;
    Boolean location7 = false;
    private void locationFilter(String location){
        if (location.equals("1")){
            if (location1 == false){
                location1 = true;
                circleL1.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location1 = false;
                circleL1.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("2")){
            if (location2 == false){
                location2 = true;
                circleL2.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location2 = false;
                circleL2.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("3")){
            if (location3 == false){
                location3 = true;
                circleL3.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location3 = false;
                circleL3.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("4")){
            if (location4 == false){
                location4 = true;
                circleL4.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location4 = false;
                circleL4.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("5")){
            if (location5 == false){
                location5 = true;
                circleL5.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location5 = false;
                circleL5.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("6")){
            if (location6 == false){
                location6 = true;
                circleL6.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location6 = false;
                circleL6.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
        if (location.equals("7")){
            if (location7 == false){
                location7 = true;
                circleL7.setBackgroundResource(R.drawable.circle_selected);
                Log.d(TAG, "location selected");
            }else {
                location7 = false;
                circleL7.setBackgroundResource(R.drawable.circle_select);
                Log.d(TAG, "location remove");
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.icon_clear:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        etSearch.setText("");
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

                        break;
                }
                break;
            case R.id.btn_cost2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:

                        break;
                }
                break;
            case R.id.btn_cost3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
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
        }
    }
}
