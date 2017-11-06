package in.foodtalk.privilege.fragment.experiencesDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.foodtalk.privilege.R;

/**
 * Created by RetailAdmin on 06-11-2017.
 */

public class ExperienceDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    JSONObject response;
    JSONArray dataList;

    private final int VIEW_COVER = 0;
    private final int VIEW_DES = 1;
    private final int VIEW_LIST1 = 2;
    private final int VIEW_LIST2 = 3;
    private final int VIEW_IMG = 4;
    private final int VIEW_VIDEO = 5;
    private final int VIEW_URL = 6;

    public ExperienceDetailsAdapter (Context context, JSONObject response){
        this.context = context;
        this.response = response;
        try {
            this.dataList = response.getJSONObject("result").getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CoverCard coverCard;
        DescriptionCard descriptionCard;
        List1Card list1Card;
        List2Card list2Card;
        VideoCard videoCard;
        UrlCard urlCard;
        ImageCard imageCard;
        View view;
        if (viewType == VIEW_COVER){
            view = layoutInflater.inflate(R.layout.expe_cover_card, parent, false);
            coverCard = new CoverCard(view);
            return coverCard;
        }else if (viewType == VIEW_DES){
            view = layoutInflater.inflate(R.layout.expe_description_card, parent, false);
            descriptionCard = new DescriptionCard(view);
            return descriptionCard;
        }else if (viewType == VIEW_LIST1){
            view = layoutInflater.inflate(R.layout.expe_list1_card, parent, false);
            list1Card = new List1Card(view);
            return list1Card;
        }else if (viewType == VIEW_LIST2){
            view = layoutInflater.inflate(R.layout.expe_list2_card, parent, false);
            list2Card = new List2Card(view);
            return  list2Card;
        }else if (viewType == VIEW_IMG){
            view = layoutInflater.inflate(R.layout.expe_img_card, parent, false);
            imageCard = new ImageCard(view);
            return imageCard;
        }else if (viewType == VIEW_VIDEO){
            view = layoutInflater.inflate(R.layout.expe_video_card, parent, false);
            videoCard = new VideoCard(view);
            return videoCard;
        }else if (viewType == VIEW_URL){
            view = layoutInflater.inflate(R.layout.expe_url_card, parent, false);
            urlCard = new UrlCard(view);
            return urlCard;
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return VIEW_COVER;
        }else {
            int i = position-1;
            try {
                if (dataList.getJSONObject(i).getString("type").equals("TEXT")){
                    return VIEW_DES;
                }else if (dataList.getJSONObject(i).getString("type").equals("LIST1")){
                    return VIEW_LIST1;
                }else if (dataList.getJSONObject(i).getString("type").equals("LIST2")){
                    return VIEW_LIST2;
                }else if (dataList.getJSONObject(i).getString("type").equals("IMAGE")){
                    return VIEW_IMG;
                }else if (dataList.getJSONObject(i).getString("type").equals("VIDEO")){
                    return VIEW_VIDEO;
                }else if (dataList.getJSONObject(i).getString("type").equals("URL")){
                    return VIEW_URL;
                }else {
                    return super.getItemViewType(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return super.getItemViewType(position);
            }
        }
       // return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
       /* try {
            return response.getJSONObject("result").getJSONArray("data").length()+1;

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }*/
        return dataList.length()+1;
    }

    private class CoverCard extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView tvTitle, tvTime, tvTime1, tvAddress, tvAddress1;
        public CoverCard(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvTime1 = (TextView) itemView.findViewById(R.id.tv_time1);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvAddress1 = (TextView) itemView.findViewById(R.id.tv_address1);
        }
    }
    private class DescriptionCard extends RecyclerView.ViewHolder{
        public DescriptionCard(View itemView) {
            super(itemView);
        }
    }
    private class List1Card extends RecyclerView.ViewHolder{

        public List1Card(View itemView) {
            super(itemView);
        }
    }
    private class List2Card extends RecyclerView.ViewHolder{

        public List2Card(View itemView) {
            super(itemView);
        }
    }
    private class ImageCard extends RecyclerView.ViewHolder{

        public ImageCard(View itemView) {
            super(itemView);
        }
    }
    private class VideoCard extends RecyclerView.ViewHolder{

        public VideoCard(View itemView) {
            super(itemView);
        }
    }
    private class UrlCard extends RecyclerView.ViewHolder{

        public UrlCard(View itemView) {
            super(itemView);
        }
    }
}
