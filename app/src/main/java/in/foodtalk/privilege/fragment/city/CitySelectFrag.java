package in.foodtalk.privilege.fragment.city;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.library.ToastShow;

/**
 * Created by RetailAdmin on 13-09-2017.
 */

public class CitySelectFrag extends Fragment implements View.OnTouchListener, ApiCallback {
    View layout;

    String TAG = CitySelectFrag.class.getSimpleName();

    ImageView imgCity2;

    LinearLayout btnCity1, btnCity2;
    TextView tvRestaurantCity1, tvOutletCity1, tvRestaurantCity2, tvOutletCity2;

    DatabaseHandler db;
    LinearLayout btnSaveCity;
    String cityId = "1";

    ValueCallback valueCallback;

    CallbackFragOpen callbackFragOpen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.city_frag, container, false);


        btnCity1 = (LinearLayout) layout.findViewById(R.id.btn_city1);
        btnCity2 = (LinearLayout) layout.findViewById(R.id.btn_city2);

        tvRestaurantCity1 = (TextView) layout.findViewById(R.id.tv_restaurant_city1);
        tvRestaurantCity2 = (TextView) layout.findViewById(R.id.tv_restaurant_city2);
        tvOutletCity1 = (TextView) layout.findViewById(R.id.tv_outlet_city1);
        tvOutletCity2 = (TextView) layout.findViewById(R.id.tv_outlet_city2);
        imgCity2 = (ImageView) layout.findViewById(R.id.img_city2);

        btnCity1.setOnTouchListener(this);
        btnCity2.setOnTouchListener(this);

        btnSaveCity = (LinearLayout) layout.findViewById(R.id.btn_save_city);
        btnSaveCity.setOnTouchListener(this);

        valueCallback = (ValueCallback) getActivity();

        callbackFragOpen = (CallbackFragOpen) getActivity();

        db = new DatabaseHandler(getActivity());

        loadCities();
        return layout;
    }

    private void loadCities(){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_CITIES, "getCities", this);
    }

    Boolean cityMumbai;
    JSONObject response;
    private void setData(JSONObject response) throws JSONException {
        this.response = response;

        Log.d(TAG, "check city: "+db.getUserDetails().get("cityId"));
        Log.d(TAG, "getUserDetails: "+db.getUserDetails());

        if (response.getJSONArray("result").getJSONObject(0).getString("is_active").equals("1")){
            tvRestaurantCity1.setText(response.getJSONArray("result").getJSONObject(0).getString("restaurant_count"));
            tvOutletCity1.setText(response.getJSONArray("result").getJSONObject(0).getString("outlet_count"));
        }else {
            tvRestaurantCity1.setText("Coming Soon!");
        }
        if (response.getJSONArray("result").getJSONObject(1).getString("is_active").equals("1")){
            tvRestaurantCity2.setText(response.getJSONArray("result").getJSONObject(1).getString("restaurant_count"));
            tvOutletCity2.setText(response.getJSONArray("result").getJSONObject(1).getString("outlet_count"));
            cityMumbai = true;
        }else {
            cityMumbai = false;
            tvRestaurantCity2.setText("Coming Soon!");
            tvOutletCity2.setText("");
            imgCity2.setColorFilter(getResources().getColor(R.color.brownish_grey));
           // imgCity2.setVisibility(View.GONE);
            //imgCity2.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if (db.getRowCount() > 0){
            if (db.getUserDetails().get("cityId") == null){
                btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                saveDefaultCity();
            }else {
                if (db.getUserDetails().get("cityId").equals("null") || db.getUserDetails().get("cityId").equals("")){
                    btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                    btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                    saveDefaultCity();
                }else {
                    cityId = db.getUserDetails().get("cityId");
                    if (cityId.equals("1")){
                        btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                        btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                    }else if (cityId.equals("2")){
                        btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                        btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                    }
                }
            }
        }else {
            btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
            btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
        }
    }

    private void saveDefaultCity(){
        cityId = "1";
        db.updateCity(db.getUserDetails().get("userId"),cityId);
        valueCallback.setValue("cityName", "Delhi NCR");
    }

    private void saveCity(String cityId) throws JSONException{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("city_id", cityId);

        Log.d(TAG, "json obj: "+jsonObject);
        if (db.getRowCount() > 0){
            ApiCall.jsonObjRequest(Request.Method.PUT, getActivity(), jsonObject, Url.USER_UPDATE+"?sessionid="+db.getUserDetails().get("sessionId"), "saveCity", this);
        }else {
            //getFragmentManager().popBackStack();
            if (cityId.equals("1")){
                valueCallback.setValue("cityName", "Delhi NCR");
                //valueCallback.setValue("cityId","1");
                AppController.getInstance().cityId = "1";
            }else if (cityId.equals("2")){
                valueCallback.setValue("cityName", "Mumbai");
                //valueCallback.setValue("cityId", "2");
                AppController.getInstance().cityId = "2";
            }
            callbackFragOpen.openFrag("homeFrag","");
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG,"btn clicked");
        switch (view.getId()){
            case R.id.btn_city1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn city delhi");
                        btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                        btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                        try {
                            cityId = response.getJSONArray("result").getJSONObject(0).getString("id");
                            Log.d(TAG,"btn city1 : "+cityId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case R.id.btn_city2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn city Mumbai");
                        if (cityMumbai == true){
                            btnCity2.setBackground(getResources().getDrawable(R.drawable.btn_bg3));
                            btnCity1.setBackground(getResources().getDrawable(R.drawable.btn_bg4));
                            try {
                                cityId = response.getJSONArray("result").getJSONObject(1).getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            ToastShow.showToast(getActivity(),"Mumbai coming soon!");
                        }
                        break;
                }
                break;
            case R.id.btn_save_city:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        try {
                            saveCity(cityId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
        return false;
    }

    private void citySaved(JSONObject response) throws JSONException {
        if (response.getString("status").equals("OK")){
            db.updateCity(db.getUserDetails().get("userId"),response.getJSONObject("result").getString("city_id"));
            if (response.getJSONObject("result").getString("city_id").equals("1")){
                valueCallback.setValue("cityName", "Delhi NCR");
            }else {
                valueCallback.setValue("cityName", "Mumbai");
            }
            //getFragmentManager().popBackStack();
            callbackFragOpen.openFrag("homeFrag","");
        }
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("getCities")){
                Log.d(TAG, "response: "+ response);
                try {
                    setData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (tag.equals("saveCity")){
                Log.d(TAG,"city save response: "+ response);
                try {
                    citySaved(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
