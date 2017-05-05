package in.foodtalk.privilege.fragment.home;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.foodtalk.privilege.R;
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

    String rs;

    public HomeAdapter(Context context, List<OfferCardObj> offerCardList){
        this.context = context;
        this.offerCardList = offerCardList;
        layoutInflater = LayoutInflater.from(context);

        callbackFragOpen = (CallbackFragOpen) context;

        rs = context.getResources().getString(R.string.rs);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.home_card, parent, false);
        OfferCard offerCard = new OfferCard(view);
        return offerCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OfferCardObj offerCardObj = offerCardList.get(position);
        OfferCard offerCard = (OfferCard) holder;
        offerCard.tvTitle.setText(offerCardObj.name);
        offerCard.tvPrice.setText(rs+" "+offerCardObj.cost);
        Log.d(TAG, "outletCount: "+ offerCardObj.outletCount);
        if (Integer.parseInt(offerCardObj.outletCount) > 1){
            offerCard.tvLocation.setText("Location "+offerCardObj.outletCount);
        }else if (Integer.parseInt(offerCardObj.offerCount) > 1){
            offerCard.tvLocation.setText("Offers "+offerCardObj.offerCount);
        }else {
            offerCard.tvLocation.setText("Offer "+offerCardObj.offerCount);
        }

        Picasso.with(context)
                .load(offerCardObj.cardImage)
                .fit().centerCrop()
                //.fit()
                .placeholder(R.drawable.bitmap)
                .into(offerCard.imgView);
    }

    @Override
    public int getItemCount() {
        return offerCardList.size();
    }

    class OfferCard extends RecyclerView.ViewHolder implements View.OnTouchListener {
        CardView cardView;
        ImageView imgView;
        TextView tvTitle, tvLocation, tvPrice;
        public OfferCard(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);

            cardView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.card_view:
                    Log.d(TAG, "card clicked");
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            if (Integer.parseInt(offerCardList.get(getAdapterPosition()).outletCount) > 1){
                                Log.d(TAG, "open outlet list");
                                callbackFragOpen.openFrag("selectOutletFrag", offerCardList.get(getAdapterPosition()).rId);
                            }else if (Integer.parseInt(offerCardList.get(0).offerCount) > 1){
                                Log.d(TAG, "open offer list");
                                callbackFragOpen.openFrag("selectOfferFrag", offerCardList.get(getAdapterPosition()).outletIds);
                            }else {
                                Log.d(TAG, "open details");
                                callbackFragOpen.openFrag("offerDetailsFrag", offerCardList.get(getAdapterPosition()).outletIds);
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    }
}
