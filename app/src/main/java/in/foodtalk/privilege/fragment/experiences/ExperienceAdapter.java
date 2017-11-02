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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 02-11-2017.
 */

public class ExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<JSONObject> expeList;
    public ExperienceAdapter(Context context, List<JSONObject> expeList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.expeList = expeList;
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
        TextView tvTitle, tvAddress, tvTime, tvCost;
        LinearLayout btnDetails;

        public ExpeCard(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }
}
