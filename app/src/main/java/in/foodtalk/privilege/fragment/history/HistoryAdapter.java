package in.foodtalk.privilege.fragment.history;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.library.DateFunction;
import in.foodtalk.privilege.models.HistoryObj;

/**
 * Created by RetailAdmin on 26-05-2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<HistoryObj> historyList;

    public HistoryAdapter (Context context, List<HistoryObj> historyList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.historyList = historyList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.history_card, parent, false);
        HistoryCard historyCard = new HistoryCard(view);
        return historyCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryCard historyCard = (HistoryCard) holder;
        HistoryObj historyObj = historyList.get(position);

        historyCard.tvTitle.setText(historyObj.name);
        historyCard.tvRid.setText("RID "+historyObj.offerRedeemed);
        historyCard.tvCoupons.setText("Coupons used: "+historyObj.offerRedeemed);
        historyCard.tvTime.setText(DateFunction.convertFormat(historyObj.createdAt, "yyyy-MM-dd HH:mm:ss",  "EEE, d MMM yyyy '-' hh:mm aaa"));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    private class HistoryCard extends RecyclerView.ViewHolder implements View.OnTouchListener{

        TextView tvTitle, tvCoupons, tvRid, tvTime;

        Typeface typefaceFutura = Typeface.createFromAsset(context.getAssets(), "fonts/futura_bold.otf");


        public HistoryCard(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCoupons = (TextView) itemView.findViewById(R.id.tv_coupons);
            tvRid = (TextView) itemView.findViewById(R.id.tv_rid);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            tvTitle.setTypeface(typefaceFutura);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }
}
