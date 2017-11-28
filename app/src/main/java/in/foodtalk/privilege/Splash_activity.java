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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.fragment.howitwork.ConfirmSlide;
import in.foodtalk.privilege.fragment.howitwork.DineSlide;
import in.foodtalk.privilege.fragment.howitwork.DiningTour;
import in.foodtalk.privilege.fragment.howitwork.EnterTour;
import in.foodtalk.privilege.fragment.howitwork.ExperiTour;
import in.foodtalk.privilege.fragment.howitwork.ExplorSlide;
import in.foodtalk.privilege.fragment.howitwork.HWPagerAdapter;
import in.foodtalk.privilege.fragment.howitwork.LandingTour;
import in.foodtalk.privilege.fragment.howitwork.OffersTour;
import in.foodtalk.privilege.fragment.howitwork.PurchaseSlide;
import in.foodtalk.privilege.fragment.howitwork.RedeemTour;
import in.foodtalk.privilege.fragment.howitwork.SelectSlide;
import in.foodtalk.privilege.fragment.howitwork.SplashSlide;

public class Splash_activity extends AppCompatActivity implements View.OnTouchListener {

    TextView txtLogo, txtLogin, txtExplore, btnNext, tv1;
    final String TAG = "Splash_activity";

    LinearLayout exploreHolder;

    private FragmentActivity myContext;

    private android.support.v4.view.PagerAdapter mPagerAdapter;

    List<Fragment> fragments = new ArrayList<>();

    FragmentManager fm;

    String firstCopy = "Swipe to know more";
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        txtLogo = (TextView) findViewById(R.id.txt_logo);
        txtLogin = (TextView) findViewById(R.id.txt_login);
        txtExplore = (TextView) findViewById(R.id.txt_explore);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText(firstCopy);

        exploreHolder = (LinearLayout) findViewById(R.id.explore_holder);

        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnTouchListener(this);

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
        /*fragments.add(new SplashSlide());
        fragments.add(new ExplorSlide());
        fragments.add(new SelectSlide());
        fragments.add(new DineSlide());
        fragments.add(new ConfirmSlide());
        fragments.add(new PurchaseSlide());*/

        fragments.add(new LandingTour());
        fragments.add(new ExperiTour());
        fragments.add(new DiningTour());
        //fragments.add(new OffersTour());
       // fragments.add(new RedeemTour());
        fragments.add(new EnterTour());

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
                Log.d(TAG, "vp postion: "+ position);
                if (position == 3){
                    exploreHolder.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                    tv1.setVisibility(View.GONE);
                    //txtExplore.setVisibility(View.VISIBLE);
                }else {
                    tv1.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    exploreHolder.setVisibility(View.GONE);
                    //txtExplore.setVisibility(View.GONE);
                }
               switch (position){
                   case 0:
                       tv1.setText(firstCopy);
                       break;
                   case 1:
                       tv1.setText("Book a seat at the most exciting events in the city, curated by Food Talk. Exclusively for Privilege members.");
                       break;
                   case 2:
                       tv1.setText("Unlock minimum six coupons per restaurant. Enjoy a year full of dining privileges.");
                       break;
                   case 3:
                       //tv1.setText("Search for your favourite restaurants or discover a restaurant you haven’t tried before!");
                       //tv1.setText("Just ask your server to enter the unique PIN to redeem your offer.");
                       break;
                   case 4:
                       tv1.setText("Just ask your server to enter the unique PIN to redeem your offer.");
                       break;
                   case 5:
                       break;
               }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
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
                        AppController.getInstance().fbLogEvent("First_Enter", null);
                        break;
                }
                break;
            case R.id.btn_next:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        viewPager.setCurrentItem(getItem(+1),true);
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
