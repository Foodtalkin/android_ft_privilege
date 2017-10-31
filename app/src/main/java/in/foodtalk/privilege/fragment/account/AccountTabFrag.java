package in.foodtalk.privilege.fragment.account;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.FragmentManager;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.fragment.AccountFrag;
import in.foodtalk.privilege.fragment.favorites.FavoritesFrag;
import in.foodtalk.privilege.fragment.history.HistoryFrag;

/**
 * Created by RetailAdmin on 29-09-2017.
 */

public class AccountTabFrag extends Fragment {
    View layout;

    ViewPager viewPager;

    FragmentManager fm;

    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.account_tabs_frag, container, false);
        viewPager = (ViewPager) layout.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) layout.findViewById(R.id.tab_layout);
        initViewPager();
        return layout;
    }

    private void initViewPager(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AccountFrag());
        fragments.add(new HistoryFrag());
        fragments.add(new FavoritesFrag());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            fm = getChildFragmentManager();
        }else {
            fm = getFragmentManager();
        }
        pagerAdapter = new AccountPager(fm, fragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }
}
