package in.foodtalk.privilege.fragment.experiences;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 21-09-2017.
 */

public class ExpeFrag extends Fragment {
    View layout;

    TextView btnOffer, tv1;

    CallbackFragOpen callbackFragOpen;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.coming_soon_ex, container, false);

        tv1 = (TextView) layout.findViewById(R.id.tv1);

        Typeface typefaceFmedium= Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_medium.ttf");
        Typeface typefaceFutura = Typeface.createFromAsset(getActivity().getAssets(), "fonts/futura_bold.otf");

        callbackFragOpen = (CallbackFragOpen) getActivity();
        btnOffer = (TextView) layout.findViewById(R.id.btn_offers);
        btnOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callbackFragOpen.openFrag("homeFrag","");
            }
        });

        tv1.setTypeface(typefaceFmedium);
        btnOffer.setTypeface(typefaceFutura);
        return layout;
    }
}
