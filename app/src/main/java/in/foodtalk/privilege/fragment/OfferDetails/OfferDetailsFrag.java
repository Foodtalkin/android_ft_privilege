package in.foodtalk.privilege.fragment.OfferDetails;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.ImagesObj;

/**
 * Created by RetailAdmin on 02-05-2017.
 */

public class OfferDetailsFrag extends Fragment implements View.OnTouchListener, ApiCallback, ValueCallback {
    View layout;
    TextView tvCounter, btnNext, tvShortDes, tvAddress, tvHours, tvDes, tvCuisine, tvSuggestedDish;
    ImageView btnCancel;
    Animation slideUpAnimation, slideDownAnimation, slideUpAnimation1;
    LinearLayout redeemBar, btnRedeem, btnSlideUp;
    public Boolean redeemBarVisible = false;
    public Boolean imgSliderVisible = false;
    String TAG = OfferDetailsFrag.class.getSimpleName();
    ImageView btnRemove, btnAdd;
    CallbackFragOpen callbackFragOpen;
    ImageView imgView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager1;



    ScrollView scrollView;

    DatabaseHandler db;

    TextView btnBuyNow;
    TextView tvCoupons;
    TextView btnReadmore;



    public String offerId;
    public String outletId;

    public int purchaseLimit;

    List<ImagesObj> imagesList = new ArrayList<>();

    ImageView btnBookmark, btnPhone, btnLocation;

    String sId;

    static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 14;

    RelativeLayout imgSlider;
    RecyclerView recyclerView1;

    ImageView btnClose;

    TextView tvName, tvArea, tvPrice;

    TextView tvDinner1, tvDinner2, tvDinner3, tvCoupon1, tvCoupon2, tvCoupon3, tvTableHead, tvTableHead1, tvLine1, tvLine2, tvLine3;


