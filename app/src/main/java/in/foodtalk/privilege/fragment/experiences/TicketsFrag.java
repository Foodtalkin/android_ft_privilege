package in.foodtalk.privilege.fragment.experiences;

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
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 16-11-2017.
 */

public class TicketsFrag extends Fragment implements ApiCallback {
    View layout;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHandler db;

    LinearLayout progressBar, placeholderInternet;
    TextView btnRetry, btnBrowseExpe;

    CallbackFragOpen callbackFragOpen;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.tickets_frag, container, false);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);

        callbackFragOpen = (CallbackFragOpen) getActivity();
        btnBrowseExpe = (TextView) layout.findViewById(R.id.btn_browse_expe);
        btnBrowseExpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("btnBrowseExpe", "clicked");
                callbackFragOpen.openFrag("homeTabFrag", "");
            }
        });

        placeholderInternet.setVisibility(View.GONE);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        db = new DatabaseHandler(getActivity());
        loadData();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
        return layout;
    }

    private void loadData(){
        progressBar.setVisibility(View.VISIBLE);
        String sessionId = db.getUserDetails().get("sessionId");
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCES+"/history?sessionid="+sessionId, "tickets", this);
    }
    private void sendToAdapter(JSONObject response) throws JSONException {
        if (response.getString("status").equals("OK")){
            TicketsAdapter ticketsAdapter = new TicketsAdapter(getActivity(), response.getJSONArray("result"));
            recyclerView.setAdapter(ticketsAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (tag.equals("tickets")){
            if (response != null){
                Log.d("tickets response", response+"");
                try {
                    sendToAdapter(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }else {
                placeholderInternet.setVisibility(View.VISIBLE);
            }
        }
    }
}
