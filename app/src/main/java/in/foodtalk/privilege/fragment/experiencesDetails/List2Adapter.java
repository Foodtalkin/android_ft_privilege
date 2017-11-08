package in.foodtalk.privilege.fragment.experiencesDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 08-11-2017.
 */

public class List2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    JSONArray dataList;
    LayoutInflater layoutInflater;
    public List2Adapter (Context context, JSONArray dataList){
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.expe_list_item, parent, false);
        ListCard listCard = new ListCard(view);
        return listCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListCard listCard = (ListCard) holder;

        try {
            listCard.tvTitle.setText(dataList.getJSONObject(position).getString("title"));
            listCard.tvDes.setText(dataList.getJSONObject(position).getString("data"));

            Log.e("list onBind", dataList.getJSONObject(position).getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataList.length();
    }

    public class ListCard extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDes;
        public ListCard(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
        }
    }
}
