package in.foodtalk.privilege.fragment.favorites;

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
import in.foodtalk.privilege.models.FavoriteObj;

/**
 * Created by RetailAdmin on 23-05-2017.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;

    String TAG = FavoritesAdapter.class.getSimpleName();

    CallbackFragOpen callbackFragOpen;

    List<FavoriteObj> favList;

    public FavoritesAdapter (Context context, List<FavoriteObj>favList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.favList = favList;

        callbackFragOpen = (CallbackFragOpen) context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.favorites_card, parent, false);
        FavCard favCard = new FavCard(view);

        return favCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FavoriteObj favoriteObj = favList.get(position);
        FavCard favCard = (FavCard) holder;

        favCard.tvTitle.setText(favoriteObj.name);
        favCard.tvTime.setText(favoriteObj.createdAt);

    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    private class FavCard extends RecyclerView.ViewHolder implements View.OnTouchListener {
        LinearLayout btnOffer;
        TextView tvTitle, tvTime;

        public FavCard(View itemView) {
            super(itemView);

            btnOffer = (LinearLayout) itemView.findViewById(R.id.btn_offer);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            btnOffer.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_offer:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "btn offer clicked");
                            JSONObject offerOutletId = new JSONObject();
                            try {
                                offerOutletId.put("offerId", favList.get(getAdapterPosition()).offerId);
                                offerOutletId.put("outletId", favList.get(getAdapterPosition()).outletId);
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
