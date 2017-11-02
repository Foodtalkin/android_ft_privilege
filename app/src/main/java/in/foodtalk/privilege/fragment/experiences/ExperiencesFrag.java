package in.foodtalk.privilege.fragment.experiences;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
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
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;

/**
 * Created by RetailAdmin on 31-10-2017.
 */

public class ExperiencesFrag extends Fragment implements ApiCallback {
    View layout;

    String TAG = ExperiencesFrag.class.getSimpleName();

    List<JSONObject> expeList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experiences_frag, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);




        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                // Do something after 5s = 5000ms
                //buttons[inew][jnew].setBackgroundColor(Color.BLACK);
            }
        }, 300);
        return layout;
    }

    private void loadData(){
        Log.d(TAG, "load experiences");
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCES, "experiences", this);
    }

    private void sendToAdapter(JSONObject reponse) throws JSONException {
        JSONArray expeArrayList = reponse.getJSONObject("result").getJSONArray("data");
        for (int i = 0; i< expeArrayList.length(); i++){
            expeList.add(expeArrayList.getJSONObject(i));
        }
        if (getActivity() != null){
            ExperienceAdapter experienceAdapter = new ExperienceAdapter(getActivity(), expeList);
            recyclerView.setAdapter(experienceAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d(TAG, "load expe: "+ response);
        if (response != null){
            if (tag.equals("experiences")){
                Log.e(TAG, "response: "+ response);
                try {
                    sendToAdapter(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
