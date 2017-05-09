package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 02-05-2017.
 */

public class OfferDetailsFrag extends Fragment implements View.OnTouchListener {
    View layout;
    TextView tvCounter, btnCancel, btnNext;
    Animation slideUpAnimation, slideDownAnimation, slideUpAnimation1;
    LinearLayout redeemBar, btnRedeem, btnSlideUp;
    Boolean redeemBarVisible = false;
    String TAG = OfferDetailsFrag.class.getSimpleName();
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
        btnCancel.setOnTouchListener(this);
        btnNext.setOnTouchListener(this);
        btnRedeem.setOnTouchListener(this);
        btnSlideUp.setOnTouchListener(this);

        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");
        tvCounter.setTypeface(typefaceFutura);

        setAnimation();
        return layout;
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
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
        }
        return false;
    }
}