    LinearLayout tableHolder;
    RelativeLayout barButtons;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.offer_details, container, false);
        tvCounter = (TextView) layout.findViewById(R.id.tv_counter);
        redeemBar = (LinearLayout) layout.findViewById(R.id.redeem_bar);
        btnRedeem = (LinearLayout) layout.findViewById(R.id.btn_redeem);
        btnCancel = (ImageView) layout.findViewById(R.id.btn_cancel);
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
        scrollView = (ScrollView) layout.findViewById(R.id.scrollview);
        scrollView.setVisibility(View.GONE);

        tvPrice = (TextView) layout.findViewById(R.id.tv_price);

        tvDinner1 = (TextView) layout.findViewById(R.id.tv_dinners1);
        tvDinner2 = (TextView) layout.findViewById(R.id.tv_dinners2);
        tvDinner3 = (TextView) layout.findViewById(R.id.tv_dinners3);

        tvCoupon1 = (TextView) layout.findViewById(R.id.tv_coupon1);
        tvCoupon2 = (TextView) layout.findViewById(R.id.tv_coupon2);
        tvCoupon3 = (TextView) layout.findViewById(R.id.tv_coupon3);

        tvTableHead = (TextView) layout.findViewById(R.id.tv_table_head);
        tvTableHead1 = (TextView) layout.findViewById(R.id.tv_table_head1);

        tvLine1 = (TextView) layout.findViewById(R.id.tv_line1);
        tvLine2 = (TextView) layout.findViewById(R.id.tv_line2);
        tvLine3 = (TextView) layout.findViewById(R.id.tv_line3);

        tableHolder = (LinearLayout) layout.findViewById(R.id.table_holder);

        barButtons = (RelativeLayout) layout.findViewById(R.id.bar_buttons);
        barButtons.setVisibility(View.INVISIBLE);




        tvDinner1.setText("--");
        tvDinner2.setText("--");
        tvDinner3.setText("--");

        tvCoupon1.setText("--");
        tvCoupon2.setText("--");
        tvCoupon3.setText("--");

        tvTableHead.setText("--");
        tvTableHead1.setText("--");

        tvName = (TextView) layout.findViewById(R.id.tv_name);
        tvArea = (TextView) layout.findViewById(R.id.tv_area);

        tvSuggestedDish = (TextView) layout.findViewById(R.id.tv_suggested_dish);

        tvName.setText("--");
        tvAddress.setText("--");

        redeemBar.setClickable(false);

        /*redeemBar.setEnabled(false);
        redeemBar.setClickable(false);
        redeemBar.setVisibility(View.GONE);*/

        btnClose = (ImageView) layout.findViewById(R.id.btn_close);
        btnClose.setOnTouchListener(this);

        btnBuyNow = (TextView) layout.findViewById(R.id.btn_buy_now);
        btnBuyNow.setOnTouchListener(this);

        tvCoupons = (TextView) layout.findViewById(R.id.tv_coupons);

        btnReadmore = (TextView) layout.findViewById(R.id.btn_readmore);
        btnReadmore.setOnTouchListener(this);

        imgSlider = (RelativeLayout) layout.findViewById(R.id.img_slider);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        recyclerView1 = (RecyclerView) layout.findViewById(R.id.recycler_view1);

        redeemBar.setVisibility(View.GONE);
        btnBuyNow.setVisibility(View.GONE);


        db = new DatabaseHandler(getActivity());

        sId = db.getUserDetails().get("sessionId");

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        recyclerView1.setOverScrollMode(1);



        Log.d(TAG, "offerId: "+offerId+" outletId: "+outletId);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        btnRemove.setOnTouchListener(this);
        btnAdd.setOnTouchListener(this);
        btnCancel.setOnTouchListener(this);
        btnNext.setOnTouchListener(this);
        //btnRedeem.setOnTouchListener(this);
        btnSlideUp.setOnTouchListener(this);

        callbackFragOpen = (CallbackFragOpen) getActivity();

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
        tvCounter.setTypeface(typefaceFutura);

        ActionBar actionBar =(((AppCompatActivity)getActivity()).getSupportActionBar());

        btnBookmark = (ImageView) actionBar.getCustomView().findViewById(R.id.btn_bookmark);
        btnPhone = (ImageView) actionBar.getCustomView().findViewById(R.id.btn_phone);
        btnLocation = (ImageView) actionBar.getCustomView().findViewById(R.id.btn_location);

        btnBookmark.setOnTouchListener(this);
        btnPhone.setOnTouchListener(this);
        btnLocation.setOnTouchListener(this);



        //setAnimation();
        loadData("offerDetails");
        return layout;
    }


    String isBookmarked;
    String outletOfferId;
    String phone;
    String lat = "46.414382";
    String lon = "10.013988";
    private void loadData(String tag){
        if (db.getRowCount() > 0) {
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OFFER_DETAILS + "outlet/" + outletId + "/offer/" + offerId+ "/?sessionid="+sId, tag, this);
        }else {
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OFFER_DETAILS + "outlet/" + outletId + "/offer/" + offerId, tag, this);
        }
    }
    private void bookmark(){
        Log.d(TAG, "sId: "+ sId);
        if (db.getRowCount() > 0) {
            if (isBookmarked.equals("0")) {
                ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), null, Url.BOOKMARK + "/" + outletOfferId + "?sessionid=" + sId, "addBookmark", this);
                btnBookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.green));
                isBookmarked = "1";
                ToastShow.showToast(getActivity(), "Offer is bookmarked");
            } else if (isBookmarked.equals("1")) {
                ApiCall.jsonObjRequest(Request.Method.DELETE, getActivity(), null, Url.BOOKMARK + "/" + outletOfferId + "?sessionid=" + sId, "addBookmark", this);
                btnBookmark.setColorFilter(ContextCompat.getColor(getActivity(), R.color.pale_grey_two));
                isBookmarked = "0";
            }
        }else {
            callbackFragOpen.openFrag("signupAlert","");
        }
    }

    private void setData(JSONObject response) throws JSONException {



        JSONObject result = response.getJSONObject("result");
        Picasso.with(getActivity())
                .load(result.getString("cover_image"))
                .fit().centerCrop()
                //.fit()
                .placeholder(R.drawable.bitmap)
                .into(imgView);

        outletOfferId = result.getString("outlet_offer_id");
        phone = result.getString("phone");

        tvName.setText(AppController.getInstance().restaurantName);
        tvArea.setText(result.getString("area"));

        int cost = Integer.valueOf(result.getString("cost").toString());

        String rs = getActivity().getResources().getString(R.string.rs);
        if (cost < 500){
            tvPrice.setText(rs);
        }else if (cost < 999){
            tvPrice.setText(rs+rs);
        }else {
            tvPrice.setText(rs+rs+rs);
        }

        if (result.getJSONObject("metadata").getJSONObject("rules").has("table")){
            tableHolder.setVisibility(View.VISIBLE);
            String numberOfDiners = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("head").getString(0);
            String numberOfCoupons = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("head").getString(1);

            String dinner1 = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("body").getJSONArray(0).getString(0);
            String dinner2 = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("body").getJSONArray(0).getString(1);
            String dinner3 = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("body").getJSONArray(0).getString(2);

            String coupon1 = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("body").getJSONArray(1).getString(0);
            String coupon2 = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("body").getJSONArray(1).getString(1);
            String coupon3 = result.getJSONObject("metadata").getJSONObject("rules").getJSONObject("table").getJSONArray("body").getJSONArray(1).getString(2);

            tvDinner1.setText(dinner1);
            tvDinner2.setText(dinner2);
            tvDinner3.setText(dinner3);

            tvCoupon1.setText(coupon1);
            tvCoupon2.setText(coupon2);
            tvCoupon3.setText(coupon3);

            tvTableHead.setText(numberOfDiners);
            tvTableHead1.setText(numberOfCoupons);
        }else {
            tableHolder.setVisibility(View.GONE);
            tvLine1.setText(result.getJSONObject("metadata").getJSONObject("rules").getJSONArray("lines").getString(0).toString());
            tvLine2.setText(result.getJSONObject("metadata").getJSONObject("rules").getJSONArray("lines").getString(1).toString());
            tvLine3.setText(result.getJSONObject("metadata").getJSONObject("rules").getJSONArray("lines").getString(2).toString());

        }

        if (db.getRowCount() > 0){
            redeemBar.setVisibility(View.VISIBLE);
            btnSlideUp.setVisibility(View.VISIBLE);
            btnBuyNow.setVisibility(View.GONE);
        }else {
            redeemBar.setVisibility(View.GONE);
            btnBuyNow.setVisibility(View.VISIBLE);
            btnSlideUp.setVisibility(View.GONE);
        }
       // redeemBar.setVisibility(View.INVISIBLE);
       // btnBuyNow.setVisibility(View.INVISIBLE);
        setAnimation();




        String dishList = result.getString("suggested_dishes").replaceAll(", ", "\n");
        tvSuggestedDish.setText(dishList);
        if (result.getString("is_bookmarked").equals("1")){
            btnBookmark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.green));
            isBookmarked = "1";
        }else {
            isBookmarked = "0";
            btnBookmark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.pale_grey_two));
        }

        tvShortDes.setText(result.getString("short_description"));
        tvAddress.setText(result.getString("address"));
        tvHours.setText(result.getString("work_hours"));
        tvDes.setText(result.getString("description"));
        tvCoupons.setText(result.getString("purchase_limit")+" Coupons available");

        purchaseLimit = Integer.parseInt(result.getString("purchase_limit"));

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = result.getJSONArray("cuisine");

        if (jsonArray.length() > 0){
            StringBuilder cuisine = new StringBuilder(jsonArray.getJSONObject(0).getString("title"));
            for(int i = 1, count = jsonArray.length(); i< count; i++)
            {
                cuisine.append(", "+jsonArray.getJSONObject(i).getString("title"));
            }
            Log.d(TAG, "cuisine: "+cuisine);
            tvCuisine.setText(cuisine);
        }
        setImages(result.getJSONArray("images"));
    }

    private void setImages(JSONArray images) throws JSONException {
        for (int i = 0; i < images.length(); i++){
            ImagesObj imagesObj = new ImagesObj();
            imagesObj.url = images.getJSONObject(i).getString("url");
            imagesList.add(imagesObj);
        }
        if (getActivity() != null){
            ImagesAdapter imagesAdapter = new ImagesAdapter(getActivity(), imagesList, this);
            recyclerView.setAdapter(imagesAdapter);

            BigImagesAdapter bigImagesAdapter = new BigImagesAdapter(getActivity(), imagesList, this);
            recyclerView1.setAdapter(bigImagesAdapter);
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
                barButtons.setVisibility(View.INVISIBLE);
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
                barButtons.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void showRedeemBar(){
        redeemBar.startAnimation(slideDownAnimation);
        redeemBarVisible = true;
    }
    public void hideRedeemBar(){
        redeemBarVisible = false;
        redeemBar.startAnimation(slideUpAnimation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call
                    Log.d(TAG, "call permissions granted");

                } else {
                    Log.d(TAG, "call permissions denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void makePhoneCall(){
        String number = ("tel:" + phone);
        Intent mIntent = new Intent(Intent.ACTION_CALL);
        mIntent.setData(Uri.parse(number));
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

           /* ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);*/

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            Log.d(TAG, "ask for permission phone");
        } else {
            //You already have permission
            Log.d(TAG, "make a phone call");
            try {
                startActivity(mIntent);
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void openMap(String lat, String lon){
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+lat+","+lon);

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_readmore:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "readmore clicked: maxline "+ tvDes.getMaxLines());
                        //tvDes.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                        /*ViewGroup.LayoutParams params = tvDes.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvDes.setLayoutParams(params);*/
                        if (tvDes.getMaxLines() == 2){
                            tvDes.setMaxLines(10);
                            btnReadmore.setText("Read less");
                        }else {
                            btnReadmore.setText("Read more");
                            tvDes.setMaxLines(2);
                            Log.d(TAG,"set line 2");
                        }

                        break;
                }
                break;
            case R.id.btn_buy_now:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("signupAlert","");
                        Log.d(TAG, "btn buy now clicked");
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
                        Log.d(TAG, "btn next clicked");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("outlet_id",outletId);
                            jsonObject.put("offer_id",offerId);
                            jsonObject.put("offers_redeemed", tvCounter.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callbackFragOpen.openFrag("restaurantPin", jsonObject.toString());
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
                        redeemBar.setClickable(true);
                        break;
                }
                break;
            case R.id.btn_add:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn add clicked");
                        int currentCounter = Integer.parseInt(tvCounter.getText().toString());
                        if (currentCounter < purchaseLimit){
                            tvCounter.setText(String.valueOf(currentCounter+1));
                        }

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
            case R.id.btn_bookmark:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn bookmark clicked");
                        if (isBookmarked != null){
                            bookmark();
                        }
                        break;
                }
                break;
            case R.id.btn_phone:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:

                        if (phone != null){
                            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+phone));//change the number
                            startActivity(callIntent);*/
                            Log.d(TAG, "btn phone clicked");
                            if (db.getRowCount() > 0){
                                makePhoneCall();
                            }else {
                                callbackFragOpen.openFrag("signupAlert","");
                            }

                        }

                        break;
                }
                break;
            case R.id.btn_close:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        hideImgSlider();
                        break;
                }
                break;
            case R.id.btn_location:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn location clicked");
                        if (db.getRowCount() > 0){
                            openMap(lat, lon);
                        }else {
                            callbackFragOpen.openFrag("signupAlert","");
                        }
                        break;
                }
                break;
        }
        return false;
    }

    public void hideImgSlider(){
        imgSlider.setVisibility(View.GONE);
        imgSliderVisible = false;
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("offerDetails")){
                try {
                    setData(response);
                    scrollView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @Override
    public void setValue(String v1, String v2) {
        if (v1.equals("bigImg")){
            imgSlider.setVisibility(View.VISIBLE);
            recyclerView1.scrollToPosition(Integer.valueOf(v2));
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            imgSliderVisible = true;
        }else if (v1.equals("close")){
            imgSlider.setVisibility(View.GONE);
            imgSliderVisible = false;
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }

    }
}
