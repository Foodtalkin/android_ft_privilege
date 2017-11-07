package in.foodtalk.privilege.fragment.experiencesDetails;

import android.content.Context;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.app.Url;
import in.foodtalk.privilege.comm.CallbackFragOpen;

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

    CallbackFragOpen callbackFragOpen;

    public ExperienceDetailsAdapter (Context context, JSONObject response){
        this.context = context;
        this.response = response;
        try {
            this.dataList = response.getJSONObject("result").getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layoutInflater = LayoutInflater.from(context);
        callbackFragOpen = (CallbackFragOpen) context;
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
        if (holder instanceof CoverCard){
            CoverCard coverCard = (CoverCard) holder;
            try {
                coverCard.tvTitle.setText(response.getJSONObject("result").getString("title"));
                coverCard.tvAddress.setText(response.getJSONObject("result").getString("address"));
                //coverCard.tvAddress1.setText(response.getJSONObject("result").getString(""));

                Picasso.with(context)
                        .load(response.getJSONObject("result").getString("cover_image"))
                        //.fit()
                        .placeholder(R.drawable.ic_placeholder)
                        .fit().centerCrop()
                        .into(coverCard.imgView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (holder instanceof DescriptionCard){
            DescriptionCard descriptionCard = (DescriptionCard) holder;
            try {
                descriptionCard.tvTitle.setText(dataList.getJSONObject(position-1).getString("title"));
                descriptionCard.tvDes.setText(dataList.getJSONObject(position-1).getString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (holder instanceof List1Card){
            List1Card list1Card = (List1Card) holder;
            try {
                list1Card.tvTitle.setText(dataList.getJSONObject(position-1).getString("title"));
                JSONArray list = dataList.getJSONObject(position-1).getJSONArray("content");
                String listStr = "- ";
                for (int i = 0; i < list.length(); i++){
                    if (list.length() > i+1){
                        listStr = listStr+list.getString(i)+"\n- ";
                    }else {
                        listStr = listStr+list.getString(i);
                    }
                }
                list1Card.tvList.setText(listStr);
                Log.e("List1Card", listStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (holder instanceof UrlCard){
            UrlCard urlCard = (UrlCard) holder;
            try {
                urlCard.tvLink.setText(dataList.getJSONObject(position-1).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (holder instanceof VideoCard){
            VideoCard videoCard = (VideoCard) holder;
            Uri uri = null;
            try {
                uri = Uri.parse(dataList.getJSONObject(position-1).getString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String videoId = uri.getQueryParameter("v");  //will return "V-Maths-Addition "

            videoCard.videoId1 = videoId;
            Log.e("videoCard","videoId: "+ videoId);

            String summary = "<html><body><iframe width=\"100%\" height=\"200\" src=\"https://www.youtube.com/embed/"+videoId+"?rel=0\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            videoCard.webView.loadData(summary, "text/html", null);
        }
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
        TextView tvTitle, tvDes;
        public DescriptionCard(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
        }
    }
    private class List1Card extends RecyclerView.ViewHolder{
        TextView tvTitle, tvList;
        public List1Card(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvList = (TextView) itemView.findViewById(R.id.tv_list);
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
        WebView webView;
        String videoId1;
        public VideoCard(View itemView) {
            super(itemView);

            Log.e("InVideoCard", "videoId1: "+ videoId1);

            //Log.d("html video code", summary);
            webView = (WebView) itemView.findViewById(R.id.webview);
            webView.setWebViewClient(new CustomWebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);

            webView.getSettings().setAppCacheEnabled(false);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
    }
    private class UrlCard extends RecyclerView.ViewHolder {
        TextView tvLink;
        public UrlCard(View itemView) {
            super(itemView);
            tvLink = (TextView) itemView.findViewById(R.id.tv_link);
            tvLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        callbackFragOpen.openFrag("webViewFrag",dataList.getJSONObject(getAdapterPosition()-1).getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
