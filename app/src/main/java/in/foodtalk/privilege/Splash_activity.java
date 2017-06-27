package in.foodtalk.privilege;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.fragment.howitwork.ConfirmSlide;
import in.foodtalk.privilege.fragment.howitwork.DineSlide;
import in.foodtalk.privilege.fragment.howitwork.ExplorSlide;
import in.foodtalk.privilege.fragment.howitwork.HWPagerAdapter;
import in.foodtalk.privilege.fragment.howitwork.PurchaseSlide;
import in.foodtalk.privilege.fragment.howitwork.SelectSlide;
import in.foodtalk.privilege.fragment.howitwork.SplashSlide;

public class Splash_activity extends AppCompatActivity implements View.OnTouchListener {

    TextView txtLogo, txtLogin, txtExplore;
    final String TAG = "Splash_activity";

    private FragmentActivity myContext;

    private android.support.v4.view.PagerAdapter mPagerAdapter;

    List<Fragment> fragments = new ArrayList<>();

    FragmentManager fm;

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        txtLogo = (TextView) findViewById(R.id.txt_logo);
        txtLogin = (TextView) findViewById(R.id.txt_login);
        txtExplore = (TextView) findViewById(R.id.txt_explore);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/AbrilFatface_Regular.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/futura_bold.otf");

        txtExplore.setTypeface(typeface1);
        txtLogin.setTypeface(typeface1);
        txtLogo.setTypeface(typeface);

        txtLogin.setOnTouchListener(this);
        txtExplore.setOnTouchListener(this);

        initViewPager();

    }

    private void initViewPager(){
        fragments.add(new SplashSlide());
        fragments.add(new ExplorSlide());
        fragments.add(new SelectSlide());
        fragments.add(new DineSlide());
        fragments.add(new ConfirmSlide());
        fragments.add(new PurchaseSlide());

        fm = getSupportFragmentManager();

        //mPager = (ViewPager) layout.findViewById(R.id.viewpager);
        mPagerAdapter = new HWPagerAdapter(fm, fragments);
        viewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.txt_login:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        gotoLogin();
                        Log.d(TAG, "LoginOtp");
                        break;
                }
                break;
            case R.id.txt_explore:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        gotoMain();
                        Log.d(TAG, "Main");
                        break;
                }
                break;
        }
        return false;
    }

    private void gotoLogin(){
        Intent intent = new Intent(Splash_activity.this, Login.class);
        startActivity(intent);
    }
    private void gotoMain(){
        Intent intent = new Intent(Splash_activity.this, MainActivity.class);
        startActivity(intent);
    }
}
