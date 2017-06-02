package in.foodtalk.privilege.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

/**
 * Created by RetailAdmin on 02-06-2017.
 */

public class PayNow {
    String TAG = PayNow.class.getSimpleName();
    Context context;
    Activity activity;
    public PayNow(Activity activity){
        this.activity = activity;
    }
    private void paymentWithOrder(String accessToken, String orderId){
        // Good time to show dialog
        Request request = new Request(accessToken, orderId, new OrderRequestCallBack() {
            @Override
            public void onFinish(final Order order, final Exception error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionError) {
                                ToastShow.showToast(activity, "No internet connection");
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

    private void startPreCreatedUI(Order order){
        //Using Pre created UI
        Intent intent = new Intent(activity, PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE);
    }
}
