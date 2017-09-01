package in.foodtalk.privilege.fragment.home;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.EndlessRecyclerOnScrollListener;
import in.foodtalk.privilege.library.EndlessRecyclerViewScrollListener;
import in.foodtalk.privilege.library.GridSpacingItemDecoration;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.OfferCardObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment implements ApiCallback, View.OnTouchListener {

    View layout;

    private String TAG = HomeFrag.class.getSimpleName();

    List<OfferCardObj> offerCardList = new ArrayList<>();

    CallbackFragOpen callbackFragOpen;
    HomeAdapter homeAdapter;

    RecyclerView recyclerView;
    TextView btnBuy, tvHeader, btnLocation, tvFooter;
    ImageView btnLocationClose;
    LinearLayout header, footer;
    DatabaseHandler db;


    Boolean loadingMore = false;

    RecyclerView.LayoutManager mLayoutManager;

   // LinearLayoutManager linearLayoutManager;

    GridLayoutManager linearLayoutManager;

    LinearLayout progressBar;
    LinearLayout placeholderInternet;
    TextView btnRetry, tvMsg;

    String nextUrl;

    Boolean saveState = false;

    String sId;
    Boolean savingsLoaded = false;
    String savingsAmount;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private FusedLocationProviderClient mFusedLocationClient;

    protected Location mLastLocation;

    String lat = "";
    String lon = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "onCreate");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_frag, container, false);

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
        Typeface typefaceFmedium= Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        placeholderInternet.setVisibility(View.GONE);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        tvMsg = (TextView) layout.findViewById(R.id.tv_msg);
        btnRetry.setTypeface(typefaceFutura);
        tvMsg.setTypeface(typefaceFmedium);
        btnRetry.setOnTouchListener(this);

        footer = (LinearLayout) layout.findViewById(R.id.footer);
        btnLocation = (TextView) layout.findViewById(R.id.btn_location);
        tvFooter = (TextView) layout.findViewById(R.id.tv_footer);
        btnLocationClose = (ImageView) layout.findViewById(R.id.btn_location_close);
        btnLocationClose.setOnTouchListener(this);

        btnLocation.setOnTouchListener(this);

        btnLocation.setTypeface(typefaceFutura);
        tvFooter.setTypeface(typefaceFmedium);


        btnBuy = (TextView) layout.findViewById(R.id.btn_buy);
        tvHeader = (TextView) layout.findViewById(R.id.tv_header);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        header = (LinearLayout) layout.findViewById(R.id.header);

        db = new DatabaseHandler(getActivity());
        sId = db.getUserDetails().get("sessionId");
        savingsLoaded = false;



        if (db.getRowCount() > 0){
            header.setVisibility(View.GONE);
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_PROFILE+"?sessionid="+sId, "savings", this);
        }else {
            header.setVisibility(View.VISIBLE);
        }





        tvHeader.setTypeface(typefaceFmedium);
        btnBuy.setTypeface(typefaceFutura);

        btnBuy.setOnTouchListener(this);


        callbackFragOpen = (CallbackFragOpen) getActivity();



        ((AppCompatActivity) getActivity()).getSupportActionBar().show();





        /*CardView cardView = (CardView) layout.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HomeFrag","click");
                callbackFragOpen.openFrag("selectOfferFrag","");
            }
        });*/

        checkLocationPermission();

        return layout;
    }

    static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 5;

    private void checkLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

               /* ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSION_ACCESS_COURSE_LOCATION );*/
                footer.setVisibility(View.VISIBLE);
                Log.d(TAG,"not Location Permissions");
                startLoading();
            }else {
                footer.setVisibility(View.GONE);
                Log.d(TAG,"Location Permissions");
                getLastLocation();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG,"requestCode: "+ requestCode);
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COURSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                Log.d(TAG,"requestCode: done");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG,"permission was granted");
                    saveState = false;
                    getLastLocation();
                    footer.setVisibility(View.GONE);
                } else {
                    Log.d(TAG,"permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                          /*  mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude()));*/
                            Log.d(TAG, "lat: "+mLastLocation.getLatitude()+" : Lon: "+mLastLocation.getLongitude());
                            lat = new Double(mLastLocation.getLatitude()).toString();
                            lon = new Double(mLastLocation.getLongitude()).toString();
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            lat = "";
                            lon = "";
                            //showSnackbar(getString(R.string.no_location_detected));
                        }
                        startLoading();
                    }
                });
    }







    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


       // Log.d(TAG, "ActivityCreated "+ "array length: "+offerCardList.size());


    }

    private void startLoading(){
        if (saveState == false){
            loadData("loadOffers");
            Log.d(TAG,"loadOffers");
            saveState = true;
        }else {
            homeAdapter = new HomeAdapter(getActivity(), offerCardList);
            recyclerView.setAdapter(homeAdapter);
            setMethod();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState = true;
        Log.d(TAG,"onSaveInstanceState");
        //outState.putBoolean("saveState", true);
        //Save the fragment's state here
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loadData(String tag){
        if (tag.equals("loadOffersMore")){
            if (!nextUrl.equals("")){
                String url;
                if (!lat.equals("")){
                    url = nextUrl+"&latitude="+lat+"&longitude="+lon;
                }else {
                    url = nextUrl;
                }
                ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, url, tag, this);
                OfferCardObj offerCardObj = new OfferCardObj();
                offerCardObj.type = "loader";
                offerCardList.add(offerCardObj);

                recyclerView.post(new Runnable() {
                    public void run() {
                        homeAdapter.notifyItemInserted(offerCardList.size()-1);
                    }
                });
            }else {
                ToastShow.showToast(getActivity(), "No more offers!");

                //scrollListener.resetState();


            }
        }else if (tag.equals("loadOffers")){
            progressBar.setVisibility(View.VISIBLE);
            placeholderInternet.setVisibility(View.GONE);
            String url;
            if (!lat.equals("")){
                url = Url.OFFERS+"?latitude="+lat+"&longitude="+lon;
            }else {
                url = Url.OFFERS;
            }
            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, url, tag, this);

        }
    }

    //-----remove function is used to remove progress bar using indexOf position of null objevt--------
    public void remove() {
        for (int i = 0; i < offerCardList.size(); i++){
            if (offerCardList.get(i).type.equals("loader")){
                offerCardList.remove(i);
                homeAdapter.notifyItemRemoved(i);
            }
        }
        //int position = offerCardList.indexOf(data);
        //Log.d("position for remove", position+"");

    }

    private void sendToAdapter(JSONObject response, String tag) throws JSONException {
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");

        progressBar.setVisibility(View.GONE);

        if (tag.equals("loadOffers")){

            offerCardList.clear();
            if (savingsLoaded){
                if (!savingsAmount.equals("0") && !savingsAmount.equals("")){
                    OfferCardObj offerCardObj = new OfferCardObj();
                    offerCardObj.type = "savings";
                    offerCardObj.savingAmount = savingsAmount;
                    offerCardList.add(offerCardObj);
                }
            }
        }else {
            loadingMore = false;
            remove();
        }



        nextUrl = response.getJSONObject("result").getString("next_page_url");


        for (int i = 0; i< listArray.length(); i++ ){
            OfferCardObj offerCardObj = new OfferCardObj();
            offerCardObj.offerCount = listArray.getJSONObject(i).getString("offer_count");
            offerCardObj.offerIds = listArray.getJSONObject(i).getString("offer_ids");
            offerCardObj.outletCount = listArray.getJSONObject(i).getString("outlet_count");
            offerCardObj.outletIds = listArray.getJSONObject(i).getString("outlet_ids");
            offerCardObj.rId = listArray.getJSONObject(i).getString("rid");
            offerCardObj.name = listArray.getJSONObject(i).getString("name");
            offerCardObj.cost = listArray.getJSONObject(i).getString("cost");
            offerCardObj.cardImage = listArray.getJSONObject(i).getString("card_image");
            offerCardObj.oneLiner = listArray.getJSONObject(i).getString("one_liner");
            offerCardObj.type = "offer";
            offerCardObj.primaryCuisine = listArray.getJSONObject(i).getString("primary_cuisine");
            if (listArray.getJSONObject(i).has("distance")){
                Double dis = Double.parseDouble(listArray.getJSONObject(i).getString("distance"))/1000;
                offerCardObj.distance = String.format("%.1f", dis);
            }else {
                offerCardObj.distance = "";
            }


            offerCardList.add(offerCardObj);
        }
        if (getActivity() != null){
            if (tag.equals("loadOffers")){
                homeAdapter = new HomeAdapter(getActivity(), offerCardList);
                recyclerView.setAdapter(homeAdapter);
            }else if (tag.equals("loadOffersMore")){
                homeAdapter.notifyDataSetChanged();
            }
        }
        setMethod();

    }
    private void setMethod(){
        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(homeAdapter.getItemViewType(position)){
                    case 0:
                        // Log.d(TAG, "getSpan 0");
                        return 2;
                    case 2:
                        // Log.d(TAG, "getSpan 2");
                        return 2;

                    case 1:
                        // Log.d(TAG, "getSpan 1");
                        return 1;
                    default:
                        //  Log.d(TAG, "getSpan default");
                        return -1;
                }
            }
        });
        endlessScrolling();
    }
    EndlessRecyclerViewScrollListener scrollListener;
    private void endlessScrolling(){
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (loadingMore == false){
                    if (!nextUrl.equals("")){
                        loadData("loadOffersMore");
                        loadingMore = true;
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d(TAG, "response: "+ response);
        if (response != null){
            try {
                if (tag.equals("loadOffers") || tag.equals("loadOffersMore")){
                    if (response.getString("status").equals("OK")){
                        sendToAdapter(response, tag);
                    }
                }else if (tag.equals("savings")){
                    if (response.getString("status").equals("OK")){
                        savingsLoaded = true;
                        savingsAmount = response.getJSONObject("result").getString("saving");
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            if (tag.equals("loadOffers")){
                progressBar.setVisibility(View.GONE);
                placeholderInternet.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_buy:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("signupAlert","");
                        break;
                }
                break;
            case R.id.btn_retry:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        loadData("loadOffers");
                        break;
                }
                break;
            case R.id.btn_location_close:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        footer.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.btn_location:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions( new String[] {
                                            Manifest.permission.ACCESS_COARSE_LOCATION  },
                                    MY_PERMISSION_ACCESS_COURSE_LOCATION );
                        }
                        break;
                }
                break;
        }
        return false;
    }
}
