package in.foodtalk.privilege.fragment.experiences;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.library.DateFunction;

/**
 * Created by RetailAdmin on 02-11-2017.
 */

public class ExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<JSONObject> expeList;
    CallbackFragOpen callbackFragOpen;
    public ExperienceAdapter(Context context, List<JSONObject> expeList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.expeList = expeList;
        callbackFragOpen = (CallbackFragOpen) context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.experience_card, parent, false);
        ExpeCard expeCard = new ExpeCard(view);
        return expeCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpeCard expeCard = (ExpeCard) holder;
        JSONObject expeObj = expeList.get(position);
        try {
            expeCard.tvTitle.setText(expeObj.getString("title"));
            expeCard.tvAddress.setText(expeObj.getString("address"));
            expeCard.tvCost.setText(expeObj.getString("cost")+"/Person");

            String date = DateFunction.convertFormat(expeObj.getString("start_time"), "yyyy-MM-dd HH:mm:ss", "MMM d 'at' h:mm a");
            String date1 = DateFunction.convertFormat(expeObj.getString("end_time"), "yyyy-MM-dd HH:mm:ss", "h:mm a");

            expeCard.tvTime.setText(date+" - "+date1);

            if (expeObj.getString("avilable_seats").equals("0")){
                expeCard.btnDetails.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_red));
                //expeCard.btnDetails.setClickable(false);
                expeCard.tvBtnDetails.setText(" Sold Out ");
            }

            Picasso.with(context)
                    .load(expeObj.getString("cover_image"))
                    //.fit()
                    .placeholder(R.drawable.ic_placeholder)
                    //.fit().centerCrop()
                    .into(expeCard.imgView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return expeList.size();
    }

    private class ExpeCard extends RecyclerView.ViewHolder implements View.OnTouchListener{
        ImageView imgView;
        TextView tvTitle, tvAddress, tvTime, tvCost, tvBtnDetails;
        LinearLayout btnDetails;


        public ExpeCard(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvCost = (TextView) itemView.findViewById(R.id.tv_cost);
            tvBtnDetails  = (TextView) itemView.findViewById(R.id.tv_btn_details);
            btnDetails = (LinearLayout) itemView.findViewById(R.id.btn_details);
            btnDetails.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_details:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            try {
                                callbackFragOpen.openFrag("ExperienceDetailsFrag",expeList.get(getAdapterPosition()).getString("id"));
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
