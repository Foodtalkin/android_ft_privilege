package in.foodtalk.privilege.library;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.LoginOtp;
import in.foodtalk.privilege.MainActivity;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.models.LoginValue;

/**
 * Created by RetailAdmin on 19-06-2017.
 */

public class SaveLogin {

    public static void addUser (Context context, JSONObject response) throws JSONException{
        DatabaseHandler db = new DatabaseHandler(context);
        String status = response.getString("status");
        String message = response.getString("message");
        if (status.equals("OK")){
            //callbackFragOpen.openFrag("homeFrag","");
            LoginValue loginValue = new LoginValue();
            loginValue.sId = response.getJSONObject("result").getJSONObject("session").getString("session_id");
            loginValue.rToken = response.getJSONObject("result").getJSONObject("session").getString("refresh_token");
            loginValue.uId = response.getJSONObject("result").getJSONObject("session").getString("user_id");
            //loginValue.createAt = response.getJSONObject("result").getJSONObject("session").getString("created_at");
            //loginValue.updateAt = response.getJSONObject("result").getJSONObject("session").getString("updated_at");


            JSONObject result = response.getJSONObject("result");
            JSONArray subscription = result.getJSONArray("subscription");

            loginValue.name = ((result.isNull("name")) ? "N/A" : result.getString("name"));
            loginValue.email = ((result.isNull("email")) ? "N/A" : result.getString("email"));
            loginValue.phone = ((result.isNull("phone")) ? "N/A" : result.getString("phone"));
            loginValue.gender = ((result.isNull("gender")) ? "N/A" : result.getString("gender"));
            loginValue.dob = ((result.isNull("dob")) ? "N/A" : result.getString("dob"));
            loginValue.pref = result.getString("preference");
            loginValue.uId = result.getString("id");

            loginValue.subscription = subscription.toString();
            db.addUser(loginValue);

            //name = ((result.isNull("name")) ? "N/A" : result.getString("name"));

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }else {
            Log.e("SaveLogin", "message: "+ message);
        }
    }

}
