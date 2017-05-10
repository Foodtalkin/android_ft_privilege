package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 10-05-2017.
 */

public class RestaurantPin extends Fragment {
    View layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.restaurant_pin, container, false);
        return layout;
    }
}
