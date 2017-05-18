package in.foodtalk.privilege;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.fragment.OfferDetails.OfferDetailsFrag;
import in.foodtalk.privilege.fragment.OtpVerifyFrag;
import in.foodtalk.privilege.fragment.OutletList.SelectOutletFrag;
import in.foodtalk.privilege.fragment.RestaurantPin;
import in.foodtalk.privilege.fragment.SearchFrag;
import in.foodtalk.privilege.fragment.SignupFrag;
import in.foodtalk.privilege.fragment.SuccessFrag;
import in.foodtalk.privilege.fragment.home.HomeFrag;
import in.foodtalk.privilege.fragment.offerlist.SelectOfferFrag;

public class MainActivity extends AppCompatActivity implements CallbackFragOpen, View.OnTouchListener {

    NavigationView navigationView;
    Fragment currentFragment;
    SearchFrag searchFrag;

    String TAG = MainActivity.class.getSimpleName();

    HomeFrag homeFrag;
    //FrameLayout container;
    TextView txtFoodtalkNav, txtTitle;
    ImageView navBtn;
    ImageView searchBtn;

    DrawerLayout drawerLayout;

    LinearLayout navLogin, navBuyNow, navHowItWork, navRules, navLegal, navContact, navAbout;

    SuccessFrag successFrag = new SuccessFrag();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        txtFoodtalkNav = (TextView) findViewById(R.id.txt_foodtalk);

        navLogin = (LinearLayout) findViewById(R.id.nav_login);
        navBuyNow = (LinearLayout) findViewById(R.id.nav_buynow);
        navHowItWork = (LinearLayout) findViewById(R.id.nav_howitwork);
        navRules = (LinearLayout) findViewById(R.id.nav_rules);
        navLegal = (LinearLayout) findViewById(R.id.nav_contact);
        navContact = (LinearLayout) findViewById(R.id.nav_contact);
        navAbout = (LinearLayout) findViewById(R.id.nav_about);

        navLogin.setOnTouchListener(this);
        navBuyNow.setOnTouchListener(this);
        navHowItWork.setOnTouchListener(this);
        navRules.setOnTouchListener(this);
        navLegal.setOnTouchListener(this);
        navContact.setOnTouchListener(this);
        navAbout.setOnTouchListener(this);

        //container = (FrameLayout) findViewById(R.id.container);
        actionBar();

        navBtn = (ImageView) findViewById(R.id.nav_btn);
        navBtn.setOnTouchListener(this);
        searchBtn = (ImageView) findViewById(R.id.btn_search);
        searchBtn.setOnTouchListener(this);


        searchFrag = new SearchFrag();
        homeFrag = new HomeFrag();
        setFragmentView(homeFrag, R.id.container, "homeFrag", false);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/AbrilFatface_Regular.ttf");

        txtFoodtalkNav.setTypeface(typeface);
        txtTitle = (TextView) findViewById(R.id.title_text);
        txtTitle.setTypeface(typeface);
    }
    ActionBar mActionBar;
    private void actionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public  void setFragmentView(Fragment newFragment, int container, String tag, Boolean bStack){

        /*if (tag.equals("successFrag")){
            //mActionBar.setDisplayShowCustomEnabled(false);
            getSupportActionBar().hide();
        }else {
            getSupportActionBar().show();
           // mActionBar.setDisplayShowCustomEnabled(true);
        }*/

        String fragmentName = newFragment.getClass().getName();
        currentFragment = newFragment;
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container,newFragment,tag);
        if (bStack){
            transaction.addToBackStack(fragmentName);
        }
        transaction.commit();
    }

    @Override
    public void openFrag(String fragName, String value) {
        if (fragName.equals("selectOfferFrag")){
            SelectOfferFrag selectOfferFrag = new SelectOfferFrag();
            selectOfferFrag.outletId = value;
            setFragmentView(selectOfferFrag, R.id.container, "selectOfferFrag", true);
        }
        if (fragName.equals("selectOutletFrag")){
            SelectOutletFrag selectOutletFrag = new SelectOutletFrag();
            selectOutletFrag.rId = value;
            setFragmentView(selectOutletFrag, R.id.container, "selectOutletFrag", true);
        }
        if (fragName.equals("offerDetailsFrag")){
            OfferDetailsFrag offerDetailsFrag = new OfferDetailsFrag();
            //offerDetailsFrag.outletId = value;
            Log.d(TAG, value);
            try {
                JSONObject offerOutletId = new JSONObject(value);
                offerDetailsFrag.offerId = offerOutletId.getString("offerId");
                offerDetailsFrag.outletId = offerOutletId.getString("outletId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setFragmentView(offerDetailsFrag, R.id.container, "offerDetailsFrag", true);
        }
        if (fragName.equals("restaurantPin")){
            RestaurantPin restaurantPin = new RestaurantPin();
            setFragmentView(restaurantPin, R.id.container, "offerDetailsFrag", true);
        }
        if (fragName.equals("homeFrag")){
            setFragmentView(homeFrag, R.id.container, "homeFrag", false);
        }
        if (fragName.equals("successFrag")){
            setFragmentView(successFrag, R.id.container, "successFrag", false);
        }
        if (fragName.equals("signupFrag")){
            SignupFrag signupFrag = new SignupFrag();
            setFragmentView(signupFrag, R.id.container, "signupFrag", false);
        }
        if (fragName.equals("otpVerify")){
            OtpVerifyFrag otpVerifyFrag = new OtpVerifyFrag();
            otpVerifyFrag.phone = value;
            setFragmentView(otpVerifyFrag, R.id.container, "otpVerify", false);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.getFragmentManager().findFragmentById(R.id.container) == successFrag ){
            //setFragmentView(homeFrag, R.id.container, "homeFrag", false);
            clearBackStack();
            //this.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else {
            super.onBackPressed();
        }

    }
    private void clearBackStack() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.nav_btn:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        drawerLayout.openDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.nav_login:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.btn_search:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d("MainActivity","search btn clicked");
                        setFragmentView(searchFrag, R.id.container, "searchFrag", false);
                        break;
                }
                break;
        }
        return false;
    }
}
