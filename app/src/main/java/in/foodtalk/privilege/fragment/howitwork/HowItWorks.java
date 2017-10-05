package in.foodtalk.privilege.fragment.howitwork;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 13-06-2017.
 */

public class HowItWorks extends android.app.Fragment {
    View layout;

    private ViewPager mPager;

    private FragmentActivity myContext;

    private android.support.v4.view.PagerAdapter mPagerAdapter;

    List<android.support.v4.app.Fragment> fragments = new ArrayList<>();

    FragmentManager fm;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.howitwork_frag, container, false);




        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        fragments.add(new ExplorSlide());
        fragments.add(new SelectSlide());
        fragments.add(new DineSlide());
        fragments.add(new ConfirmSlide());
        fragments.add(new PurchaseSlide());


        mPager = (ViewPager) layout.findViewById(R.id.viewpager);
        mPagerAdapter = new HWPagerAdapter(fm, fragments);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        fm = myContext.getSupportFragmentManager();
        super.onAttach(activity);
    }
}
