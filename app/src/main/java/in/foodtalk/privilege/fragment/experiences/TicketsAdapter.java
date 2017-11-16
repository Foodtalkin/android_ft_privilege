package in.foodtalk.privilege.fragment.experiences;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 16-11-2017.
 */

public class TicketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    JSONArray listData;
    LayoutInflater layoutInflater;
    public TicketsAdapter (Context context, JSONArray listData){
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = layoutInflater.inflate(R.layout.tickte_card, parent, false);
        TicketCard ticketCard = new TicketCard(layout);
        return ticketCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TicketCard ticketCard = (TicketCard) holder;
        try {
            ticketCard.tvTitle.setText(listData.getJSONObject(position).getString("title"));
            ticketCard.tvTime.setText(listData.getJSONObject(position).getString("start_time"));
            ticketCard.tvAddress.setText(listData.getJSONObject(position).getString("address"));
            ticketCard.tvTickets.setText(listData.getJSONObject(position).getString("total_tickets"));
            ticketCard.tvTraId.setText(listData.getJSONObject(position).getString("txn_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listData.length();
    }

    private class TicketCard extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvAddress, tvTickets, tvTraId;
        public TicketCard(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvTickets = (TextView) itemView.findViewById(R.id.tv_ticktes);
            tvTraId = (TextView) itemView.findViewById(R.id.tv_tra_id);
        }
    }
}
