package in.foodtalk.privilege.fragment.experiencesDetails;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.ValueCallback;

/**
 * Created by RetailAdmin on 03-11-2017.
 */

public class ExperienceDetailsFrag extends Fragment implements ApiCallback, ValueCallback, View.OnTouchListener {
    View layout;
    String TAG = ExperienceDetailsFrag.class.getSimpleName();

    LinearLayout progressBar, placeholderInternet;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ValueCallback valueCallback;
    public Boolean imgSliderVisible = false;

    RelativeLayout imgSlider;
    RecyclerView recyclerView1;

    RecyclerView.LayoutManager layoutManager1;

    ImageView btnClose;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experience_details, container, false);
        imgSlider = (RelativeLayout) layout.findViewById(R.id.img_slider);
        recyclerView1 = (RecyclerView) layout.findViewById(R.id.recycler_view1);
        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        placeholderInternet.setVisibility(View.GONE);
        btnClose = (ImageView) layout.findViewById(R.id.btn_close);
        btnClose.setOnTouchListener(this);
        valueCallback = this;
        loadData();
        return layout;
    }

    private void loadData(){
        placeholderInternet.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCE_DETAILS, "experienceDetails", this);
    }

    private void sendToAdapter(JSONObject reponse) throws JSONException {

        //JSONArray dataList = reponse.getJSONObject("result").getJSONArray("data");
        if (getActivity() != null){
            ExperienceDetailsAdapter experienceDetailsAdapter = new ExperienceDetailsAdapter(getActivity(), reponse, valueCallback);
            recyclerView.setAdapter(experienceDetailsAdapter);
        }

    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (tag.equals("experienceDetails")){
            progressBar.setVisibility(View.GONE);
            if (response != null){
                Log.d(TAG, "response: "+response);
                try {
                    sendToAdapter(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                placeholderInternet.setVisibility(View.VISIBLE);
            }
        }
    }
    public void hideImgSlider(){
        imgSlider.setVisibility(View.GONE);
        imgSliderVisible = false;
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void setValue(String v1, String v2) {
        Log.e("setValue: ", "v1: "+v1+" v2: "+v2);
        /*if (v1.equals("bigImg")){
            imgSlider.setVisibility(View.VISIBLE);
            recyclerView1.scrollToPosition(Integer.valueOf(v2));
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            imgSliderVisible = true;
        }else if (v1.equals("close")){
            imgSlider.setVisibility(View.GONE);
            imgSliderVisible = false;
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }*/
        if (v1.equals("close")){
            imgSlider.setVisibility(View.GONE);
            imgSliderVisible = false;
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }else {
            imgSlider.setVisibility(View.VISIBLE);
            recyclerView1.scrollToPosition(Integer.valueOf(v2));
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            imgSliderVisible = true;
            try {
                JSONArray imgArray = new JSONArray(v1);
                Log.d("image array list", imgArray.toString());
                ExpeBigImagesAdapter bigImagesAdapter = new ExpeBigImagesAdapter(getActivity(), imgArray);
                SnapHelper snapHelper = new PagerSnapHelper();
                recyclerView1.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(recyclerView1);
                recyclerView1.setAdapter(bigImagesAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_close:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        hideImgSlider();
                        break;
                }
                break;
        }
        return false;
    }
}
