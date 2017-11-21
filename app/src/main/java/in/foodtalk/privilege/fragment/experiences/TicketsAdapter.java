package in.foodtalk.privilege.fragment.experiences;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.DateFunction;

/**
 * Created by RetailAdmin on 16-11-2017.
 */

public class TicketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    JSONArray listData;
    LayoutInflater layoutInflater;
    CallbackFragOpen callbackFragOpen;
    public TicketsAdapter (Context context, JSONArray listData){
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        callbackFragOpen = (CallbackFragOpen) context;
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
            //ticketCard.tvTime.setText(listData.getJSONObject(position).getString("start_time"));

            String date = DateFunction.convertFormat(listData.getJSONObject(position).getString("start_time"), "yyyy-MM-dd HH:mm:ss", "MMM d 'at' h:mm a");
            String date1 = DateFunction.convertFormat(listData.getJSONObject(position).getString("end_time"), "yyyy-MM-dd HH:mm:ss", "h:mm a");
            ticketCard.tvTime.setText(date+" - "+date1);


            ticketCard.tvAddress.setText(listData.getJSONObject(position).getString("address"));
            ticketCard.tvTickets.setText(listData.getJSONObject(position).getString("total_tickets")+" Tickets");
            ticketCard.tvTraId.setText(listData.getJSONObject(position).getString("txn_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listData.length();
    }

    private class TicketCard extends RecyclerView.ViewHolder implements View.OnTouchListener{
        TextView tvTitle, tvTime, tvAddress, tvTickets, tvTraId, btnEvent;
        public TicketCard(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvTickets = (TextView) itemView.findViewById(R.id.tv_ticktes);
            tvTraId = (TextView) itemView.findViewById(R.id.tv_tra_id);
            btnEvent = (TextView) itemView.findViewById(R.id.btn_event);
            btnEvent.setOnTouchListener(this);
        }
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_event:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            Log.d("event btn","clicked "+getAdapterPosition());
                            try {
                                callbackFragOpen.openFrag("ExperienceDetailsFrag", listData.getJSONObject(getAdapterPosition()).getString("exp_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    }
}