package in.foodtalk.privilege.library;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackKeypad;

/**
 * Created by RetailAdmin on 11-05-2017.
 */

public class Keypad implements View.OnTouchListener {
    TextView key1, key2, key3 ,key4, key5, key6, key7, key8, key9, key0;
    ImageView keyBack;
    String TAG = Keypad.class.getSimpleName();
    View layout;
    CallbackKeypad callbackKeypad;
    private Activity activity;
    public Keypad(View layout, CallbackKeypad callbackKeypad){
        this.activity = activity;
        this.callbackKeypad = callbackKeypad;
        this.layout = layout;
        key1 = (TextView) layout.findViewById(R.id.key1);
        key2 = (TextView) layout.findViewById(R.id.key2);
        key3 = (TextView) layout.findViewById(R.id.key3);
        key4 = (TextView) layout.findViewById(R.id.key4);
        key5 = (TextView) layout.findViewById(R.id.key5);
        key6 = (TextView) layout.findViewById(R.id.key6);
        key7 = (TextView) layout.findViewById(R.id.key7);
        key8 = (TextView) layout.findViewById(R.id.key8);
        key9 = (TextView) layout.findViewById(R.id.key9);
        key0 = (TextView) layout.findViewById(R.id.key0);
        keyBack = (ImageView) layout.findViewById(R.id.key_back);

        key1.setOnTouchListener(this);
        key2.setOnTouchListener(this);
        key3.setOnTouchListener(this);
        key4.setOnTouchListener(this);
        key5.setOnTouchListener(this);
        key6.setOnTouchListener(this);
        key7.setOnTouchListener(this);
        key8.setOnTouchListener(this);
        key9.setOnTouchListener(this);
        key0.setOnTouchListener(this);
        keyBack.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.key1:
                        Log.d(TAG, "1");
                        callbackKeypad.keyRead("1");
                        break;
                    case R.id.key2:
                        Log.d(TAG, "2");
                        callbackKeypad.keyRead("2");
                        break;
                    case R.id.key3:
                        Log.d(TAG, "3");
                        callbackKeypad.keyRead("3");
                        break;
                    case R.id.key4:
                        Log.d(TAG, "4");
                        callbackKeypad.keyRead("4");
                        break;
                    case R.id.key5:
                        Log.d(TAG, "5");
                        callbackKeypad.keyRead("5");
                        break;
                    case R.id.key6:
                        Log.d(TAG, "6");
                        callbackKeypad.keyRead("6");
                        break;
                    case R.id.key7:
                        Log.d(TAG, "7");
                        callbackKeypad.keyRead("7");
                        break;
                    case R.id.key8:
                        Log.d(TAG, "8");
                        callbackKeypad.keyRead("8");
                        break;
                    case R.id.key9:
                        Log.d(TAG, "9");
                        callbackKeypad.keyRead("9");
                        break;
                    case R.id.key0:
                        Log.d(TAG, "0");
                        callbackKeypad.keyRead("0");
                        break;
                    case R.id.key_back:
                        Log.d(TAG, "back");
                        callbackKeypad.keyRead("");
                        break;
                }
                break;
        }
        return false;
    }
}
