package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class SearchFrag extends Fragment implements View.OnTouchListener {
    View layout;

    LinearLayout btnLocation1, btnLocation2, btnLocation3, btnLocation4, btnLocation5, btnLocation6,
            btnCost1, btnCost2, btnCost3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.search_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        btnLocation1 = (LinearLayout) layout.findViewById(R.id.btn_location1);
        btnLocation2 = (LinearLayout) layout.findViewById(R.id.btn_location2);
        btnLocation3 = (LinearLayout) layout.findViewById(R.id.btn_location3);
        btnLocation4 = (LinearLayout) layout.findViewById(R.id.btn_location4);
        btnLocation5 = (LinearLayout) layout.findViewById(R.id.btn_location5);
        btnLocation6 = (LinearLayout) layout.findViewById(R.id.btn_location6);

        btnCost1 = (LinearLayout) layout.findViewById(R.id.btn_cost1);
        btnCost2 = (LinearLayout) layout.findViewById(R.id.btn_cost2);
        btnCost3 = (LinearLayout) layout.findViewById(R.id.btn_cost3);

        return layout;
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_location1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:

                        break;
                }
                break;
            case R.id.btn_location2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_location3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_location4:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_location5:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_location6:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_cost1:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_cost2:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
            case R.id.btn_cost3:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                }
                break;
        }
        return false;
    }
}
