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
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.library.EndlessRecyclerViewScrollListener;

/**
 * Created by RetailAdmin on 31-10-2017.
 */

public class ExperiencesFrag extends Fragment implements ApiCallback {
    View layout;

    String TAG = ExperiencesFrag.class.getSimpleName();

    List<JSONObject> expeList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    LinearLayout progressBar, placeholderInternet, placeholderEmpty;
    TextView btnRetry;

    Boolean loadingMore = false;
    Boolean loadedData = false;

    String nextUrl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experiences_frag, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        placeholderEmpty = (LinearLayout) layout.findViewById(R.id.placeholder_empty);
        placeholderEmpty.setVisibility(View.GONE);

        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("load");
            }
        });
        placeholderInternet.setVisibility(View.GONE);

        loadData("load");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    //loadData("load");
                // Do something after 5s = 5000ms
                //buttons[inew][jnew].setBackgroundColor(Color.BLACK);
            }
        }, 2000);
        endlessScrolling();
        return layout;
    }

    EndlessRecyclerViewScrollListener scrollListener;
    private void endlessScrolling(){
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG,"onLoadMore");
                if (loadingMore == false){
                    if (!nextUrl.equals("")){
                        loadData("loadMore");
                        loadingMore = true;
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void remove() {
        int i = expeList.indexOf(null);
        expeList.remove(i);
        experienceAdapter.notifyItemRemoved(i);
        Log.d("remove index of", i+"");
        //int position = offerCardList.indexOf(data);
        //Log.d("position for remove", position+"");
    }

    private void loadData(String tag){
        //Log.d(TAG, "load experiences");
        if (tag.equals("loadMore")){
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, nextUrl, "experiencesLoadMore", this);
            expeList.add(null);
            recyclerView.post(new Runnable() {
                public void run() {
                    experienceAdapter.notifyItemInserted(expeList.size()-1);
                }
            });
        }else {
            placeholderInternet.setVisibility(View.GONE);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCES, "experiences", this);
        }
    }
    ExperienceAdapter experienceAdapter;
    private void sendToAdapter(JSONObject response, String tag) throws JSONException {
        progressBar.setVisibility(View.GONE);
        JSONArray expeArrayList = response.getJSONObject("result").getJSONArray("data");

        if (tag.equals("load")){
            expeList.clear();
        }

        nextUrl = response.getJSONObject("result").getString("next_page_url");

        if (expeArrayList.length() == 0){
            placeholderEmpty.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "expe total: "+expeArrayList.length());
        for (int i = 0; i< expeArrayList.length(); i++){
            expeList.add(expeArrayList.getJSONObject(i));
        }
        if (getActivity() != null){
            if (tag.equals("load")){
                experienceAdapter = new ExperienceAdapter(getActivity(), expeList);
                recyclerView.setAdapter(experienceAdapter);
            }else {
                remove();
                loadingMore = false;
                experienceAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d(TAG, "load expe: "+ response);
        if (response != null){
            if (tag.equals("experiences")){
                Log.e(TAG, "response: "+ response);
                try {
                    sendToAdapter(response, "load");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (tag.equals("experiencesLoadMore")){
                Log.e(TAG, "response: "+ response);
                try {
                    sendToAdapter(response, "loadMore");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            placeholderInternet.setVisibility(View.VISIBLE);
        }
    }
}
