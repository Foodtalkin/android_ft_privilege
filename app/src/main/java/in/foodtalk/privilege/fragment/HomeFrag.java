package in.foodtalk.privilege.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {

    View layout;

    CallbackFragOpen callbackFragOpen;


    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_frag, container, false);

        callbackFragOpen = (CallbackFragOpen) getActivity();


        CardView cardView = (CardView) layout.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HomeFrag","click");
                callbackFragOpen.openFrag("selectOfferFrag","");
            }
        });


        return layout;
    }

}
