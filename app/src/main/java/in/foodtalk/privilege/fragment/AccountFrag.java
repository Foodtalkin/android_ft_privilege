package in.foodtalk.privilege.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.library.DateFunction;
import in.foodtalk.privilege.library.StringCase;
import in.foodtalk.privilege.library.ToastShow;
import in.foodtalk.privilege.models.LoginValue;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class AccountFrag extends Fragment implements View.OnTouchListener, ApiCallback {
    View layout;

    String TAG = AccountFrag.class.getSimpleName();

    TextView tvGender, tvVeg, tvName, tvPhone, tvMembership;

    EditText inputName, inputEmail;

    RelativeLayout progressBar;

    static TextView tvDob;
    public DatePickerDialog myDialog;

    DatabaseHandler db;

    Dialog dialogPref;
    Dialog dialogGender;

    LinearLayout btnSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.account_frag, container, false);
        inputName = (EditText) layout.findViewById(R.id.input_name);
        inputEmail = (EditText) layout.findViewById(R.id.input_email);
        tvGender = (TextView) layout.findViewById(R.id.tv_gender);
        tvVeg = (TextView) layout.findViewById(R.id.tv_veg);

        tvMembership = (TextView) layout.findViewById(R.id.tv_membership);

        progressBar = (RelativeLayout) layout.findViewById(R.id.progress_bar);
        btnSave = (LinearLayout) layout.findViewById(R.id.btn_save);
        btnSave.setOnTouchListener(this);

        tvVeg.setOnTouchListener(this);
        tvGender.setOnTouchListener(this);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AbrilFatface_Regular.ttf");

        tvName = (TextView) layout.findViewById(R.id.tv_name);
        tvPhone = (TextView) layout.findViewById(R.id.tv_phone);

        tvName.setTypeface(typeface);

        db = new DatabaseHandler(getActivity());



        //Log.d("accountfrag", db.getUserDetails().get("phone"));

        tvDob = (TextView) layout.findViewById(R.id.tv_dob);
        tvDob.setOnTouchListener(this);

        tvGender.setOnTouchListener(this);

        setTextValue();
        setPref();
        setGender();
        return layout;
    }

    private void sendToServer() throws JSONException{

        progressBar.setVisibility(View.VISIBLE);

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String dob = tvDob.getText().toString();
        String gender = tvGender.getText().toString();
        String veg = tvVeg.getText().toString();

        JSONObject jsonObject = new JSONObject();
        if (!name.equals("")){
            jsonObject.put("name", name);
        }
        if (!email.equals("")){
            jsonObject.put("email", email);
        }
        if (!dob.equals("DD/MM/YYYY")){
            jsonObject.put("dob", dob);
        }
        if (!gender.equals("select")){
            jsonObject.put("gender", gender);
        }
        if (veg.equals("Yes")){
            jsonObject.put("preference", "vegetarian");
        }else if (veg.equals("No")){
            jsonObject.put("preference", "non vegetarian");
        }
        Log.d(TAG, "json obj: "+jsonObject);
        ApiCall.jsonObjRequest(Request.Method.PUT, getActivity(), jsonObject, Url.USER_UPDATE+"?sessionid="+db.getUserDetails().get("sessionId"), "userUpdate", this);
    }

    public void setTextValue(){

        Log.d(TAG, "subscription: "+ db.getUserDetails().get("subscription"));
        try {
            JSONArray subscritionArray = new JSONArray(db.getUserDetails().get("subscription"));
            String date = subscritionArray.getJSONObject(0).getString("expiry");
            date = DateFunction.convertFormat(date, "yyyy-MM-dd HH:mm:ss", "d MMM yyyy");
            tvMembership.setText("Membership valid till "+ date);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        inputName.setText(StringCase.caseSensitive(db.getUserDetails().get("name")));
        inputEmail.setText(db.getUserDetails().get("email"));

        inputName.setSelection(inputName.getText().length());
        inputEmail.setSelection(inputEmail.getText().length());

        tvName.setText(StringCase.caseSensitive(db.getUserDetails().get("name")));
        tvPhone.setText(db.getUserDetails().get("phone"));

        String dob = db.getUserDetails().get("dob");
        String gender = db.getUserDetails().get("gender");
        String pref = db.getUserDetails().get("pref");
        if (dob.equals("")){
            tvDob.setText("DD/MM/YYYY");
        }else {
            tvDob.setText(dob);
        }
        if (gender.equals("")){
            tvGender.setText("select");
        }else {
            tvGender.setText(gender);
        }
        if (pref.equals("vegetarian")){
            tvVeg.setText("Yes");
        }else {
            tvVeg.setText("No");
        }
        Log.d(TAG, "dob: "+ db.getUserDetails().get("dob"));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                switch (view.getId()){
                    case R.id.tv_dob:
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");
                        break;
                    case R.id.tv_veg:
                        dialogPref.show();
                        Log.d(TAG,"open dialog");
                        break;
                    case R.id.tv_gender:
                        dialogGender.show();
                        break;
                    case R.id.btn_save:
                        try {
                            sendToServer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void apiResponse(JSONObject response, String tag) {
        progressBar.setVisibility(View.GONE);
        if (response != null){
            Log.d(TAG,"response: " +response);
            try {
                saveToDB(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            ToastShow.showToast(getActivity(),"Check internet connection");
        }
    }
    /*
    values.put(KEY_NAME, loginValue.name);
        values.put(KEY_EMAIL, loginValue.email);
        values.put(KEY_PHONE, loginValue.phone);
        values.put(KEY_GENDER, loginValue.gender);
        values.put(KEY_PREF, loginValue.pref);
        values.put(KEY_DOB, loginValue.dob);
     */

    private void saveToDB(JSONObject response) throws JSONException{

        if (response.getString("status").equals("OK")){
            LoginValue loginValue = new LoginValue();

            loginValue.name = response.getJSONObject("result").getString("name");
            loginValue.phone = response.getJSONObject("result").getString("phone");
            loginValue.gender = response.getJSONObject("result").getString("gender");
            loginValue.pref = response.getJSONObject("result").getString("preference");
            loginValue.dob = response.getJSONObject("result").getString("dob");
            loginValue.uId = response.getJSONObject("result").getString("id");
            loginValue.email = response.getJSONObject("result").getString("email");

            Log.d(TAG, "check userId: "+ loginValue.uId + ": "+ db.getUserDetails().get("userId"));
            db.updateUserInfo(loginValue.uId, loginValue);

            //setTextValue();
        }else {
            Log.d(TAG, "error: "+ response.getString("message"));
        }

    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            //String date = DateFunction.convertFormat(day+"/"+month+"/"+year,"dd-MM-yyyy","d MMM yyyy");
            String date = DateFunction.changeDateFormat(day+"-"+month+"-"+year,"dd-MM-yyyy","d MMM yyyy");
            tvDob.setText(day+"-"+month+"-"+year);
        }

    }

    private void setGender(){
        dialogGender = new Dialog(getActivity());
        dialogGender.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGender.setContentView(R.layout.gender_dialog);

        TextView male = (TextView) dialogGender.findViewById(R.id.btn_male);
        TextView female = (TextView) dialogGender.findViewById(R.id.btn_female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGender.dismiss();
                tvGender.setText("Male");
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGender.dismiss();
                tvGender.setText("Female");
            }
        });
    }

    private void setPref(){
        // custom dialog
        dialogPref = new Dialog(getActivity());
        dialogPref.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPref.setContentView(R.layout.pref_dialog);


        TextView veg = (TextView) dialogPref.findViewById(R.id.btn_veg);
        TextView nonVeg = (TextView) dialogPref.findViewById(R.id.btn_nonveg);
        nonVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPref.dismiss();
                tvVeg.setText("No");
            }
        });
        // if button is clicked, close the custom dialog
        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPref.dismiss();
                tvVeg.setText("Yes");
            }
        });
    }
}
