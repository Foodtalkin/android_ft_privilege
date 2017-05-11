package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 11-05-2017.
 */

public class SuccessFrag extends Fragment implements View.OnTouchListener {
    View layout;
    TextView btnDone;

    String TAG = SuccessFrag.class.getSimpleName();
    CallbackFragOpen callbackFragOpen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.success_frag, container, false);
        btnDone = (TextView) layout.findViewById(R.id.btn_done);
        callbackFragOpen = (CallbackFragOpen) getActivity();

        TextView txtFoodtalk = (TextView) layout.findViewById(R.id.txt_foodtalk);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        txtFoodtalk.setTypeface(typeface);

        btnDone.setOnTouchListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return layout;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_done:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn done clicked");
                        callbackFragOpen.openFrag("homeFrag","");
                        break;
                }
                break;
        }
        return false;
    }
}
