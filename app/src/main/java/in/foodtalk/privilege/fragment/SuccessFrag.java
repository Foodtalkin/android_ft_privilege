package in.foodtalk.privilege.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;

/**
 * Created by RetailAdmin on 11-05-2017.
 */

public class SuccessFrag extends Fragment implements View.OnTouchListener {
    View layout;
    TextView btnDone;

    String TAG = SuccessFrag.class.getSimpleName();
    CallbackFragOpen callbackFragOpen;

    public String rId;
    TextView tvSuccess;

    TextView tvRid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.success_frag, container, false);
        btnDone = (TextView) layout.findViewById(R.id.btn_done);
        callbackFragOpen = (CallbackFragOpen) getActivity();

        tvRid = (TextView) layout.findViewById(R.id.tv_rid);

        tvSuccess  = (TextView) layout.findViewById(R.id.tv_success);

        tvSuccess.setText(Html.fromHtml(getString(R.string.successCopy)));

        tvSuccess.setOnTouchListener(this);


        tvRid.setText("RID : "+rId);

        TextView txtFoodtalk = (TextView) layout.findViewById(R.id.txt_foodtalk);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");
        txtFoodtalk.setTypeface(typeface);

        btnDone.setOnTouchListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return layout;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.btn_done:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "btn done clicked");
                        callbackFragOpen.openFrag("homeFrag","fromRedeemSuccess");
                        break;
                }
                break;
            case R.id.tv_success:
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        email();
                        break;

                }
                break;
        }
        return false;
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
}
