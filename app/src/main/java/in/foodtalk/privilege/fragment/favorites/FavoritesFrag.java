package in.foodtalk.privilege.fragment.favorites;

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
import in.foodtalk.privilege.models.FavoriteObj;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class FavoritesFrag extends Fragment implements ApiCallback, View.OnTouchListener{
    View layout;
    DatabaseHandler db;
    String TAG = FavoritesFrag.class.getSimpleName();
    List<FavoriteObj> favList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    CallbackFragOpen callbackFragOpen;

    LinearLayout progressBar;
    LinearLayout placeholderInternet, placeholderEmpty;
    TextView btnRetry, tvMsg, btnOffers;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.favorites_frag, container, false);
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


        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loadData("getFavorites");
        return layout;
    }
    private void loadData(String tag){
        if (db.getRowCount() > 0){
            progressBar.setVisibility(View.VISIBLE);
            placeholderInternet.setVisibility(View.GONE);
            String sessionId = db.getUserDetails().get("sessionId");
            Log.d(TAG, "url: "+ Url.BOOKMARK+"?sessionid="+sessionId);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.BOOKMARK+"?sessionid="+sessionId, tag, this);
        }else {
            Log.e(TAG, "user not loggedin");
        }
    }

    private void setAdapter(JSONObject response) throws JSONException {
        favList.clear();
        progressBar.setVisibility(View.GONE);
        JSONArray result = response.getJSONArray("result");
        Log.d(TAG,"result: " +result);
        if (result.length() == 0){
            placeholderEmpty.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < result.length(); i++){
            FavoriteObj favoriteObj = new FavoriteObj();
            favoriteObj.name = result.getJSONObject(i).getString("name");
            favoriteObj.area = result.getJSONObject(i).getString("area");
            favoriteObj.createdAt = result.getJSONObject(i).getString("created_at");
            favoriteObj.outletId = result.getJSONObject(i).getString("outlet_id");
            //favoriteObj.outletOfferId = result.getJSONObject(i).getString("outlet_offer_id");
            favoriteObj.offerId = result.getJSONObject(i).getString("offer_id");
            favList.add(favoriteObj);
        }
        if (getActivity() != null){
            FavoritesAdapter favoritesAdapter = new FavoritesAdapter(getActivity(), favList);
            recyclerView.setAdapter(favoritesAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("getFavorites")){
                Log.e(TAG, "response: "+ response);
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
