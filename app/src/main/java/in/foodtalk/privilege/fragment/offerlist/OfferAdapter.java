package in.foodtalk.privilege.fragment.offerlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.models.OfferCardObj;
import in.foodtalk.privilege.models.SelectOfferObj;

/**
 * Created by RetailAdmin on 08-05-2017.
 */

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = OfferAdapter.class.getSimpleName();
    List<SelectOfferObj> offerCardList;
    Context context;
    LayoutInflater layoutInflater;
    CallbackFragOpen callbackFragOpen;
    public OfferAdapter (Context context, List<SelectOfferObj> offerCardList){
        this.context = context;
        this.offerCardList = offerCardList;
        this.callbackFragOpen = (CallbackFragOpen) context;
        this.layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.offer_card, parent, false);
        OfferCard offerCard = new OfferCard(view);

        return offerCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OfferCard offerCard = (OfferCard) holder;
        SelectOfferObj offerCardObj = offerCardList.get(position);
        offerCard.tvTitle.setText(offerCardObj.title);
        offerCard.tvDes.setText(offerCardObj.shortDescription);
    }

    @Override
    public int getItemCount() {
        return offerCardList.size();
    }

    private class OfferCard extends RecyclerView.ViewHolder implements View.OnTouchListener{

        LinearLayout btnOffer;
        TextView tvTitle, tvDes;
        public OfferCard(View itemView) {
            super(itemView);
            btnOffer = (LinearLayout) itemView.findViewById(R.id.btn_offer);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            btnOffer.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_offer:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "offer clicked");
                            JSONObject offerOutletId = new JSONObject();
                            try {
                                offerOutletId.put("offerId", offerCardList.get(getAdapterPosition()).offerId);
                                offerOutletId.put("outletId", offerCardList.get(getAdapterPosition()).outletId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            callbackFragOpen.openFrag("offerDetailsFrag", offerOutletId.toString());
                            break;
                    }
                    break;
            }
            return false;
        }
    }
}
