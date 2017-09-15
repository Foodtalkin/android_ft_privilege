package in.foodtalk.privilege.fragment.city;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.library.ToastShow;

/**
 * Created by RetailAdmin on 13-09-2017.
 */

public class CitySelectFrag extends Fragment implements View.OnTouchListener, ApiCallback {
    View layout;

    String TAG = CitySelectFrag.class.getSimpleName();

    LinearLayout btnCity1, btnCity2;
    TextView tvRestaurantCity1, tvOutletCity1, tvRestaurantCity2, tvOutletCity2;

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

        btnCity1.setOnTouchListener(this);
        btnCity2.setOnTouchListener(this);

        loadCities();
        return layout;
    }

    private void loadCities(){
        ApiCall.jsonObjRequest(Request.Method.GET, getActivity(), null, Url.URL_CITIES, "getCities", this);
    }

    Boolean cityMumbai;
    private void setData(JSONObject response) throws JSONException {
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
                        }else {
                            ToastShow.showToast(getActivity(),"Mumbai coming soon!");
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
            if (tag.equals("getCities")){
                Log.d(TAG, "response: "+ response);
                try {
                    setData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
