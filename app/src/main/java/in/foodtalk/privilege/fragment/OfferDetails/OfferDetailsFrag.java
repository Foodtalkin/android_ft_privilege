package in.foodtalk.privilege.fragment.OfferDetails;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.models.ImagesObj;

/**
 * Created by RetailAdmin on 02-05-2017.
 */

public class OfferDetailsFrag extends Fragment implements View.OnTouchListener, ApiCallback {
    View layout;
    TextView tvCounter, btnCancel, btnNext, tvShortDes, tvAddress, tvHours, tvDes, tvCuisine;
    Animation slideUpAnimation, slideDownAnimation, slideUpAnimation1;
    LinearLayout redeemBar, btnRedeem, btnSlideUp;
    Boolean redeemBarVisible = false;
    String TAG = OfferDetailsFrag.class.getSimpleName();
    ImageView btnRemove, btnAdd;
    CallbackFragOpen callbackFragOpen;
    ImageView imgView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    public String offerId;
    public String outletId;

    List<ImagesObj> imagesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.offer_details, container, false);
        tvCounter = (TextView) layout.findViewById(R.id.tv_counter);
        redeemBar = (LinearLayout) layout.findViewById(R.id.redeem_bar);
        btnRedeem = (LinearLayout) layout.findViewById(R.id.btn_redeem);
        btnCancel = (TextView) layout.findViewById(R.id.btn_cancel);
        btnNext = (TextView) layout.findViewById(R.id.btn_next);
        btnSlideUp = (LinearLayout) layout.findViewById(R.id.btn_slideUp);
        btnRemove = (ImageView) layout.findViewById(R.id.btn_remove);
        btnAdd = (ImageView) layout.findViewById(R.id.btn_add);
        imgView = (ImageView) layout.findViewById(R.id.img_view);
        tvShortDes = (TextView) layout.findViewById(R.id.tv_short_des);
        tvAddress = (TextView) layout.findViewById(R.id.tv_address);
        tvHours = (TextView) layout.findViewById(R.id.tv_hours);
        tvDes = (TextView) layout.findViewById(R.id.tv_des);
        tvCuisine = (TextView) layout.findViewById(R.id.tv_cuisine);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Log.d(TAG, "offerId: "+offerId+" outletId: "+outletId);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        btnRemove.setOnTouchListener(this);
        btnAdd.setOnTouchListener(this);
        btnCancel.setOnTouchListener(this);
        btnNext.setOnTouchListener(this);
        btnRedeem.setOnTouchListener(this);
        btnSlideUp.setOnTouchListener(this);

        callbackFragOpen = (CallbackFragOpen) getActivity();

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
        tvCounter.setTypeface(typefaceFutura);

        setAnimation();
        loadData("offerDetails");
        return layout;
    }

    private void loadData(String tag){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OFFER_DETAILS+"outlet/"+outletId+"/offer/"+offerId, tag, this);
    }

    private void setData(JSONObject response) throws JSONException {
        JSONObject result = response.getJSONObject("result");
        Picasso.with(getActivity())
                .load(result.getString("cover_image"))
                .fit().centerCrop()
                //.fit()
                .placeholder(R.drawable.bitmap)
                .into(imgView);

        tvShortDes.setText(result.getString("short_description"));
        tvAddress.setText(result.getString("address"));
        tvHours.setText(result.getString("work_hours"));
        tvDes.setText(result.getString("description"));

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = result.getJSONArray("cuisine");
        StringBuilder cuisine = new StringBuilder(jsonArray.getJSONObject(0).getString("title"));
        for(int i = 1, count = jsonArray.length(); i< count; i++)
        {
            cuisine.append(", "+jsonArray.getJSONObject(i).getString("title"));
        }
        Log.d(TAG, "cuisine: "+cuisine);
        tvCuisine.setText(cuisine);

        setImages(result.getJSONArray("images"));
    }

    private void setImages(JSONArray images) throws JSONException {
        for (int i = 0; i < images.length(); i++){
            ImagesObj imagesObj = new ImagesObj();
            imagesObj.url = images.getJSONObject(i).getString("url");
            imagesList.add(imagesObj);
        }
        if (getActivity() != null){
            ImagesAdapter imagesAdapter = new ImagesAdapter(getActivity(), imagesList);
            recyclerView.setAdapter(imagesAdapter);
        }


    }

    private void setAnimation(){
        slideUpAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up_animation);

        slideDownAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down_animation);

        slideUpAnimation1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up_animation1);

        redeemBar.startAnimation(slideUpAnimation1);

        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void showRedeemBar(){
        redeemBar.startAnimation(slideDownAnimation);
    }
    private void hideRedeemBar(){
        redeemBar.startAnimation(slideUpAnimation);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_cancel:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn cancel clicked");
                        hideRedeemBar();
                        btnSlideUp.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.btn_next:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn next clicked");
                        callbackFragOpen.openFrag("restaurantPin","");
                        break;
                }
                break;
            case R.id.btn_redeem:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn redeem clicked");

                        break;
                }
                break;
            case R.id.btn_slideUp:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        showRedeemBar();
                        btnSlideUp.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.btn_add:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn add clicked");
                        int currentCounter = Integer.parseInt(tvCounter.getText().toString());
                        tvCounter.setText(String.valueOf(currentCounter+1));
                        break;
                }
                break;
            case R.id.btn_remove:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn remove clicked");
                        int currentCounter = Integer.parseInt(tvCounter.getText().toString());
                        if (currentCounter > 1){
                            tvCounter.setText(String.valueOf(currentCounter-1));
                        }

                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("offerDetails")){
                try {
                    setData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
