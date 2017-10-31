package in.foodtalk.privilege;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.fragment.AccountFrag;
import in.foodtalk.privilege.fragment.BlankFrag;
import in.foodtalk.privilege.fragment.HelpSupportFrag;
import in.foodtalk.privilege.fragment.OfferDetails.OfferDetailsFrag;
import in.foodtalk.privilege.fragment.OtpVerifyFrag;
import in.foodtalk.privilege.fragment.OutletList.SelectOutletFrag;
import in.foodtalk.privilege.fragment.PaymentFlow;
import in.foodtalk.privilege.fragment.PaymentNowFrag;
import in.foodtalk.privilege.fragment.PaymentPaytm;
import in.foodtalk.privilege.fragment.RestaurantPin;
import in.foodtalk.privilege.fragment.SignupAlert;
import in.foodtalk.privilege.fragment.WebViewFrag;
import in.foodtalk.privilege.fragment.account.AccountTabFrag;
import in.foodtalk.privilege.fragment.city.CitySelectFrag;
import in.foodtalk.privilege.fragment.experiences.ExpeFrag;
import in.foodtalk.privilege.fragment.home.HomeTabFrag;
import in.foodtalk.privilege.fragment.howitwork.HowItWorks;
import in.foodtalk.privilege.fragment.search.SearchFrag;
import in.foodtalk.privilege.fragment.SignupFrag;
import in.foodtalk.privilege.fragment.SuccessFrag;
import in.foodtalk.privilege.fragment.favorites.FavoritesFrag;
import in.foodtalk.privilege.fragment.history.HistoryFrag;
import in.foodtalk.privilege.fragment.home.HomeFrag;
import in.foodtalk.privilege.fragment.offerlist.SelectOfferFrag;
import in.foodtalk.privilege.fragment.search.SearchResult;
import in.foodtalk.privilege.helper.ParseUtils;
import in.foodtalk.privilege.library.PayNow;
import in.foodtalk.privilege.library.ToastShow;

public class MainActivity extends AppCompatActivity implements CallbackFragOpen, View.OnTouchListener, FragmentManager.OnBackStackChangedListener, ValueCallback {

    NavigationView navigationView;
    Fragment currentFragment;
    SearchFrag searchFrag;


    String TAG = MainActivity.class.getSimpleName();

    HomeFrag homeFrag;
    HomeTabFrag homeTabFrag = new HomeTabFrag();
    //FrameLayout container;
    TextView txtFoodtalkNav, txtTitle;
    ImageView navBtn;
    ImageView searchBtn;

    DrawerLayout drawerLayout;



    LinearLayout navLogin, navBuyNow, navHowItWork, navRules, navLegal, navContact, navAbout, forLogin, forLogin1, navAccount, navHistory, navFavourites, navLogout, navHome, navExperines, navCity;

    SuccessFrag successFrag = new SuccessFrag();
    PaymentFlow paymentFlow = new PaymentFlow();
    PaymentPaytm paymentPaytm = new PaymentPaytm();
    OfferDetailsFrag offerDetailsFrag = new OfferDetailsFrag();
    WebViewFrag webViewFrag;
    ExpeFrag expeFrag = new ExpeFrag();
    HelpSupportFrag helpSupportFrag = new HelpSupportFrag();
    HowItWorks howItWorks = new HowItWorks();

    CitySelectFrag citySelectFrag = new CitySelectFrag();
    AccountTabFrag accountTabFrag = new AccountTabFrag();

    DatabaseHandler db;

    LinearLayout offerBarButtons;
    ImageView btnBookmark, btnPhone, btnLocation;

    static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 32;
    static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 14;

    TextView tvPm;

    String versionName;
    TextView txtVersion;

    LinearLayout btnCity;

    TextView tvCityName, tvCityName1, btnLogoutBar;



    //private FirebaseAnalytics firebaseAnalytics;

    String MY_PREFS_NAME = "MyPrefsFile";

    int bExitCount = 0;

    Fragment mContent;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        db = new DatabaseHandler(this);


        //getKeyHash();


        checkPermission();

        txtFoodtalkNav = (TextView) findViewById(R.id.txt_foodtalk);
        tvPm = (TextView) findViewById(R.id.tv_pm);


