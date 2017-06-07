package in.foodtalk.privilege.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instamojo.android.Instamojo;
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
        Instamojo.setBaseUrl("https://test.instamojo.com/");
    }
    public  void paymentWithOrder(String accessToken, String orderId){
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
    public void payment(String accessToken, String transactionID, String name, String email, String phone, String amount, String purpose){
        Order order = new Order(accessToken, transactionID, name, email, phone, amount, purpose);

        order.setWebhook("http://stg-api.foodtalk.in/webhook/instamojo");

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
                Log.d(TAG,"order: "+ order);
                Log.d(TAG,"order: id "+ order.getId());
                Log.d(TAG,"order: T id "+ order.getTransactionID());
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