package in.foodtalk.privilege.fragment.experiencesDetails;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;

import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;

/**
 * Created by RetailAdmin on 03-11-2017.
 */

public class ExperienceDetailsFrag extends Fragment implements ApiCallback {
    View layout;
    String TAG = ExperienceDetailsFrag.class.getSimpleName();

    LinearLayout progressBar, placeholderInternet;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experience_details, container, false);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        placeholderInternet.setVisibility(View.GONE);

        loadData();
        return layout;
    }

    private void loadData(){
        placeholderInternet.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCE_DETAILS, "experienceDetails", this);
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (tag.equals("experienceDetails")){
            progressBar.setVisibility(View.GONE);
            if (response != null){
                Log.d(TAG, "response: "+response);
            }else {
                placeholderInternet.setVisibility(View.VISIBLE);
            }
        }
    }
}
