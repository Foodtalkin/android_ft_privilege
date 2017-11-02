package in.foodtalk.privilege.fragment.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.fragment.AccountFrag;
import in.foodtalk.privilege.fragment.account.AccountPager;
import in.foodtalk.privilege.fragment.experiences.ExperiencesFrag;
import in.foodtalk.privilege.fragment.favorites.FavoritesFrag;
import in.foodtalk.privilege.fragment.history.HistoryFrag;

/**
 * Created by RetailAdmin on 31-10-2017.
 */

public class HomeTabFrag extends Fragment {
    View layout;

    ViewPager viewPager;

    FragmentManager fm;

    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.home_tab_frag, container, false);
        viewPager = (ViewPager) layout.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) layout.findViewById(R.id.tab_layout);
        //viewPager.setOffscreenPageLimit(0);
        initViewPager();
        return layout;
    }

    private void initViewPager(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFrag());
        fragments.add(new ExperiencesFrag());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            fm = getChildFragmentManager();
        }else {
            fm = getFragmentManager();
        }
        pagerAdapter = new HomePager(fm, fragments);
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
