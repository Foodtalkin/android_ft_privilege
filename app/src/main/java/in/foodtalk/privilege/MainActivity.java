package in.foodtalk.privilege;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.fragment.AccountFrag;
import in.foodtalk.privilege.fragment.OfferDetails.OfferDetailsFrag;
import in.foodtalk.privilege.fragment.OtpVerifyFrag;
import in.foodtalk.privilege.fragment.OutletList.SelectOutletFrag;
import in.foodtalk.privilege.fragment.PaymentNowFrag;
import in.foodtalk.privilege.fragment.RestaurantPin;
import in.foodtalk.privilege.fragment.SignupAlert;
import in.foodtalk.privilege.fragment.WebViewFrag;
import in.foodtalk.privilege.fragment.search.SearchFrag;
import in.foodtalk.privilege.fragment.SignupFrag;
import in.foodtalk.privilege.fragment.SuccessFrag;
import in.foodtalk.privilege.fragment.favorites.FavoritesFrag;
import in.foodtalk.privilege.fragment.history.HistoryFrag;
import in.foodtalk.privilege.fragment.home.HomeFrag;
import in.foodtalk.privilege.fragment.offerlist.SelectOfferFrag;
import in.foodtalk.privilege.fragment.search.SearchResult;
import in.foodtalk.privilege.library.ToastShow;

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

    LinearLayout navLogin, navBuyNow, navHowItWork, navRules, navLegal, navContact, navAbout, forLogin, forLogin1, navAccount, navHistory, navFavourites, navLogout;

    SuccessFrag successFrag = new SuccessFrag();

    DatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        db = new DatabaseHandler(this);





        txtFoodtalkNav = (TextView) findViewById(R.id.txt_foodtalk);

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




        if (db.getRowCount() > 0){
            forLogin.setVisibility(View.VISIBLE);
            forLogin1.setVisibility(View.VISIBLE);
            navLogin.setVisibility(View.GONE);
            navBuyNow.setVisibility(View.GONE);
        }else {
            forLogin.setVisibility(View.GONE);
            forLogin1.setVisibility(View.GONE);
            navLogin.setVisibility(View.VISIBLE);
            navBuyNow.setVisibility(View.VISIBLE);
        }
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
    public void setFragmentView(Fragment newFragment, int container, String tag, Boolean bStack){

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

    private void signUp(){
        SignupFrag signupFrag = new SignupFrag();
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
            OfferDetailsFrag offerDetailsFrag = new OfferDetailsFrag();
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
            setFragmentView(homeFrag, R.id.container, "homeFrag", false);
        }
        if (fragName.equals("successFrag")){
            setFragmentView(successFrag, R.id.container, "successFrag", false);
        }
        if (fragName.equals("signupAlert")){
            signupAlert();
        }
        if (fragName.equals("otpVerify")){
            OtpVerifyFrag otpVerifyFrag = new OtpVerifyFrag();
            otpVerifyFrag.phone = value;
            setFragmentView(otpVerifyFrag, R.id.container, "otpVerify", false);
        }
        if (fragName.equals("searchResult")){
            SearchResult searchResult = new SearchResult();
            searchResult.offerUrl = value;
            setFragmentView(searchResult, R.id.container, "searchResult", true);
        }
        if (fragName.equals("signUp")){
            signUp();
        }
        if (fragName.equals("paymentNowFrag")){
            PaymentNowFrag paymentNowFrag = new PaymentNowFrag();
            setFragmentView(paymentNowFrag, R.id.container, "paymentNowFrag", true);
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

    public void hideSoftKeyboard() {
        if(this.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void logOut() {
        Log.e("Home","logOut function call");
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
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@foodtalkindia.com"});
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void webView(String url){
        WebViewFrag webViewFrag = new WebViewFrag();
        webViewFrag.url = url;
        setFragmentView(webViewFrag, R.id.container, "webViewLegal", true);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }
    private void paymentWithOrder(){
        // Good time to show dialog
        Request request = new Request("accessToken", "orderID", new OrderRequestCallBack() {
            @Override
            public void onFinish(final Order order, final Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionError) {
                                ToastShow.showToast(getApplication(), "No internet connection");
                                //showToast("No internet connection");
                            } else if (error instanceof Errors.ServerError) {
                                //ToastShow.showToast(getApplicationContext(),"Server Error. Try again");
                                Log.e(TAG, "Server Error. Try again");
                                //showToast("Server Error. Try again");
                            } else if (error instanceof Errors.AuthenticationError) {
                                Log.e(TAG, "Access token is invalid or expired. Please Update the token!!");
                                //ToastShow.showToast(getApplicationContext(), "Access token is invalid or expired. Please Update the token!!");
                                //showToast("Access token is invalid or expired. Please Update the token!!");
                            } else {
                                Log.d(TAG, error.toString());
                                //showToast(error.toString());
                            }
                            return;
                        }

                        startPreCreatedUI(order);
                    }
                });

            }
        });
        request.execute();
    }
    private void payment(){
        Order order = new Order("k6cBh0Jk7c4XvY2Ho6kbph4p4oOeDv", "1c1aad6e07154789bb5b022604bc9c41", "Mandeep", "android@foodtalkindia.com", "8383083893", "10", "membership999");

       //orderId: 016af1ae8c5744c1bbc59cfd0367ed39
        //transactionId: 72a8d3f8510a43628bf768d975108bce
        //paymentId: MOJO7602005A46505780

        Instamojo.setBaseUrl("https://test.instamojo.com/");

        if (!order.isValid()){
            //oops order validation failed. Pinpoint the issue(s).

            if (!order.isValidName()){
                Log.e("App", "Buyer name is invalid");
            }

            if (!order.isValidEmail()){
                Log.e("App", "Buyer email is invalid");
            }

            if (!order.isValidPhone()){
                Log.e("App", "Buyer phone is invalid");
            }

            if (!order.isValidAmount()){
                Log.e("App", "Amount is invalid");
            }

            if (!order.isValidDescription()){
                Log.e("App", "description is invalid");
            }

            if (!order.isValidTransactionID()){
                Log.e("App", "Transaction ID is invalid");
            }

            if (!order.isValidRedirectURL()){
                Log.e("App", "Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
                //showToast("Webhook URL is invalid");
                Log.e("App", "Webhook URL is invalid");
            }

            return;
        }

        //Validation is successful. Proceed

        // Good time to show progress dialog to user
        Request request = new Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(Order order, Exception error) {
                //dismiss the dialog if showed

                // Make sure the follwoing code is called on UI thread to show Toasts or to
                //update UI elements
                if (error != null) {
                    if (error instanceof Errors.ConnectionError) {
                        Log.e("App", "No internet connection");
                    } else if (error instanceof Errors.ServerError) {
                        Log.e("App", "Server Error. Try again");
                    } else if (error instanceof Errors.AuthenticationError){
                        Log.e("App", "Access token is invalid or expired");
                    } else if (error instanceof Errors.ValidationError){
                        // Cast object to validation to pinpoint the issue
                        Errors.ValidationError validationError = (Errors.ValidationError) error;
                        if (!validationError.isValidTransactionID()) {
                            Log.e("App", "Transaction ID is not Unique");
                            return;
                        }
                        if (!validationError.isValidRedirectURL()) {
                            Log.e("App", "Redirect url is invalid");
                            return;
                        }


                        if (!validationError.isValidWebhook()) {
                            //showToast("Webhook url is invalid");
                            Log.d(TAG, "Webhook url is invalid");
                            return;
                        }

                        if (!validationError.isValidPhone()) {
                            Log.e("App", "Buyer's Phone Number is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidEmail()) {
                            Log.e("App", "Buyer's Email is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidAmount()) {
                            Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                            return;
                        }
                        if (!validationError.isValidName()) {
                            Log.e("App", "Buyer's Name is required");
                            return;
                        }
                    } else {
                        Log.e("App", error.getMessage());
                    }
                    return;
                }

                startPreCreatedUI(order);

            }

        });
        request.execute();

    }

    private void startPreCreatedUI(Order order){
        //Using Pre created UI
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (orderID != null && transactionID != null && paymentID != null) {
                //Check for Payment status with Order ID or Transaction ID
                Log.d(TAG,"onActivityResult: orderId: "+ orderID);
                Log.d(TAG,"onActivityResult: transactionId: "+ transactionID);
                Log.d(TAG,"onActivityResult: paymentId: "+ paymentID);
            } else {
                //Oops!! Payment was cancelled
            }
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
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        break;
                }
                break;
            case R.id.nav_buynow:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        signUp();
                        drawerLayout.closeDrawer(Gravity.LEFT);
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
                        logOut();
                        break;
                }
                break;
            case R.id.nav_contact:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        email();
                        break;
                }
                break;
            case R.id.nav_favourites:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        FavoritesFrag favoritesFrag = new FavoritesFrag();
                        setFragmentView(favoritesFrag, R.id.container, "favoritesFrag", true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.nav_history:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        HistoryFrag historyFrag = new HistoryFrag();
                        setFragmentView(historyFrag, R.id.container, "historyFrag", true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.nav_legal:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        webView("http://foodtalk.in/app/legal.html");
                        drawerLayout.closeDrawer(Gravity.LEFT);
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
                        setFragmentView(accountFrag, R.id.container, "historyFrag", true);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
            case R.id.nav_about:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "click about nav");
                        //payment();
                        paymentWithOrder();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                break;
        }
        return false;
    }
}
