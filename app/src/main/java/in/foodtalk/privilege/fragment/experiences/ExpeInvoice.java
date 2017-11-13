package in.foodtalk.privilege.fragment.experiences;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 13-11-2017.
 */

public class ExpeInvoice extends Fragment {
    View layout;

    TextView tvTitle, tvTime, tvTime1, tvAddress, tvAddress1, tvVegNon, tvSubTotal, tvAmount, tvTax, tvFee, tvTotalAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.experiences_invoice, container, false);
        tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTime = (TextView) layout.findViewById(R.id.tv_time);
        tvTime1 = (TextView) layout.findViewById(R.id.tv_time1);
        tvAddress = (TextView) layout.findViewById(R.id.tv_address);
        tvAddress1 = (TextView) layout.findViewById(R.id.tv_address1);
        tvVegNon = (TextView) layout.findViewById(R.id.tv_veg_non);
        tvSubTotal = (TextView) layout.findViewById(R.id.tv_subtotal);
        tvAmount = (TextView) layout.findViewById(R.id.tv_amount);
        tvTax = (TextView) layout.findViewById(R.id.tv_amount);
        tvFee = (TextView) layout.findViewById(R.id.tv_fee);
        return layout;
    }

    private void loadData(){

    }
}
