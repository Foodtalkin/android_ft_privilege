package in.foodtalk.privilege.fragment.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.ValueCallback;

/**
 * Created by RetailAdmin on 30-05-2017.
 */

public class SearchFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JSONArray cuisineList;
    Context context;
    LayoutInflater layoutInflater;

    String TAG = SearchFilterAdapter.class.getSimpleName();

    ValueCallback valueCallback;

    public SearchFilterAdapter (Context context, ValueCallback valueCallback, JSONArray cuisineList){
        this.context = context;
        this.cuisineList  = cuisineList;
        layoutInflater = LayoutInflater.from(context);
        this.valueCallback = valueCallback;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.filter_card, parent, false);
        FilterCard filterCard = new FilterCard(view);
        return filterCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FilterCard filterCard = (FilterCard) holder;
        try {
            String text = cuisineList.getJSONObject(position).getString("title");
            filterCard.tvFilter.setText(text);
            filterCard.id = cuisineList.getJSONObject(position).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cuisineList.length();
    }

    class FilterCard extends RecyclerView.ViewHolder implements View.OnTouchListener{
        LinearLayout btnFilter;
        TextView tvFilter;
        View filterCircle;
        String id;
        Boolean selected = false;
        public FilterCard(View itemView) {
            super(itemView);
            btnFilter = (LinearLayout) itemView.findViewById(R.id.btn_filter);
            tvFilter = (TextView) itemView.findViewById(R.id.tv_filter);
            filterCircle = itemView.findViewById(R.id.filter_circle);
            btnFilter.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_filter:
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG,"btn filter clicked");
                            if (selected == false){
                                filterCircle.setBackgroundResource(R.drawable.circle_selected);
                                valueCallback.setValue("add", id);
                                selected = true;
                            }else {
                                filterCircle.setBackgroundResource(R.drawable.circle_select);
                                valueCallback.setValue("remove", id);
                                selected = false;
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    }
}
