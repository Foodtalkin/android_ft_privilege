package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.CallbackKeypad;
import in.foodtalk.privilege.library.Keypad;

/**
 * Created by RetailAdmin on 10-05-2017.
 */

public class RestaurantPin extends Fragment implements CallbackKeypad {
    View layout;
    TextView tvOtp1, tvOtp2, tvOtp3, tvOtp4;

    CallbackFragOpen callbackFragOpen;

    public String jsonString;

    String TAG = RestaurantPin.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.restaurant_pin, container, false);

        Keypad keypad = new Keypad(layout, this);
        callbackFragOpen = (CallbackFragOpen) getActivity();


        tvOtp1 = (TextView) layout.findViewById(R.id.tv_otp1);
        tvOtp2 = (TextView) layout.findViewById(R.id.tv_otp2);
        tvOtp3 = (TextView) layout.findViewById(R.id.tv_otp3);
        tvOtp4 = (TextView) layout.findViewById(R.id.tv_otp4);

        Log.d(TAG, jsonString);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("RestaurantPin","onResume");
    }

    @Override
    public void keyRead(String value) {
        typeOtpAdd(value);
    }

    private void typeOtpAdd(String value){
        if (tvOtp1.length() == 0){
            tvOtp1.setText(value);
        }else if (tvOtp2.length() == 0){
            tvOtp2.setText(value);
        }else if (tvOtp3.length() == 0 ){
            tvOtp3.setText(value);
        }else if (tvOtp4.length() == 0){
            tvOtp4.setText(value);
            //gotoHome();
            callbackFragOpen.openFrag("successFrag", "");
        }

        if (value.equals("")){
            if (tvOtp4.length() > 0){
                tvOtp4.setText(value);
            }else if (tvOtp3.length() > 0){
                tvOtp3.setText(value);
            }else if (tvOtp2.length() > 0 ){
                tvOtp2.setText(value);
            }else if (tvOtp1.length() > 0){
                tvOtp1.setText(value);
            }
        }
    }


}
