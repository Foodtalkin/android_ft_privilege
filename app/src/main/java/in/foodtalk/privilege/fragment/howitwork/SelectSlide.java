package in.foodtalk.privilege.fragment.howitwork;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 13-06-2017.
 */

public class SelectSlide extends Fragment {
    View layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.slide_select, container, false);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTitle.setTypeface(typeface);
        return layout;
    }
}
