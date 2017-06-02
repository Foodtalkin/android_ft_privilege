package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 02-06-2017.
 */

public class PaymentFailedFrag extends Fragment {
    View layout;
    TextView btnRetry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.payment_failed, container, false);
        btnRetry = (TextView) layout.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return layout;
    }
}
