package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 31-05-2017.
 */

public class SignupAlert extends Fragment implements View.OnTouchListener {
    View layout;
    LinearLayout btnSignup;
    CallbackFragOpen callbackFragOpen;
    TextView txtFoodtalk, tvHead, tvRs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.signup_alert, container, false);
        btnSignup = (LinearLayout) layout.findViewById(R.id.btn_signup);
        btnSignup.setOnTouchListener(this);

        tvHead = (TextView) layout.findViewById(R.id.tv_head);
        tvRs = (TextView) layout.findViewById(R.id.tv_rs);

        callbackFragOpen = (CallbackFragOpen) getActivity();


        txtFoodtalk = (TextView) layout.findViewById(R.id.txt_foodtalk);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface futuraMedium = Typeface.createFromAsset(getActivity().getAssets(),"fonts/futura_medium.ttf");
        Typeface futuraBold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/futura_bold.otf");
        txtFoodtalk.setTypeface(typeface);
        tvHead.setTypeface(futuraBold);
        tvRs.setTypeface(typeface);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return layout;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_signup:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        callbackFragOpen.openFrag("signUp","payment");
                        break;
                }
                break;
        }
        return false;
    }
}
