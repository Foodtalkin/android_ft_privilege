package in.foodtalk.privilege.fragment.howitwork;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 13-06-2017.
 */

public class EnterTour extends Fragment {
    View layout;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.tour_enter, container, false);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        TextView tv3 = (TextView) layout.findViewById(R.id.tv3);
        tvTitle.setTypeface(typeface);

        //Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface futuraMedium = Typeface.createFromAsset(getActivity().getAssets(),"fonts/futura_medium.ttf");
        Typeface futuraBold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/futura_bold.otf");

        tv1.setTypeface(futuraMedium);
        tv2.setTypeface(futuraBold);
        tv3.setTypeface(futuraMedium);

        return layout;
    }
}
