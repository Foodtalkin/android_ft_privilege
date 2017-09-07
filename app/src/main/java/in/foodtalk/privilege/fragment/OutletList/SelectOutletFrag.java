package in.foodtalk.privilege.fragment.OutletList;


import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.LatLonCallback;
import in.foodtalk.privilege.library.GetLocation;
import in.foodtalk.privilege.models.ConstantVar;
import in.foodtalk.privilege.models.OutletCardObj;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOutletFrag extends Fragment implements ApiCallback, View.OnTouchListener, LatLonCallback {

    View layout;
    public String rId;
    String TAG = SelectOutletFrag.class.getSimpleName();

    RecyclerView recyclerView;

    OutletAdapter outletAdapter;

    RecyclerView.LayoutManager layoutManager;

    List<OutletCardObj> outletCardList = new ArrayList<>();

    TextView tvOutletName, tvOutletLine;

    LinearLayout progressBar;
    LinearLayout placeholderInternet;
    TextView btnRetry, tvMsg;

    String lat = "";
    String lon = "";

    LatLonCallback latLonCallback;

    GetLocation getLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.select_outlet_frag, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        tvOutletName = (TextView) layout.findViewById(R.id.tv_outlet_name);
        tvOutletLine = (TextView) layout.findViewById(R.id.tv_outlet_line);

        tvOutletName.setText(AppController.getInstance().restaurantName);
        tvOutletLine.setText(AppController.getInstance().rOneLiner);


        Typeface typefaceAbril = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");

        tvOutletName.setTypeface(typefaceAbril);
        tvOutletLine.setTypeface(typefaceFutura);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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

        //--loadData("restaurantOutlets");

        checkLocationPermission();

        return layout;
    }

    private void checkLocationPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

               /* ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        MY_PERMISSION_ACCESS_COURSE_LOCATION );*/
                //footer.setVisibility(View.VISIBLE);
                Log.d(TAG,"not Location Permissions");
                loadData("restaurantOutlets");
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
            loadData("restaurantOutlets");
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



    private void loadData(String tag){
        progressBar.setVisibility(View.VISIBLE);
        placeholderInternet.setVisibility(View.GONE);
        String url;
        if (!lat.equals("")){
            url = Url.RESTAURANT_OUTLETS+"/"+rId+"?latitude="+lat+"&longitude="+lon;
        }else {
            url = Url.RESTAURANT_OUTLETS+"/"+rId;
        }
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, url, tag, this);
    }

    private void sendToAdapter(JSONObject response, String tag)throws JSONException{
        JSONArray listArray = response.getJSONObject("result").getJSONArray("data");
        progressBar.setVisibility(View.GONE);
        outletCardList.clear();
        for (int i = 0; i < listArray.length(); i++){
            OutletCardObj outletCardObj = new OutletCardObj();
            outletCardObj.offerCount = listArray.getJSONObject(i).getString("offer_count");
            outletCardObj.offerIds = listArray.getJSONObject(i).getString("offer_ids");
            outletCardObj.id = listArray.getJSONObject(i).getString("id");
            outletCardObj.name = listArray.getJSONObject(i).getString("name");
            outletCardObj.cityId = listArray.getJSONObject(i).getString("city_id");
            outletCardObj.cityZone_id = listArray.getJSONObject(i).getString("city_zone_id");
            outletCardObj.address = listArray.getJSONObject(i).getString("area");
            outletCardObj.postcode = listArray.getJSONObject(i).getString("postcode");
           // outletCardObj.description = listArray.getJSONObject(i).getString("description");
            outletCardObj.workHours = listArray.getJSONObject(i).getString("work_hours");

            if (listArray.getJSONObject(i).has("distance")){
                Double dis = Double.parseDouble(listArray.getJSONObject(i).getString("distance"))/1000;
                outletCardObj.distance = String.format("%.1f", dis);
            }else {
                outletCardObj.distance = "";
            }


            outletCardList.add(outletCardObj);
        }
        if (getActivity() != null){
            outletAdapter = new OutletAdapter(getActivity(), outletCardList);
            recyclerView.setAdapter(outletAdapter);
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("restaurantOutlets")){
                Log.d(TAG, "respnse: "+response);
                try {
                    sendToAdapter(response, tag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            progressBar.setVisibility(View.GONE);
            placeholderInternet.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_retry:
                loadData("outletOffer");
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
        loadData("restaurantOutlets");
    }
}
