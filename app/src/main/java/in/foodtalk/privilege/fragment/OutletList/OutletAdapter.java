package in.foodtalk.privilege.fragment.OutletList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.models.OutletCardObj;

/**
 * Created by RetailAdmin on 05-05-2017.
 */

public class OutletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = OutletAdapter.class.getSimpleName();
    List<OutletCardObj> outletCardList;
    LayoutInflater layoutInflater;
    Context context;
    CallbackFragOpen callbackFragOpen;


    public OutletAdapter (Context context, List<OutletCardObj> outletCardList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        callbackFragOpen = (CallbackFragOpen) context;
        this.outletCardList = outletCardList;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.outlet_card, parent, false);
        OutletCard outletCard = new OutletCard(view);
        return outletCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OutletCardObj outletCardObj = outletCardList.get(position);
        OutletCard outletCard = (OutletCard) holder;
        outletCard.tvTitle.setText(outletCardObj.name);
    }

    @Override
    public int getItemCount() {
        return outletCardList.size();
    }

    class OutletCard extends RecyclerView.ViewHolder implements View.OnTouchListener{
        LinearLayout btnOutlet;
        TextView tvTitle;
        public OutletCard(View itemView) {
            super(itemView);
            btnOutlet = (LinearLayout) itemView.findViewById(R.id.btn_outlet);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            btnOutlet.setOnTouchListener(this);

        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_outlet:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "btn cliced");
                            if (Integer.parseInt(outletCardList.get(getAdapterPosition()).offerCount) > 1){
                                callbackFragOpen.openFrag("selectOfferFrag", outletCardList.get(getAdapterPosition()).id);
                                Log.d(TAG, "goto offer list");
                            }else {
                                Log.d(TAG, "goto offer details");
                                //error with this
                                JSONObject offerOutletId = new JSONObject();
                                try {
                                    offerOutletId.put("offerId", outletCardList.get(getAdapterPosition()).offerIds);
                                    offerOutletId.put("outletId", outletCardList.get(getAdapterPosition()).id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackFragOpen.openFrag("offerDetailsFrag", offerOutletId.toString());
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    }
}
