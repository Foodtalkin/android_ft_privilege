package in.foodtalk.privilege;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;

/**
 * Created by RetailAdmin on 27-04-2017.
 */

public class Login extends AppCompatActivity implements View.OnTouchListener, ApiCallback {

    final String TAG = "LoginPhone";
    TextView key1, key2, key3 ,key4, key5, key6, key7, key8, key9, key0;
    ImageView keyBack;
    TextView tvPhone, tvNext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        key1 = (TextView) findViewById(R.id.key1);
        key2 = (TextView) findViewById(R.id.key2);
        key3 = (TextView) findViewById(R.id.key3);
        key4 = (TextView) findViewById(R.id.key4);
        key5 = (TextView) findViewById(R.id.key5);
        key6 = (TextView) findViewById(R.id.key6);
        key7 = (TextView) findViewById(R.id.key7);
        key8 = (TextView) findViewById(R.id.key8);
        key9 = (TextView) findViewById(R.id.key9);
        key0 = (TextView) findViewById(R.id.key0);
        keyBack = (ImageView) findViewById(R.id.key_back);

        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnTouchListener(this);

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

        tvPhone = (TextView) findViewById(R.id.tv_phone);
    }
    private void typeOtpAdd(String value){
        if (!value.equals("")){
            tvPhone.append(value);
        }else if (tvPhone.length() > 0){
            tvPhone.setText(method(tvPhone.getText().toString()));
        }
    }

    public String method(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    private void checkUser() {

        if (tvPhone.getText().toString().length() == 10){
            ApiCall.jsonObjRequest(Request.Method.GET, this, null, Url.CHECK_USER+"/"+tvPhone.getText().toString(), "checkUser", this);
        }
    }
    private void gotoOtp(JSONObject response){
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            String phone = tvPhone.getText().toString();
            if (status.equals("OK")){
                Intent intent = new Intent(Login.this, LoginOtp.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }else {
                Log.e(TAG,"error Msg: "+ message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.key1:
                        typeOtpAdd("1");
                        Log.d(TAG, "1");
                        break;
                    case R.id.key2:
                        typeOtpAdd("2");
                        Log.d(TAG, "2");
                        break;
                    case R.id.key3:
                        typeOtpAdd("3");
                        Log.d(TAG, "3");
                        break;
                    case R.id.key4:
                        typeOtpAdd("4");
                        Log.d(TAG, "4");
                        break;
                    case R.id.key5:
                        typeOtpAdd("5");
                        Log.d(TAG, "5");
                        break;
                    case R.id.key6:
                        typeOtpAdd("6");
                        Log.d(TAG, "6");
                        break;
                    case R.id.key7:
                        typeOtpAdd("7");
                        Log.d(TAG, "7");
                        break;
                    case R.id.key8:
                        typeOtpAdd("8");
                        Log.d(TAG, "8");
                        break;
                    case R.id.key9:
                        typeOtpAdd("9");
                        Log.d(TAG, "9");
                        break;
                    case R.id.key0:
                        typeOtpAdd("0");
                        Log.d(TAG, "0");
                        break;
                    case R.id.key_back:
                        typeOtpAdd("");
                        Log.d(TAG, "back");
                        break;
                    case R.id.tv_next:
                        checkUser();
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        if (response != null){
            if (tag.equals("checkUser")){
                Log.d(TAG,"response: "+response);
                gotoOtp(response);
            }
        }
    }
}
