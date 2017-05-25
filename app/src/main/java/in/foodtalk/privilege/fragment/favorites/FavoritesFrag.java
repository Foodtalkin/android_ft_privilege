package in.foodtalk.privilege.fragment.favorites;

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
import in.foodtalk.privilege.models.FavoriteObj;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class FavoritesFrag extends Fragment implements ApiCallback {
    View layout;
    DatabaseHandler db;
    String TAG = FavoritesFrag.class.getSimpleName();
    List<FavoriteObj> favList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.favorites_frag, container, false);
        db = new DatabaseHandler(getActivity());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loadData("getFavorites");
        return layout;
    }
    private void loadData(String tag){

        if (db.getRowCount() > 0){
            String sessionId = db.getUserDetails().get("sessionId");
            Log.d(TAG, "url: "+ Url.BOOKMARK+"?sessionid="+sessionId);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.BOOKMARK+"?sessionid="+sessionId, tag, this);
        }else {
            Log.e(TAG, "user not loggedin");
        }
    }

    private void setAdapter(JSONObject response) throws JSONException {
        favList.clear();
        JSONArray result = response.getJSONArray("result");
        for (int i = 0; i < result.length(); i++){
            FavoriteObj favoriteObj = new FavoriteObj();
            favoriteObj.name = result.getJSONObject(i).getString("name");
            favoriteObj.area = result.getJSONObject(i).getString("area");
            favoriteObj.createdAt = result.getJSONObject(i).getString("created_at");
            favoriteObj.outletId = result.getJSONObject(i).getString("outlet_id");
            favoriteObj.outletOfferId = result.getJSONObject(i).getString("outlet_offer_id");
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
