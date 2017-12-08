package in.foodtalk.privilege.fragment.experiences;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.CallbackFragOpen;
import in.foodtalk.privilege.fragment.home.HomeAdapter;
import in.foodtalk.privilege.library.DateFunction;

/**
 * Created by RetailAdmin on 02-11-2017.
 */

public class ExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<JSONObject> expeList;
    CallbackFragOpen callbackFragOpen;

    private final int VIEW_LOADER = 0;
    private final int VIEW_EXPE = 1;
    public ExperienceAdapter(Context context, List<JSONObject> expeList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.expeList = expeList;
        callbackFragOpen = (CallbackFragOpen) context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_LOADER){
            View view = layoutInflater.inflate(R.layout.loader_card, parent, false);
            LoaderCard loaderCard = new LoaderCard(view);
            return loaderCard;
        }else {
            View view = layoutInflater.inflate(R.layout.experience_card, parent, false);
            ExpeCard expeCard = new ExpeCard(view);
            return expeCard;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ExpeCard){
            ExpeCard expeCard = (ExpeCard) holder;
            JSONObject expeObj = expeList.get(position);
            Log.d("expeList obj", expeObj+"");
            try {
                expeCard.tvTitle.setText(expeObj.getString("title"));
                expeCard.tvAddress.setText(expeObj.getString("address"));
                expeCard.tvCost.setText(expeObj.getString("cost")+"/Person");
                expeCard.tvTag.setText(expeObj.getString("tag"));

                //String date = DateFunction.convertFormat(expeObj.getString("start_time"), "yyyy-MM-dd HH:mm:ss", "MMM d 'at' h:mm a");
               // String date1 = DateFunction.convertFormat(expeObj.getString("end_time"), "yyyy-MM-dd HH:mm:ss", "h:mm a");

                //expeCard.tvTime.setText(date+" - "+date1);
                //expeCard.tvTime.setText(expeObj.getString("display_time"));

                String[] time = expeObj.getString("display_time").split("\n");
                if (time.length == 1){
                    expeCard.tvTime.setText(time[0]);
                }else if (time.length == 2){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        expeCard.tvTime.setText(Html.fromHtml(time[0]+"<br/><small><font color='#8c8c8c'>"+time[1]+"</font></small>", Html.FROM_HTML_MODE_COMPACT));
                    }else {
                        expeCard.tvTime.setText(Html.fromHtml(time[0]+"<br/><small><font color='#8c8c8c'>"+time[1]+"</font></small>"));
                    }
                    // expeCard.tvTime.setText(time[1]);
                }else if (time.length == 0){
                    expeCard.tvTime.setText("");
                }


                if (expeObj.getString("avilable_seats").equals("0")){
                    expeCard.btnDetails.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_red));
                    //expeCard.btnDetails.setClickable(false);
                    expeCard.tvBtnDetails.setText(" Sold Out ");
                }else {
                    expeCard.btnDetails.setBackground(context.getResources().getDrawable(R.drawable.btn_bg5));
                    //expeCard.btnDetails.setClickable(false);
                    expeCard.tvBtnDetails.setText("View Details");
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
        }else if (holder instanceof LoaderCard){

        }

    }
    @Override
    public int getItemCount() {
        return expeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (expeList.get(position) == null){
            return VIEW_LOADER;
        }else {
            return VIEW_EXPE;
        }
    }

    class LoaderCard extends RecyclerView.ViewHolder {
        RelativeLayout loaderView;
        public LoaderCard(View itemView) {
            super(itemView);
            loaderView = (RelativeLayout) itemView.findViewById(R.id.loader_view);
        }
    }

    private class ExpeCard extends RecyclerView.ViewHolder implements View.OnTouchListener{
        ImageView imgView;
        TextView tvTitle, tvAddress, tvTime, tvCost, tvBtnDetails, tvTag;
        LinearLayout btnDetails;


        public ExpeCard(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
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
