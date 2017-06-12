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
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.models.ImagesObj;

/**
 * Created by RetailAdmin on 02-05-2017.
 */

public class OfferDetailsFrag extends Fragment implements View.OnTouchListener, ApiCallback, ValueCallback {
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
        scrollView = (ScrollView) layout.findViewById(R.id.scrollview);
        scrollView.setVisibility(View.GONE);

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


        db = new DatabaseHandler(getActivity());

        sId = db.getUserDetails().get("sessionId");

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        recyclerView1.setOverScrollMode(1);

        if (db.getRowCount() > 0){
            redeemBar.setVisibility(View.VISIBLE);
            btnSlideUp.setVisibility(View.VISIBLE);
            btnBuyNow.setVisibility(View.GONE);
        }else {
            redeemBar.setVisibility(View.GONE);
            btnBuyNow.setVisibility(View.VISIBLE);
            btnSlideUp.setVisibility(View.GONE);

        }

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

        ActionBar actionBar =(((AppCompatActivity)getActivity()).getSupportActionBar());

        btnBookmark = (ImageView) actionBar.getCustomView().findViewById(R.id.btn_bookmark);
        btnPhone = (ImageView) actionBar.getCustomView().findViewById(R.id.btn_phone);
        btnLocation = (ImageView) actionBar.getCustomView().findViewById(R.id.btn_location);

        btnBookmark.setOnTouchListener(this);
        btnPhone.setOnTouchListener(this);
        btnLocation.setOnTouchListener(this);



        setAnimation();
        loadData("offerDetails");
        return layout;
    }


    String isBookmarked;
    String outletOfferId;
    String phone;
    String lat = "46.414382";
    String lon = "10.013988";
    private void loadData(String tag){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.OFFER_DETAILS+"outlet/"+outletId+"/offer/"+offerId, tag, this);
    }
    private void bookmark(){
        Log.d(TAG, "sId: "+ sId);
        if (isBookmarked.equals("0")){
            ApiCall.jsonObjRequest(Request.Method.POST, getActivity(), null, Url.BOOKMARK+"/"+outletOfferId+"?sessionid="+sId, "addBookmark",this);
            btnBookmark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.green));
            isBookmarked = "1";
        }else if (isBookmarked.equals("1")){
            ApiCall.jsonObjRequest(Request.Method.DELETE, getActivity(), null, Url.BOOKMARK+"/"+outletOfferId+"?sessionid="+sId, "addBookmark",this);
            btnBookmark.setColorFilter(ContextCompat.getColor(getActivity(),R.color.pale_grey_two));
            isBookmarked = "0";
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
                        }else {
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
                            makePhoneCall();
                        }

                        break;
                }
                break;
            case R.id.btn_close:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        imgSlider.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.btn_location:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn location clicked");
                        openMap(lat, lon);
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
        }else if (v1.equals("close")){
            imgSlider.setVisibility(View.GONE);
        }

    }
}
