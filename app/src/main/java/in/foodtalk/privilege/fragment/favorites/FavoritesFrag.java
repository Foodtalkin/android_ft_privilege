package in.foodtalk.privilege.fragment.favorites;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class FavoritesFrag extends Fragment implements ApiCallback {
    View layout;
    DatabaseHandler db;
    String TAG = FavoritesFrag.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.favorites_frag, container, false);
        db = new DatabaseHandler(getActivity());
        loadData("getFavorites");
        return layout;
    }
    private void loadData(String tag){

        if (db.getRowCount() > 0){
            String sessionId = db.getUserDetails().get("sessionId");
            Log.d(TAG, "sessionId: "+ sessionId);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.BOOKMARK+"?sessionid="+sessionId, tag, this);
        }else {
            Log.e(TAG, "user not loggedin");
        }

    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("getFavorites")){
                Log.d(TAG, "response: "+ response);
            }
        }
    }
}
