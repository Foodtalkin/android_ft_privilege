package in.foodtalk.privilege.fragment.search;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.LatLonCallback;
import in.foodtalk.privilege.fragment.home.HomeAdapter;
import in.foodtalk.privilege.fragment.home.HomeFrag;
import in.foodtalk.privilege.library.EndlessRecyclerViewScrollListener;
import in.foodtalk.privilege.library.GetLocation;
import in.foodtalk.privilege.library.GridSpacingItemDecoration;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.ConstantVar;
import in.foodtalk.privilege.models.OfferCardObj;

/**
 * Created by RetailAdmin on 30-05-2017.
 */

public class SearchResult extends Fragment implements ApiCallback, View.OnTouchListener, LatLonCallback{
    View layout;

    private String TAG = SearchResult.class.getSimpleName();

    List<OfferCardObj> offerCardList = new ArrayList<>();

    CallbackFragOpen callbackFragOpen;
    HomeAdapter homeAdapter;

    RecyclerView recyclerView;
    TextView btnBuy, tvHeader, tvHeader1;
    LinearLayout header;
    DatabaseHandler db;

    public String offerUrl;

    LinearLayout progressBar;
    LinearLayout placeholderInternet;
    TextView btnRetry, tvMsg;

    LinearLayout emptyPlaceholder;

    TextView btnEditFilter;

    Boolean loadingMore = false;
    String nextUrl;

    LinearLayoutManager linearLayoutManager;

    String lat = "";
    String lon = "";

    LatLonCallback latLonCallback;

    GetLocation getLocation;

