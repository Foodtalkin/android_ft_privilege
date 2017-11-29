package in.foodtalk.privilege.fragment.experiencesDetails;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
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
    Animation slideUpAnimation, slideDownAnimation, slideUpAnimation1;

    RelativeLayout imgSlider;
    RecyclerView recyclerView1;

    RecyclerView.LayoutManager layoutManager1;

    LinearLayout btnSlideUp, redeemBar, vegNonBar;
    public Boolean redeemBarVisible = false;

    ImageView btnClose, btnCancel, btnAdd, btnRemove;

    TextView tvTitleBar, tvDateBar, tvCounter, tvTotalAmount, btnNext, tvSeatsCount, vegBarCancel, tvVegCounter, btnBookNow, btnContinue, tvCost, tvPref, tvPrefTitle;

    public int purchaseLimit;

    SeekBar seekBar;

    DatabaseHandler db;

    CallbackFragOpen callbackFragOpen;

    public String expeId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experience_details, container, false);

        btnContinue = (TextView) layout.findViewById(R.id.btn_continue);
        btnContinue.setOnTouchListener(this);

        tvPrefTitle = (TextView) layout.findViewById(R.id.tv_pref_title);

        tvCost = (TextView) layout.findViewById(R.id.tv_cost);
        tvTitleBar = (TextView) layout.findViewById(R.id.tv_title_bar);
        tvDateBar = (TextView) layout.findViewById(R.id.tv_date_bar);
        tvCounter = (TextView) layout.findViewById(R.id.tv_counter);
        tvTotalAmount = (TextView) layout.findViewById(R.id.tv_total_amount);
        btnNext = (TextView) layout.findViewById(R.id.btn_next);
        btnAdd = (ImageView) layout.findViewById(R.id.btn_add);
        btnRemove = (ImageView) layout.findViewById(R.id.btn_remove);
        tvSeatsCount = (TextView) layout.findViewById(R.id.tv_seats_count);
        vegNonBar = (LinearLayout) layout.findViewById(R.id.veg_non_bar);
        vegNonBar.setVisibility(View.GONE);



        tvPref = (TextView) layout.findViewById(R.id.tv_pref);

        btnBookNow = (TextView) layout.findViewById(R.id.btn_book_now);
        btnBookNow.setOnTouchListener(this);

        tvVegCounter = (TextView) layout.findViewById(R.id.tv_veg_counter);

        seekBar = (SeekBar) layout.findViewById(R.id.seek_bar);

        vegBarCancel = (TextView) layout.findViewById(R.id.veg_bar_cancel);
        vegBarCancel.setOnTouchListener(this);

        btnSlideUp = (LinearLayout) layout.findViewById(R.id.btn_slideUp);
        redeemBar = (LinearLayout) layout.findViewById(R.id.redeem_bar);
        redeemBar.setClickable(false);

        btnCancel = (ImageView) layout.findViewById(R.id.btn_cancel);
        btnCancel.setOnTouchListener(this);

        btnSlideUp.setOnTouchListener(this);
        btnSlideUp.setVisibility(View.GONE);

        btnAdd.setOnTouchListener(this);
        btnRemove.setOnTouchListener(this);
        btnNext.setOnTouchListener(this);

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
        setAnimation("onePlus");

        tvCost.setText("--");
        tvSeatsCount.setText("--");



        callbackFragOpen = (CallbackFragOpen) getActivity();

        db = new DatabaseHandler(getActivity());

        if (db.getRowCount() > 0){
            btnBookNow.setText("Book Now");
        }else {
            btnBookNow.setText("Book Now");
        }

        return layout;
    }

    private void loadData(){
        btnSlideUp.setVisibility(View.VISIBLE);
        placeholderInternet.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_EXPERIENCES+"/"+expeId, "experienceDetails", this);
    }
    JSONObject response;
    private void sendToAdapter(JSONObject reponse) throws JSONException {
        //JSONArray dataList = reponse.getJSONObject("result").getJSONArray("data");
        if (getActivity() != null){
            ExperienceDetailsAdapter experienceDetailsAdapter = new ExperienceDetailsAdapter(getActivity(), reponse, valueCallback);
            recyclerView.setAdapter(experienceDetailsAdapter);
            setData(reponse);
        }
    }
    int avilableSeats;
    int nonVegSeats;
    int vegSeats;
    private void setData(JSONObject reponse) throws JSONException {
        this.response = reponse;
        purchaseLimit = Integer.parseInt(reponse.getJSONObject("result").getString("avilable_seats"));
        avilableSeats = Integer.parseInt(reponse.getJSONObject("result").getString("avilable_seats"));
        tvSeatsCount.setText(avilableSeats+" Spots");
        tvCost.setText(getResources().getString(R.string.rs)+" "+ response.getJSONObject("result").getString("cost"));
        tvTotalAmount.setText(getResources().getString(R.string.rs)+" "+ reponse.getJSONObject("result").getString("cost"));

        tvTitleBar.setText(reponse.getJSONObject("result").getString("title"));
        tvPrefTitle.setText(reponse.getJSONObject("result").getString("title"));

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        if (avilableSeats < 11){

        }else {
            String txtCounter = "<font color='red'>10</font> / <font color='green'>0</font>";
            tvVegCounter.setText((Html.fromHtml(txtCounter)));
            //tvVegCounter.setText(10+" / "+"0");
            seekBar.setMax(10);
            avilableSeats = 10;
        }
        String isDisabled = reponse.getJSONObject("result").getString("is_disabled");
        String isActive = reponse.getJSONObject("result").getString("is_active");
        if (isActive.equals("0") && isDisabled.equals("0")){
            btnBookNow.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
            btnBookNow.setClickable(false);
            btnBookNow.setText(" Closed ");
        }else {
            if (avilableSeats == 0){
                btnBookNow.setBackground(getResources().getDrawable(R.drawable.btn_bg_red));
                btnBookNow.setClickable(false);
                btnBookNow.setText(" Sold Out ");
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("seekBar", "i: "+ i+" b: "+b);
                nonVegSeats = Integer.parseInt(tvCounter.getText().toString()) - i;
                vegSeats = i;
                String txtCounter = "<font color='red'>"+nonVegSeats+"</font> / <font color='green'>"+vegSeats+"</font>";
                //tvVegCounter.setText(nonVegSeats+" / "+ vegSeats);
                tvVegCounter.setText(Html.fromHtml(txtCounter));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    private void showRedeemBar(){
        redeemBar.startAnimation(slideDownAnimation);
        redeemBarVisible = true;
        //blackLayer.setAlpha(.5f);
    }

    public void hideRedeemBar(){
       // blackLayer.setAlpha(0);
        redeemBarVisible = false;
        redeemBar.startAnimation(slideUpAnimation);
    }
    private void setAnimation(String offerType){
        if (offerType.equals("onePlus")){
            slideUpAnimation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up_animation);

            slideDownAnimation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_down_animation);

            slideUpAnimation1 = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up_animation1);
        }else {
            slideUpAnimation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up_animation_p);

            slideDownAnimation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_down_animation_p);

            slideUpAnimation1 = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up_animation1_p);
        }


        redeemBar.startAnimation(slideUpAnimation1);

        slideUpAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //redeemBar.setVisibility(View.VISIBLE);
                //btnBuyNow.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //barButtons.setVisibility(View.INVISIBLE);
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
               // barButtons.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void gotoInvoic(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nonVegSeats", String.valueOf(nonVegSeats));
            jsonObject.put("vegSeats", String.valueOf(vegSeats));
            jsonObject.put("totalTickets", tvCounter.getText().toString());
            jsonObject.put("expeId", expeId);

            jsonObject.put("title", response.getJSONObject("result").getString("title"));
            jsonObject.put("address", response.getJSONObject("result").getString("address"));
            // jsonObject.put("time", response.getJSONObject("result").getString("time"));
            jsonObject.put("cover_image", response.getJSONObject("result").getString("cover_image"));
            jsonObject.put("nonveg_preference", response.getJSONObject("result").getString("nonveg_preference"));
            jsonObject.put("start_time", response.getJSONObject("result").getString("start_time"));
            jsonObject.put("end_time", response.getJSONObject("result").getString("end_time"));
            Log.e("btn_continue", response.getJSONObject("result").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callbackFragOpen.openFrag("expeInvoice", jsonObject.toString());
    }

    private void calculatePrice(int num) throws JSONException {
        int totalM = num*Integer.parseInt(response.getJSONObject("result").getString("cost"));
        tvTotalAmount.setText(getResources().getString(R.string.rs)+" "+ String.valueOf(totalM));
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
            case R.id.btn_slideUp:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:

                        break;
                }
                break;
            case R.id.btn_book_now:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (db.getRowCount() > 0){
                            showRedeemBar();
                            btnSlideUp.setVisibility(View.GONE);
                            redeemBar.setClickable(true);
                        }else {
                            //btnBookNow.setText("Sign Up");
                            callbackFragOpen.openFrag("signUp", "trial");
                        }
                        break;
                }
                break;
            case R.id.btn_cancel:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn cancel clicked");
                        hideRedeemBar();
                        btnSlideUp.setVisibility(View.VISIBLE);
                        redeemBar.setClickable(false);
                        break;
                }
                break;
            case R.id.btn_next:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        try {
                            if (response.getJSONObject("result").getString("nonveg_preference").equals("1")){
                                tvPref.setText("Booking "+tvCounter.getText().toString()+" tickets, select dining preferences");
                                vegNonBar.setVisibility(View.VISIBLE);
                                seekBar.setMax(Integer.parseInt(tvCounter.getText().toString()));
                                String txtCounter = "<font color='red'>"+tvCounter.getText().toString()+"</font> / <font color='green'>0</font>";
                                //tvVegCounter.setText(Integer.parseInt(tvCounter.getText().toString())+" / "+"0");
                                tvVegCounter.setText(Html.fromHtml(txtCounter));
                            }else {
                                gotoInvoic();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
                break;
            case R.id.veg_bar_cancel:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        vegNonBar.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.btn_continue:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                            gotoInvoic();
                        break;
                }
                break;
            case R.id.btn_add:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn add clicked");
                        int currentCounter = Integer.parseInt(tvCounter.getText().toString());
                        if (currentCounter < avilableSeats){
                            tvCounter.setText(String.valueOf(currentCounter+1));
                            try {
                                calculatePrice(Integer.parseInt(tvCounter.getText().toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (Integer.parseInt(tvCounter.getText().toString()) == avilableSeats){
                            //btnAdd.setColorFilter(ContextCompat.getColor(getActivity(), R.color.warm_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
                            btnAdd.setVisibility(View.INVISIBLE);
                        }
                        btnRemove.setVisibility(View.VISIBLE);
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
                            try {
                                calculatePrice(Integer.parseInt(tvCounter.getText().toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (Integer.parseInt(tvCounter.getText().toString()) == 1){
                            btnRemove.setVisibility(View.INVISIBLE);
                        }


                        //btnAdd.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                        //btnRemove.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                        //btnRemove.setVisibility(View.GONE);
                        /*if (currentCounter < purchaseLimit){
                            Log.d(TAG,"currentCounter: "+currentCounter+" - purchaseLimit: "+purchaseLimit);

                        }*/
                        btnAdd.setVisibility(View.VISIBLE);

                        break;
                }
        }
        return false;
    }
}
