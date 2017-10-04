package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 04-10-2017.
 */

public class HelpSupportFrag extends Fragment implements View.OnTouchListener {
    View layout;
    LinearLayout btn1, btn2, btn3;
    String TAG = HelpSupportFrag.class.getSimpleName();
    CallbackFragOpen callbackFragOpen;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.helpsupport_frag, container, false);
        btn1 = (LinearLayout) layout.findViewById(R.id.btn1);
        btn2 = (LinearLayout) layout.findViewById(R.id.btn2);
        btn3 = (LinearLayout) layout.findViewById(R.id.btn3);

        btn1.setOnTouchListener(this);
        btn2.setOnTouchListener(this);
        btn3.setOnTouchListener(this);

        callbackFragOpen = (CallbackFragOpen) getActivity();

        return layout;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "click 1");
                        callbackFragOpen.openFrag("howItWorkFrag", "");
                        break;
                }
                break;
            case R.id.btn2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "click 2");
                        callbackFragOpen.openFrag("faqFrag", "");
                        break;
                }
                break;
            case R.id.btn3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "click 3");
                        callbackFragOpen.openFrag("legalFrag", "");
                        break;
                }
                break;
        }
        return false;
    }
}
