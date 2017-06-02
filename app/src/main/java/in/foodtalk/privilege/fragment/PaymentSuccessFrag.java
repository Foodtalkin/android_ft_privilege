package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 02-06-2017.
 */

public class PaymentSuccessFrag extends Fragment {
    View layout;
    TextView btnContinue;

    CallbackFragOpen callbackFragOpen;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.payment_successful, container, false);

        callbackFragOpen = (CallbackFragOpen) getActivity();

        btnContinue = (TextView) layout.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackFragOpen.openFrag("homeFrag","");
            }
        });
        return layout;
    }
}
