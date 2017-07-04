package in.foodtalk.privilege.fragment.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.app.AppController;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.models.SearchObj;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by RetailAdmin on 29-05-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = SearchAdapter.class.getSimpleName();

    Context context;
    LayoutInflater layoutInflater;
    List<SearchObj> searchList;

    CallbackFragOpen callbackFragOpen;

    public SearchAdapter (Context context, List<SearchObj> searchList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.searchList = searchList;
        callbackFragOpen = (CallbackFragOpen) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_card, parent, false);
        SearchCard searchCard = new SearchCard(view);

        return searchCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchObj searchObj = searchList.get(position);
        SearchCard searchCard = (SearchCard) holder;
        searchCard.tvName.setText(searchObj.name);
        int offerCount = Integer.parseInt(searchObj.offerCount);
        /*if (offerCount > 1){
            searchCard.tvCount.setText(searchObj.offerCount+" offers");
        }else {
            searchCard.tvCount.setText(searchObj.offerCount+" offer");
        }*/
        String rs = context.getResources().getString(R.string.rs);

        int cost = Integer.valueOf(searchObj.cost);
        if (cost < 500){
            searchCard.tvCount.setText(rs);
        }else if (cost < 999){
            searchCard.tvCount.setText(rs+rs);
        }else {
            searchCard.tvCount.setText(rs+rs+rs);
        }
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }



    private class SearchCard extends RecyclerView.ViewHolder implements View.OnTouchListener {
        LinearLayout searchTab;
        TextView tvName, tvCount;

        public SearchCard(View itemView) {
            super(itemView);
            searchTab = (LinearLayout) itemView.findViewById(R.id.search_tab);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);

            searchTab.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.search_tab:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "tab clicked");
                            if (Integer.parseInt(searchList.get(getAdapterPosition()).outletCount) > 1){
                                Log.d(TAG, "open outlet list");
                                callbackFragOpen.openFrag("selectOutletFrag", searchList.get(getAdapterPosition()).id);
                            }else if (Integer.parseInt(searchList.get(getAdapterPosition()).offerCount) > 1){
                                Log.d(TAG, "open offer list");
                                callbackFragOpen.openFrag("selectOfferFrag", searchList.get(getAdapterPosition()).outletIds);
                            }else {
                                Log.d(TAG, "open details");
                                JSONObject offerOutletId = new JSONObject();
                                try {
                                    offerOutletId.put("offerId", searchList.get(getAdapterPosition()).offerIds);
                                    offerOutletId.put("outletId", searchList.get(getAdapterPosition()).outletIds);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callbackFragOpen.openFrag("offerDetailsFrag", offerOutletId.toString());
                            }

                            AppController.getInstance().restaurantName = searchList.get(getAdapterPosition()).name;
                            AppController.getInstance().rOneLiner = searchList.get(getAdapterPosition()).oneLiner;

                            break;
                    }
                    break;
            }
            return false;
        }
    }
}