        forLogin = (LinearLayout) findViewById(R.id.for_login);
        forLogin1 = (LinearLayout) findViewById(R.id.for_login1);

        navLogin = (LinearLayout) findViewById(R.id.nav_login);
        navBuyNow = (LinearLayout) findViewById(R.id.nav_buynow);
        navHowItWork = (LinearLayout) findViewById(R.id.nav_howitwork);
        navRules = (LinearLayout) findViewById(R.id.nav_rules);
        navLegal = (LinearLayout) findViewById(R.id.nav_legal);
        navContact = (LinearLayout) findViewById(R.id.nav_contact);
        navAbout = (LinearLayout) findViewById(R.id.nav_about);

        navAccount = (LinearLayout) findViewById(R.id.nav_account);
        navHistory = (LinearLayout) findViewById(R.id.nav_history);
        navFavourites = (LinearLayout) findViewById(R.id.nav_favourites);
        navLogout = (LinearLayout) findViewById(R.id.nav_logout);
        navHome = (LinearLayout) findViewById(R.id.nav_home);
        navCity = (LinearLayout) findViewById(R.id.nav_city);

        navExperines = (LinearLayout) findViewById(R.id.nav_experines);



        txtVersion = (TextView) findViewById(R.id.txt_version);

        navExperines.setOnTouchListener(this);

        navAccount.setOnTouchListener(this);
        navHistory.setOnTouchListener(this);
        navFavourites.setOnTouchListener(this);
        navLogout.setOnTouchListener(this);


        navLogin.setOnTouchListener(this);
        navBuyNow.setOnTouchListener(this);
        navHowItWork.setOnTouchListener(this);
        navRules.setOnTouchListener(this);
        navLegal.setOnTouchListener(this);
        navContact.setOnTouchListener(this);
        navAbout.setOnTouchListener(this);
        navHome.setOnTouchListener(this);
        navCity.setOnTouchListener(this);


        Log.d(TAG,"check login status: "+ db.getRowCount());
        if (db.getRowCount() > 0){
            loginView();
            AppController.getInstance().sessionId = db.getUserDetails().get("sessionId");
        }else {
            logoutView();
        }
        //container = (FrameLayout) findViewById(R.id.container);
        actionBar();

        navBtn = (ImageView) findViewById(R.id.nav_btn);
        navBtn.setOnTouchListener(this);
        searchBtn = (ImageView) findViewById(R.id.btn_search);
        searchBtn.setOnTouchListener(this);

        btnCity = (LinearLayout) findViewById(R.id.btn_city);
        btnCity.setOnTouchListener(this);

        btnLogoutBar = (TextView) findViewById(R.id.btn_logout_bar);
        btnLogoutBar.setOnTouchListener(this);

