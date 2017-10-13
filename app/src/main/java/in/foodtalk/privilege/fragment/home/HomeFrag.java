package in.foodtalk.privilege.fragment.home;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.LatLonCallback;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.library.DateFunction;
import in.foodtalk.privilege.library.EndlessRecyclerOnScrollListener;
import in.foodtalk.privilege.library.EndlessRecyclerViewScrollListener;
import in.foodtalk.privilege.library.GetLocation;
import in.foodtalk.privilege.library.GridSpacingItemDecoration;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.ConstantVar;
import in.foodtalk.privilege.models.OfferCardObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment implements ApiCallback, View.OnTouchListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LatLonCallback {

    View layout;

    private String TAG = HomeFrag.class.getSimpleName();

    List<OfferCardObj> offerCardList = new ArrayList<>();

    CallbackFragOpen callbackFragOpen;
    HomeAdapter homeAdapter;

    RecyclerView recyclerView;
    TextView btnBuy, tvHeader, btnLocation, btnLocation1, tvFooter, tvFooter1;
    ImageView btnLocationClose, btnLocationClose1;
    LinearLayout header, footer, footer1;
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

    LatLonCallback latLonCallback;

    GetLocation getLocation;
    ValueCallback valueCallback;

    public String cityId;

    String userType = "guest";

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
        footer1 = (LinearLayout) layout.findViewById(R.id.footer1);
        btnLocation = (TextView) layout.findViewById(R.id.btn_location);
        btnLocation1 = (TextView) layout.findViewById(R.id.btn_location1);
        tvFooter = (TextView) layout.findViewById(R.id.tv_footer);
        tvFooter = (TextView) layout.findViewById(R.id.tv_footer1);
        btnLocationClose = (ImageView) layout.findViewById(R.id.btn_location_close);
        btnLocationClose1 = (ImageView) layout.findViewById(R.id.btn_location_close1);
        btnLocationClose.setOnTouchListener(this);
        btnLocationClose1.setOnTouchListener(this);

        btnLocation.setOnTouchListener(this);
        btnLocation1.setOnTouchListener(this);

        btnLocation.setTypeface(typefaceFutura);
        tvFooter.setTypeface(typefaceFmedium);


        btnBuy = (TextView) layout.findViewById(R.id.btn_buy);
        tvHeader = (TextView) layout.findViewById(R.id.tv_header);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        header = (LinearLayout) layout.findViewById(R.id.header);

        db = new DatabaseHandler(getActivity());
        sId = db.getUserDetails().get("sessionId");
        savingsLoaded = false;
        valueCallback = (ValueCallback) getActivity();

        callbackFragOpen = (CallbackFragOpen) getActivity();

        if (db.getRowCount() > 0){
            Log.d(TAG,"subscription id: "+ db.getUserDetails().get("subscription"));
            JSONArray subscription;
            try {
                subscription = new JSONArray(db.getUserDetails().get("subscription"));
                if (subscription.getJSONObject(0).getString("subscription_type_id").equals("1")){
                    header.setVisibility(View.GONE);
                    userType = "paid";
                    Log.d(TAG, "UserType: Paid user");
                    AppController.getInstance().userType = userType;
                }else if (subscription.getJSONObject(0).getString("subscription_type_id").equals("3")){
                    header.setVisibility(View.VISIBLE);
                    btnBuy.setText("Buy Now");
                    tvHeader.setText("- days free trial left. Buy your annual membership to continue");
                    userType = "trial";
                    AppController.getInstance().userType = userType;
                    Log.d(TAG, "UserType: Trial user");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_PROFILE+"?sessionid="+sId, "savings", this);
            //-----------------
            if (db.getUserDetails().get("cityId") == null){
                callbackFragOpen.openFrag("selectCityFrag","");
            }else {
                if (db.getUserDetails().get("cityId").equals("null") || db.getUserDetails().get("cityId").equals("")){
                    callbackFragOpen.openFrag("selectCityFrag","");
                }else {
                    cityId = db.getUserDetails().get("cityId");
                    if (db.getUserDetails().get("cityId").equals("1")){
                        valueCallback.setValue("cityName", "Delhi NCR");
                    }else if (db.getUserDetails().get("cityId").equals("2")){
                        valueCallback.setValue("cityName", "Mumbai");
                    }
                }
            }
        }else {
            AppController.getInstance().userType = userType;
            cityId = AppController.getInstance().cityId;
            header.setVisibility(View.VISIBLE);
            valueCallback.setValue("cityName", AppController.getInstance().cityName);
        }


        tvHeader.setTypeface(typefaceFmedium);
        btnBuy.setTypeface(typefaceFutura);

        btnBuy.setOnTouchListener(this);






        ((AppCompatActivity) getActivity()).getSupportActionBar().show();



        /*CardView cardView = (CardView) layout.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HomeFrag","click");
                callbackFragOpen.openFrag("selectOfferFrag","");
            }
        });*/


        latLonCallback = this;
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
                //--startLoading();
            }else {
                footer.setVisibility(View.GONE);
                Log.d(TAG,"Location Permissions");
                //getLastLocation();
                checkLocationService();
            }
        }
        startLoading();
        // checkLocationService();
    }

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    private void settingApi(){
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    private void checkLocationService(){
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            Log.d(TAG,"Location GPS off");
            settingApi();
            //footer1.setVisibility(View.VISIBLE);

        }else {
            Log.d(TAG,"Location GPS on");
            //getLastLocation();
            getLocation();
            //footer1.setVisibility(View.GONE);
        }

    }

    private void getLocation(){
        latLonCallback = this;
        getLocation = new GetLocation(getActivity(), latLonCallback, "homeFrag");
        getLocation.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

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

                    //getLastLocation();
                    checkLocationService();
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
        saveState = false;
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
                            //checkLocationService();
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

        if (lat.equals("")){
            checkLocationPermission();
        }else {
            startLoading();
        }


        // Log.d(TAG, "ActivityCreated "+ "array length: "+offerCardList.size());


    }

    private void startLoading(){
        if (saveState == false){
            loadData("loadOffers");
            Log.d(TAG,"loadOffers");
            saveState = true;
            Log.d(TAG,"StartLoading 1");
        }else {
            homeAdapter = new HomeAdapter(getActivity(), offerCardList);
            recyclerView.setAdapter(homeAdapter);
            setMethod();
            Log.d(TAG,"StartLoading 2");
        }
    }

    public void refreshFeed(){
        //getLastLocation();
        getLocation();
        Log.d(TAG,"refreshFeed");
        footer1.setVisibility(View.GONE);
        // startLoading();
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
                    url = nextUrl+"&latitude="+lat+"&longitude="+lon+"&city_id="+cityId;
                }else {
                    url = nextUrl+"&city_id="+cityId;
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
                url = Url.OFFERS+"?latitude="+lat+"&longitude="+lon+"&city_id="+cityId;
            }else {
                url = Url.OFFERS+"?city_id="+cityId;
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
                Log.d(TAG,"onLoadMore");
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
        //Log.d(TAG, "response: "+ response);
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

                        Log.d(TAG, "response: saving "+ response);

                        checkUserStatus(response.getJSONObject("result").getJSONArray("subscription").getJSONObject(0).getString("expiry"),response.getString("date_time"));
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

    private void checkUserStatus(String expiryDate, String currentDate){
       // Log.e(TAG, "check days: "+ DateFunction.getCountOfDays(currentDate, expiryDate));
        String leftDays = DateFunction.getCountOfDays(currentDate, expiryDate);

        if (userType.equals("trial")){
            if (Integer.parseInt(leftDays) < 1){
                tvHeader.setText("You Free trial has expired. Buy your annual membership to continue");
                AppController.getInstance().userStatus = "expire";
            }else {
                if (Integer.parseInt(leftDays) == 1){
                    tvHeader.setText(leftDays +" day free trial left. Buy your annual membership to continue");
                }else {
                    tvHeader.setText(leftDays +" day free trial left. Buy your annual membership to continue");
                }
                AppController.getInstance().userStatus = "active";
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_buy:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (userType.equals("guest")){
                            callbackFragOpen.openFrag("signUp","trial");
                        }else if (userType.equals("trial")){
                            callbackFragOpen.openFrag("signupAlert","");
                        }
                        //refreshFeed();
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
            case R.id.btn_location_close1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        footer1.setVisibility(View.GONE);
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
            case R.id.btn_location1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        settingApi();
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...

                        Log.d(TAG,"GPS enabled");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(),
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        //Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();
                        Log.d(TAG,"Location enabled by user!");

                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        // Toast.makeText(getActivity(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Location not enabled, user cancelled.");
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void location(String gpsStatus, String lat, String lon) {
        Log.d(TAG,"location - lat: "+lat+" lon:"+ lon);
        if (gpsStatus.equals(ConstantVar.LOCATION_GOT)){
            this.lat = lat;
            this.lon = lon;
        }else {
            this.lat = "";
            this.lon = "";
        }
        saveState = false;
        startLoading();
        if (getLocation != null){
            getLocation.onStop();
        }
    }
}
