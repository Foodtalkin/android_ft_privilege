package in.foodtalk.privilege.fragment.account;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by RetailAdmin on 29-09-2017.
 */

public class AccountPager extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] tabTitles = new String[]{"Profile", "History", "Bookmarks"};
    public AccountPager(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