        offerBarButtons = (LinearLayout) findViewById(R.id.offer_bar_buttons);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String frag = bundle.getString("fragment");
            Log.d(TAG, "open frag: "+ frag);
            if (frag.equals("signupFrag")){
                //signUp();
                signupAlert();
                Log.d(TAG, "open signup fragment");
            }else if (frag.equals("paymentFlow")){
                startPaymentFlow();
            }
        }else {
            searchFrag = new SearchFrag();
            //homeFrag = new HomeFrag();
            //setFragmentView(homeFrag, R.id.container, "homeFrag", false);
            setFragmentView(homeTabFrag, R.id.container, "homeFrag", false);
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/AbrilFatface_Regular.ttf");

        txtFoodtalkNav.setTypeface(typeface);
        txtTitle = (TextView) findViewById(R.id.title_text);
        txtTitle.setTypeface(typeface);

        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvCityName1 = (TextView) findViewById(R.id.tv_city_name1);


        //checkVersion();




        //---------------
        setUserProperty();

        checkAndOpenPlayStore();
/*
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }*/

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getFragmentManager().putFragment(outState, "myFragmentName", mContent);

    }



    private void setUserProperty(){
        //Sets whether analytics collection is enabled for this app on this device.
        AppController.getInstance().firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        //firebaseAnalytics.setMinimumSessionDuration(20000);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        // firebaseAnalytics.setSessionTimeoutDuration(500);

        //Sets the user ID property.
        //Sets a user property to a given value.
        if (db.getRowCount() > 0){
            Log.e(TAG, "set User paid");
            AppController.getInstance().firebaseAnalytics.setUserProperty("User", "Paid");
            if (!db.getUserDetails().get("gender").equals("")){
                AppController.getInstance().firebaseAnalytics.setUserProperty("gender", db.getUserDetails().get("gender"));
                Log.e(TAG, "set user pro gender");
            }
            //Log.i(TAG, "set user gender: "+db.getUserDetails().get("gender"));
            AppController.getInstance().firebaseAnalytics.setUserId(db.getUserDetails().get("userId"));
        }else {
            Log.e(TAG, "set User unpaid");
            AppController.getInstance().firebaseAnalytics.setUserProperty("User", "Unpaid");
            AppController.getInstance().firebaseAnalytics.setUserId("Guest");
        }
    }
    private void checkVersion(){
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (versionName != null){
            if (BuildConfig.BUILD_TYPE.equals("debug")){
                txtVersion.setText("Version "+versionName+" "+BuildConfig.BUILD_TYPE);
            }
            else {
                txtVersion.setText("Version "+versionName);
            }
        }
    }

    private void logoutView(){
        forLogin.setVisibility(View.GONE);
        //forLogin1.setVisibility(View.GONE);
        navLogin.setVisibility(View.VISIBLE);
        navBuyNow.setVisibility(View.VISIBLE);
        txtFoodtalkNav.setText("FOODTALK");
        tvPm.setText("");
    }
    public void loginView(){
        forLogin.setVisibility(View.VISIBLE);
        //forLogin1.setVisibility(View.VISIBLE);
        navLogin.setVisibility(View.GONE);
        navBuyNow.setVisibility(View.GONE);
        txtFoodtalkNav.setText(db.getUserDetails().get("name"));
        tvPm.setText("Privilege Member");

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

        //ImageView btnS = (ImageView) mActionBar.getCustomView().findViewById(R.id.btn_search);
        //btnS.setScaleX(2);
    }
    private void onFragmentChange(Fragment fragment){
        if (fragment == homeFrag){
            searchBtn.setVisibility(View.VISIBLE);
        }else {
            searchBtn.setVisibility(View.INVISIBLE);
        }
        if (fragment == offerDetailsFrag ){
            offerBarButtons.setVisibility(View.VISIBLE);
        }else {
            offerBarButtons.setVisibility(View.INVISIBLE);
        }
        if (fragment == webViewFrag){

        }
        if (fragment == accountTabFrag){
            btnLogoutBar.setVisibility(View.VISIBLE);
            Log.d(TAG,"show logout button");
        }else {
           //Log.d(TAG, "fragment: "+ fragment.getClass().getSimpleName());
            btnLogoutBar.setVisibility(View.GONE);
            Log.d(TAG,"hide logout button");
        }

    }
    public void setFragmentView(Fragment newFragment, int container, String tag, Boolean bStack){

        /*if (tag.equals("successFrag")){
            //mActionBar.setDisplayShowCustomEnabled(false);
            getSupportActionBar().hide();
        }else {
            getSupportActionBar().show();
           // mActionBar.setDisplayShowCustomEnabled(true);
        }*/

        bExitCount = 0;

        onFragmentChange(newFragment);

        String fragmentName = newFragment.getClass().getName();

        //--logEvent---
        AppController.getInstance().logEvent(1, newFragment.getClass().getSimpleName(), "Screen");
        //AppController.getInstance().firebaseAnalytics.setCurrentScreen(this, "cScreen", newFragment.getClass().getSimpleName());
        //-------------------
        currentFragment = newFragment;
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container,newFragment,tag);
        if (bStack){
            transaction.addToBackStack(fragmentName);
        }
        transaction.commit();
    }

    private void signUp(String type){
        SignupFrag signupFrag = new SignupFrag();
        signupFrag.type = type;
        setFragmentView(signupFrag, R.id.container, "signupFrag", true);
    }
    private void signupAlert(){
        SignupAlert signupAlert = new SignupAlert();
        setFragmentView(signupAlert, R.id.container, "signupAlert", true);
    }


    @Override
    public void openFrag(String fragName, String value) {
        if (fragName.equals("selectOfferFrag")){
            SelectOfferFrag selectOfferFrag = new SelectOfferFrag();
            selectOfferFrag.outletId = value;
            setFragmentView(selectOfferFrag, R.id.container, "selectOfferFrag", true);
            hideSoftKeyboard();
        }
        if (fragName.equals("selectOutletFrag")){
            SelectOutletFrag selectOutletFrag = new SelectOutletFrag();
            selectOutletFrag.rId = value;
            setFragmentView(selectOutletFrag, R.id.container, "selectOutletFrag", true);
            hideSoftKeyboard();
        }
        if (fragName.equals("offerDetailsFrag")){
            offerDetailsFrag = new OfferDetailsFrag();
            //offerDetailsFrag.outletId = value;
            Log.d(TAG, "value for offerDetailsF: "+ value);
            hideSoftKeyboard();
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
            restaurantPin.jsonString = value;
            setFragmentView(restaurantPin, R.id.container, "offerDetailsFrag", true);
        }
        if (fragName.equals("homeFrag")){
           /* HomeFrag homeFrag = new HomeFrag();
            setFragmentView(homeFrag, R.id.container, "homeFrag", false);

            if (value.equals("fromRedeemSuccess")){
                checkAndOpenPlayStore();
            }
            clearBackStack();*/
          // recreate();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        if (fragName.equals("successFrag")){

            successFrag.rId = value;
            setFragmentView(successFrag, R.id.container, "successFrag", false);
        }
        if (fragName.equals("signupAlert")){
            signupAlert();
        }
        if (fragName.equals("otpVerify")){
            OtpVerifyFrag otpVerifyFrag = new OtpVerifyFrag();
            otpVerifyFrag.phone = value;
            setFragmentView(otpVerifyFrag, R.id.container, "otpVerify", true);
        }
        if (fragName.equals("searchResult")){
            SearchResult searchResult = new SearchResult();
            searchResult.offerUrl = value;
            setFragmentView(searchResult, R.id.container, "searchResult", true);
        }
        if (fragName.equals("signUp")){
            signUp(value);
        }
        if (fragName.equals("paymentNowFrag")){
            PaymentNowFrag paymentNowFrag = new PaymentNowFrag();
            setFragmentView(paymentNowFrag, R.id.container, "paymentNowFrag", true);
        }
        if (fragName.equals("paymentFlow")){
            startPaymentFlow();
        }

        if (fragName.equals("searchFrag")){
            setFragmentView(searchFrag, R.id.container, "searchFrag", true);
        }
        if (fragName.equals("webViewFrag")){
            webView(value);
        }
        if (fragName.equals("selectCityFrag")){
            selectCity();
        }
        if (fragName.equals("howItWorkFrag")){
            setFragmentView(howItWorks, R.id.container, "paymentPaytm", true);
        }
        if (fragName.equals("faqFrag")){
            webView("http://foodtalk.in/app/faq.html");
        }
        if (fragName.equals("legalFrag")){
            webView("http://foodtalk.in/app/legal.html");
            AppController.getInstance().fbLogEvent("legal_view", null);
        }
    }

    private void startPaymentFlow(){
        //setFragmentView(paymentFlow, R.id.container, "paymentFlow", false);
        setFragmentView(paymentPaytm, R.id.container, "paymentPaytm", false);
    }

    @Override
    public void onBackPressed() {
        int totalBS = getFragmentManager().getBackStackEntryCount();
        if (this.getFragmentManager().findFragmentById(R.id.container) == successFrag ){
            //setFragmentView(homeFrag, R.id.container, "homeFrag", false);
            clearBackStack();
            //this.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else if (this.getFragmentManager().findFragmentById(R.id.container) == offerDetailsFrag){
            if (offerDetailsFrag.imgSliderVisible == true){
                offerDetailsFrag.hideImgSlider();
            }else if (offerDetailsFrag.redeemBarVisible == true){
                offerDetailsFrag.hideRedeemBar();
            }else {
                if (totalBS < 1){
                    if (bExitCount > 0){
                        super.onBackPressed();
                    }else {
                        bExitCount++;
                        ToastShow.showToast(this,"Press again to exit");
                    }
                }else {
                    super.onBackPressed();
                }
            }
        }else {
            if (totalBS < 1){
                if (bExitCount > 0){
                    super.onBackPressed();
                }else {
                    bExitCount++;
                    ToastShow.showToast(this,"Press again to exit");
                }
            }else {
                super.onBackPressed();
            }

        }
    }
    private void clearBackStack() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void hideSoftKeyboard() {
        if(this.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void logOut() {
        Log.e("Home","logOut function call");
        //logoutView();
        db.resetTables();
        //LoginManager.getInstance().logOut();
        Intent i = new Intent(this, Splash_activity.class);
        startActivity(i);
        finish();
    }
    private void email(){
        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

/* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"contact@foodtalkindia.com"});
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void webView(String url){
        webViewFrag = new WebViewFrag();
        webViewFrag.url = url;
        setFragmentView(webViewFrag, R.id.container, "webViewLegal", true);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }



    private void sendPaymentResponse(String status, String orderId, String transactionId, String paymentId){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG,"permission was granted");

                } else {
                    Log.d(TAG,"permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.nav_btn:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        drawerLayout.openDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("Navigation_button", null);
                        break;
                }
                break;
            case R.id.nav_login:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        AppController.getInstance().fbLogEvent("login_view", null);
                        break;
                }
                break;
            case R.id.nav_buynow:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        //signUp();
                        signupAlert();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("buynow_view", null);
                        break;
                }
                break;
            case R.id.btn_search:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d("MainActivity","search btn clicked");
                        setFragmentView(searchFrag, R.id.container, "searchFrag", true);

                        break;
                }
                break;
            case R.id.nav_logout:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"logout clicked");
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("logout", null);
                        //logOut();
                        logoutDialog();
                        break;
                }
                break;
            case R.id.nav_contact:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        email();
                        AppController.getInstance().fbLogEvent("contact_us_view", null);
                        break;
                }
                break;
            case R.id.nav_favourites:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        FavoritesFrag favoritesFrag = new FavoritesFrag();
                        setFragmentView(favoritesFrag, R.id.container, "favoritesFrag", true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("favorites_view", null);
                        break;
                }
                break;
            case R.id.nav_history:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        HistoryFrag historyFrag = new HistoryFrag();
                        setFragmentView(historyFrag, R.id.container, "historyFrag", true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("history_view", null);
                        break;
                }
                break;
            case R.id.nav_legal:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        webView("http://foodtalk.in/app/legal.html");
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("legal_view", null);
                        break;
                }
                break;
            case R.id.nav_rules:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        webView("http://foodtalk.in/app/faq.html");
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.nav_account:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        AccountFrag accountFrag = new AccountFrag();
                        setFragmentView(accountTabFrag, R.id.container, "accountTabFrag", true);

                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("account_view", null);
                        break;
                }
                break;
            case R.id.nav_about:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "click about nav");
                        //PayNow payNow = new PayNow(this);
                        //payNow.paymentWithOrder("FOZH5L9GZHHG4cSQDzuPIJyRPjlFs4","5efa71cc-da8f-4c2f-9195-075381af1bf9");
                        //paymentWithOrder();
                        //{"access_token":"FOZH5L9GZHHG4cSQDzuPIJyRPjlFs4","paymentid":"6ec55f4e77ff4f7a9cded4939e283b10","order":{"order_id":"5efa71cc-da8f-4c2f-9195-075381af1bf9","name":"hsus","email":"hsu@hsj.com","phone":"+915424542454","amount":"999.00"}},"api":"\/\/subscriptionPayment?sessionid=575b5a9e34db298ab736facc1940fffeaf590e2d"}
                        //paymentWithOrder();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.nav_howitwork:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        //HowItWorks howItWorks = new HowItWorks();
                        setFragmentView(helpSupportFrag, R.id.container, "historyFrag", true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        AppController.getInstance().fbLogEvent("Help & Support", null);
                        break;
                }
                break;
            case R.id.nav_home:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        //HomeFrag homeFrag = new HomeFrag();
                       // setFragmentView(homeFrag, R.id.container, "homeFrag", true);
                        //-----------
                        if (currentFragment != homeFrag){
                            clearBackStack();
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            AppController.getInstance().fbLogEvent("home_view", null);
                            Intent intent = new Intent(this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }

                        break;
                }
                break;
            case R.id.nav_experines:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Log.d(TAG, "nav exprines");
                        experines();
                        AppController.getInstance().fbLogEvent("experiences_view", null);
                        break;
                }
                break;
            case R.id.nav_city:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        selectCity();
                        break;
                }
                break;
            case R.id.btn_city:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        selectCity();
                        break;
                }
                break;
            case R.id.btn_logout_bar:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        logOut();
                        break;
                }
                break;
        }
        return false;
    }
    private void selectCity(){
        if (currentFragment != citySelectFrag){
            AppController.getInstance().fbLogEvent("city_view", null);
            setFragmentView(citySelectFrag, R.id.container, "searchFrag", true);
        }
    }
    private void experines(){
        if (db.getRowCount() > 0){
            if (db.getUserDetails().get("cityId").equals("1")){
                String url = "http://foodtalk.in/pe/#!/app/"+db.getUserDetails().get("sessionId");
                Log.d(TAG, url);
                webView(url);
            }else {
                setFragmentView(expeFrag, R.id.container, "expeFrag", false);
            }

        }else {
            signupAlert();
        }
    }

    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        currentFragment = getFragmentManager().findFragmentById(R.id.container);
        onFragmentChange(currentFragment);

        /*if (currentFragment == homeFrag){
            //finish();
            //startActivity(getIntent());
            BlankFrag blankFrag = new BlankFrag();
            setFragmentView(blankFrag, R.id.container, "blankFrag", false);

            navContact.post(new Runnable() {
                public void run() {
                    setFragmentView(homeFrag, R.id.container, "homeFrag", false);
                }
            });
        }*/
    }
    Dialog dialogLogout;
    private void logoutDialog(){
        // custom dialog

        dialogLogout = new Dialog(this);
        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.setContentView(R.layout.logout_alert_dialog);


        TextView cancel = (TextView) dialogLogout.findViewById(R.id.btn_cancel);
        TextView logout = (TextView) dialogLogout.findViewById(R.id.btn_logout1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogout.dismiss();
                //tvVeg.setText("No");
            }
        });
        // if button is clicked, close the custom dialog
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogout.dismiss();
                logOut();
                //tvVeg.setText("Yes");
            }
        });

        dialogLogout.show();
    }
    Dialog dialogRating;
    private void ratingDialog(){
        // custom dialog

        dialogRating = new Dialog(this);
        dialogRating.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRating.setContentView(R.layout.ratenow_dialog);


        TextView rateNow = (TextView) dialogRating.findViewById(R.id.btn_ratenow);
        TextView letter = (TextView) dialogRating.findViewById(R.id.btn_letter);
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRating.dismiss();
                //3424
                //tvVeg.setText("No");
            }
        });
        // if button is clicked, close the custom dialog

        rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRating.dismiss();
                Log.d(TAG, "rate now");
                openPlayStore();
                Log.d(TAG, "rate first time");
                //logOut();
                //tvVeg.setText("Yes");
            }
        });
        dialogRating.show();
    }

    private void checkAndOpenPlayStore(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String ratingPopup = prefs.getString("ratingPopup", "No name defined");//"No name defined" is the default value.
            //int idName = prefs.getInt("idName", 0); //0 is the default value.
            Log.d(TAG, "sp name is: "+ ratingPopup);
            if (ratingPopup.equals("1")){
                ratingDialog();
            }
        }else {
            Log.d(TAG, "restoredText is null");
        }
    }

    private void openPlayStore(){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }

        // MY_PREFS_NAME - a static String variable like:

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("ratingPopup", "1");
        //editor.putInt("idName", 12);
        editor.apply();
    }
    final static int REQUEST_LOCATION = 199;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        //Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();
                        Log.d(TAG,"Location enabled by user!");
                        if (currentFragment == homeFrag){
                            //homeFrag.refreshFeed();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                   // homeFrag.refreshFeed();
                                }
                            }, 3000);
                        }
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        // Toast.makeText(getActivity(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Location not enabled, user cancelled.");
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    private void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "in.foodtalk.privilege",  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", hashKey);
               // ToastShow.showToast(this, hashKey);



                ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("hashKey", hashKey);
                clipboard.setPrimaryClip(clip);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void setValue(String v1, String v2) {
        if (v1.equals("cityName")){
            tvCityName.setText(v2);
            tvCityName1.setText(v2);
            AppController.getInstance().cityName = v2;
        }
    }
}