    String cityId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.search_result_frag, container, false);

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
        Typeface typefaceFmedium= Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        placeholderInternet = (LinearLayout) layout.findViewById(R.id.placeholder_internet);
        placeholderInternet.setVisibility(View.GONE);
        progressBar = (LinearLayout) layout.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        tvMsg = (TextView) layout.findViewById(R.id.tv_msg);
        btnRetry.setTypeface(typefaceFmedium);
        tvMsg.setTypeface(typefaceFmedium);
        btnRetry.setOnTouchListener(this);

        btnEditFilter = (TextView) layout.findViewById(R.id.btn_edit_filter);
        btnEditFilter.setOnTouchListener(this);

        emptyPlaceholder = (LinearLayout) layout.findViewById(R.id.empty_placeholder);
        emptyPlaceholder.setVisibility(View.GONE);

        //btnBuy = (TextView) layout.findViewById(R.id.btn_buy);
        tvHeader = (TextView) layout.findViewById(R.id.tv_header);
        tvHeader1 = (TextView) layout.findViewById(R.id.tv_header1);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        header = (LinearLayout) layout.findViewById(R.id.header);

        db = new DatabaseHandler(getActivity());

        /*if (db.getRowCount() > 0){
            header.setVisibility(View.GONE);
        }else {
            header.setVisibility(View.VISIBLE);
        }*/


        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //tvHeader.setTypeface(typefaceFutura);
        tvHeader1.setTypeface(typefaceFmedium);
        //btnBuy.setTypeface(typefaceFutura);

        //tvHeader.setText(offerUrl);
        tvHeader.setText("Displaying: "+AppController.getInstance().filtersName);

        //btnBuy.setOnTouchListener(this);


        callbackFragOpen = (CallbackFragOpen) getActivity();

        //--loadData("loadOffers");

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        endlessScrolling();
        //searchFilter(offerUrl);



        /*CardView cardView = (CardView) layout.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HomeFrag","click");
                callbackFragOpen.openFrag("selectOfferFrag","");
            }
        });*/
        checkLocationPermission();


        if (db.getRowCount() > 0){

            cityId = db.getUserDetails().get("cityId");
        }else {

            cityId = AppController.getInstance().cityId;
        }
        return layout;
    }

    private void checkLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

               /* ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSION_ACCESS_COURSE_LOCATION );*/
                //footer.setVisibility(View.VISIBLE);
                Log.d(TAG,"not Location Permissions");
                //loadData("restaurantOutlets");
                loadData("loadOffers");
                //--startLoading();
            }else {
                //footer.setVisibility(View.GONE);
                Log.d(TAG,"Location Permissions");
                //getLastLocation();
                checkLocationService();
            }
        }
        //--startLoading();
        // checkLocationService();
    }
    private void checkLocationService(){
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            Log.d(TAG,"Location GPS off");
            //loadData("restaurantOutlets");
            loadData("loadOffers");
            // settingApi();
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

    private String searchFilter(String query){
        Uri uri = Uri.parse("http://google.com/offers?"+query);
        String paramValue = uri.getQueryParameter("cost");

        for (String key : uri.getQueryParameterNames()) {
            String value = uri.getQueryParameter(key);
            List<String> items = Arrays.asList(value.split("\\s*,\\s*"));

            for (int i = 0; i< items.size();i++){
                Log.e(TAG,"value:"+key+" : "+ items.get(i));
            }
            //Do something with value  key, like using a switch/case
        }

        //Log.e(TAG, offerUrl + " : "+uri.getQueryParameterNames()+" : " + paramValue);
        return null;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /*private void loadData(String tag){
        progressBar.setVisibility(View.VISIBLE);
        placeholderInternet.setVisibility(View.GONE);
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, offerUrl, tag, this);
    }*/
    private void loadData(String tag){
        if (tag.equals("loadOffersMore")){
            if (!nextUrl.equals("")){
                String url;
                if (!lat.equals("")){
                    url = nextUrl+"&latitude="+lat+"&longitude="+lon+"&"+offerUrl+"&city_id="+cityId;
                }else {
                    url =  nextUrl+"&"+offerUrl+"&city_id="+cityId;
                }
                ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, url, tag, this);
                OfferCardObj offerCardObj = new OfferCardObj();
                offerCardObj.type = "loader";
                offerCardList.add(offerCardObj);
                homeAdapter.notifyItemInserted(offerCardList.size()-1);
                Log.d(TAG, "load more");
            }else {
                Log.d(TAG, "No more offers");
                ToastShow.showToast(getActivity(), "No more offers!");
            }
        }else if (tag.equals("loadOffers")){
            progressBar.setVisibility(View.VISIBLE);
            placeholderInternet.setVisibility(View.GONE);
            String url;
            if (!lat.equals("")){
                url = Url.OFFERS+"?"+"latitude="+lat+"&longitude="+lon+"&"+offerUrl+"&city_id="+cityId;
            }else {
                url =  Url.OFFERS+"?"+offerUrl+"&city_id="+cityId;
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

    //

    private void sendToAdapter(JSONObject response, String tag) throws JSONException {
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");

        progressBar.setVisibility(View.GONE);

        if (tag.equals("loadOffers")){
            offerCardList.clear();
        }else {
            loadingMore = false;
            remove();
        }

        if (listArray.length() == 0){
            emptyPlaceholder.setVisibility(View.VISIBLE);
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
    }

    private void endlessScrolling(){
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "page: "+page+" totalItemsCount: "+ totalItemsCount);
                if (loadingMore == false){
                    loadData("loadOffersMore");
                    loadingMore = true;
                }

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        Log.d(TAG, "response: "+ response);
        /*if (response != null){
            try {
                if (tag.equals("searchResult")){
                    if (response.getString("status").equals("OK")){
                        sendToAdapter(response, tag);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            progressBar.setVisibility(View.GONE);
            placeholderInternet.setVisibility(View.VISIBLE);
        }*/

        if (response != null){
            try {
                if (tag.equals("loadOffers") || tag.equals("loadOffersMore")){
                    if (response.getString("status").equals("OK")){
                        sendToAdapter(response, tag);
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
                        callbackFragOpen.openFrag("signupFrag","");
                        break;
                }
                break;
            case R.id.btn_retry:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "retry click");
                        loadData("searchResult");
                        break;
                }
                break;
            case R.id.btn_edit_filter:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("searchFrag","");
                        break;
                }
                break;
        }
        return false;
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
        loadData("loadOffers");
        if (getLocation != null){
            getLocation.onStop();
        }
    }
}
