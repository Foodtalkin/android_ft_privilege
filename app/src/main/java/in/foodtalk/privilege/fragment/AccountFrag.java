package in.foodtalk.privilege.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.library.StringCase;

/**
 * Created by RetailAdmin on 15-05-2017.
 */

public class AccountFrag extends Fragment implements View.OnTouchListener {
    View layout;

    TextView tvGender, tvVeg, tvName, tvPhone;

    EditText inputName, inputEmail;

    static TextView tvDob;
    public DatePickerDialog myDialog;

    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.account_frag, container, false);
        inputName = (EditText) layout.findViewById(R.id.input_name);
        inputEmail = (EditText) layout.findViewById(R.id.input_email);
        tvGender = (TextView) layout.findViewById(R.id.tv_gender);
        tvVeg = (TextView) layout.findViewById(R.id.tv_veg);

        tvName = (TextView) layout.findViewById(R.id.tv_name);
        tvPhone = (TextView) layout.findViewById(R.id.tv_phone);

        db = new DatabaseHandler(getActivity());

        inputName.setText(StringCase.caseSensitive(db.getUserDetails().get("name")));
        inputEmail.setText(db.getUserDetails().get("email"));

        tvName.setText(StringCase.caseSensitive(db.getUserDetails().get("name")));
        tvPhone.setText(db.getUserDetails().get("phone"));

        //Log.d("accountfrag", db.getUserDetails().get("phone"));

        tvDob = (TextView) layout.findViewById(R.id.tv_dob);
        tvDob.setOnTouchListener(this);
        return layout;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;
        }
        return false;
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
            tvDob.setText(month+"/"+day+"/"+year);
        }

    }
}
