package in.foodtalk.privilege;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.fragment.HomeFrag;
import in.foodtalk.privilege.fragment.SelectOfferFrag;

public class MainActivity extends AppCompatActivity implements CallbackFragOpen {

    NavigationView navigationView;
    Fragment currentFragment;

    HomeFrag homeFrag;
    //FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        //container = (FrameLayout) findViewById(R.id.container);
        actionBar();

        homeFrag = new HomeFrag();
        setFragmentView(homeFrag, R.id.container, "HomeFrag", false);
    }

    private void actionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public  void setFragmentView(Fragment newFragment, int container, String tag, Boolean bStack){

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
            setFragmentView(selectOfferFrag, R.id.container, "selectOfferFrag", false);
        }
    }
}
