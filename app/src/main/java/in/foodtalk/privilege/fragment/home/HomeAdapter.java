package in.foodtalk.privilege.fragment.home;

import android.content.Context;
import android.content.Loader;
import android.graphics.Point;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.apicall.ApiCall;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.app.DatabaseHandler;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.ApiCallback;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.models.OfferCardObj;

/**
 * Created by RetailAdmin on 03-05-2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private final static String TAG = HomeAdapter.class.getSimpleName();

    List<OfferCardObj> offerCardList;
    LayoutInflater layoutInflater;

    CallbackFragOpen callbackFragOpen;

    public final int VIEW_LOADER = 0;
    public final int VIEW_OFFER = 1;
    public final int VIEW_SAVING = 2;

    DatabaseHandler db;
    String sId;

    String rs;

    int imgSize;

    public HomeAdapter(Context context, List<OfferCardObj> offerCardList){
        this.context = context;
        this.offerCardList = offerCardList;
        layoutInflater = LayoutInflater.from(context);

        callbackFragOpen = (CallbackFragOpen) context;

        //------------
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imgSize = size.x/2;

        db = new DatabaseHandler(context);
        sId = db.getUserDetails().get("sessionId");

        rs = context.getResources().getString(R.string.rs);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_OFFER){
            View view = layoutInflater.inflate(R.layout.home_card, parent, false);
            OfferCard offerCard = new OfferCard(view);
            return offerCard;
        }else if (viewType == VIEW_LOADER){
            View view = layoutInflater.inflate(R.layout.loader_card, parent, false);
            LoaderCard loaderCard = new LoaderCard(view);
            return  loaderCard;
        }else if (viewType == VIEW_SAVING){
            View view = layoutInflater.inflate(R.layout.savings_card, parent, false);
            SavingsCard savingsCard = new SavingsCard(view);
            return savingsCard;
        }else {
            return null;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OfferCard){
            OfferCardObj offerCardObj = offerCardList.get(position);
            OfferCard offerCard = (OfferCard) holder;
            offerCard.tvTitle.setText(offerCardObj.name);
            int cost = Integer.valueOf(offerCardObj.cost);

            String rs = context.getResources().getString(R.string.rs);
            if (cost < 500){
                offerCard.tvPrice.setText(rs);
            }else if (cost < 999){
                offerCard.tvPrice.setText(rs+rs);
            }else {
                offerCard.tvPrice.setText(rs+rs+rs);
            }

           // offerCard.tvPrice.setText(rs+" "+offerCardObj.cost);
            //Log.d(TAG, "outletCount: "+ offerCardObj.outletCount);
            /*if (Integer.parseInt(offerCardObj.outletCount) > 1){
                offerCard.tvLocation.setText(offerCardObj.outletCount+" Location");
            }else if (Integer.parseInt(offerCardObj.offerCount) > 1){
                offerCard.tvLocation.setText(offerCardObj.offerCount+" Offers");
            }else {
                offerCard.tvLocation.setText(offerCardObj.offerCount+" Offer");
            }*/

            offerCard.tvLocation.setText(offerCardObj.primaryCuisine);


            if (offerCardObj.distance.equals("")){
                offerCard.tvDistance.setVisibility(View.GONE);
            }else {
                offerCard.tvDistance.setText(offerCardObj.distance+" KM");
            }


            offerCard.imgView.getLayoutParams().width = imgSize;
            offerCard.imgView.getLayoutParams().height = imgSize;

            Picasso.with(context)
                    .load(offerCardObj.cardImage)

                    //.fit()
                    .placeholder(R.drawable.ic_placeholder)
                    .fit().centerCrop()
                    .into(offerCard.imgView);
        }else if (holder instanceof LoaderCard){
            //Log.d(TAG, "loader view");

           // GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            //layoutParams.setFullSpan(true);
            //layoutParams.setLayoutDirection(1);


            /*final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(true);
                holder.itemView.setLayoutParams(sglp);
            }*/
        }else if (holder instanceof SavingsCard){
            OfferCardObj offerCardObj = offerCardList.get(position);
            SavingsCard savingsCard = (SavingsCard) holder;

            savingsCard.tvLine.setText("You have saved "+rs+" "+offerCardObj.savingAmount);
        }

    }

    @Override
    public int getItemCount() {
        return offerCardList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        Log.d(TAG, "type: "+ offerCardList.get(position).type.equals("loader"));
        if (offerCardList.get(position).type!= null){
            if (offerCardList.get(position).type.equals("loader")){
                return VIEW_LOADER;
            }else if (offerCardList.get(position).type.equals("savings")){
                return VIEW_SAVING;
            }else {
                return VIEW_OFFER;
            }
        }else {
            //Log.d(TAG, "null type position is "+ position);
            return VIEW_OFFER;
        }
    }

    class LoaderCard extends RecyclerView.ViewHolder {

        RelativeLayout loaderView;
        public LoaderCard(View itemView) {
            super(itemView);
            loaderView = (RelativeLayout) itemView.findViewById(R.id.loader_view);
        }
    }
    class SavingsCard extends RecyclerView.ViewHolder{
        TextView tvLine;
        TextView tvLine1;
        Typeface typefaceFmedium= Typeface.createFromAsset(context.getAssets(), "fonts/futura_medium.ttf");
        public SavingsCard(View itemView) {
            super(itemView);
            tvLine = (TextView) itemView.findViewById(R.id.tv_line);
            tvLine1 = (TextView) itemView.findViewById(R.id.tv_line1);
            //tvLine.setTypeface(typefaceFmedium);
            tvLine1.setTypeface(typefaceFmedium);
            tvLine.setText("You have saved "+rs+" --");
            ApiCallback apiCallback1 = new ApiCallback() {
                @Override
                public void apiResponse(JSONObject response, String tag) {
                    //Log.d(TAG,"savings loaded");
                }
            };
           // ApiCall.jsonObjRequest(Request.Method.GET, context, null, Url.URL_PROFILE+"?sessionid="+sId, "savings", apiCallback1);
            //Log.d(TAG,"savings api call");
        }
    }

    class OfferCard extends RecyclerView.ViewHolder implements View.OnTouchListener {
        CardView cardView;
        ImageView imgView;
        TextView tvTitle, tvLocation, tvPrice, tvDistance;
        public OfferCard(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);

            cardView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.card_view:
                    //Log.d(TAG, "card clicked");
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            if (Integer.parseInt(offerCardList.get(getAdapterPosition()).outletCount) > 1){
                                //Log.d(TAG, "open outlet list");
                                callbackFragOpen.openFrag("selectOutletFrag", offerCardList.get(getAdapterPosition()).rId);
                            }else if (Integer.parseInt(offerCardList.get(getAdapterPosition()).offerCount) > 1){
                                //Log.d(TAG, "open offer list");
                                callbackFragOpen.openFrag("selectOfferFrag", offerCardList.get(getAdapterPosition()).outletIds);
                            }else {
                                //Log.d(TAG, "open details");
                                JSONObject offerOutletId = new JSONObject();
                                try {
                                    offerOutletId.put("offerId", offerCardList.get(getAdapterPosition()).offerIds);
                                    offerOutletId.put("outletId", offerCardList.get(getAdapterPosition()).outletIds);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackFragOpen.openFrag("offerDetailsFrag", offerOutletId.toString());
                            }

                            AppController.getInstance().restaurantName = offerCardList.get(getAdapterPosition()).name;
                            AppController.getInstance().rOneLiner = offerCardList.get(getAdapterPosition()).oneLiner;


                            break;
                    }
                    break;
            }
            return false;
        }
    }
}
