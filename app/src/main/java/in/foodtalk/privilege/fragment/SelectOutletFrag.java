package in.foodtalk.privilege.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.foodtalk.privilege.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOutletFrag extends Fragment {

    View layout;


    public SelectOutletFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.select_outlet_frag, container, false);
        return layout;
    }
}
