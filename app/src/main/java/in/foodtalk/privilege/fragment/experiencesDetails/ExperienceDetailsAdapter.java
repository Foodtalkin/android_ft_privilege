package in.foodtalk.privilege.fragment.experiencesDetails;

import android.content.Context;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.library.DateFunction;

/**
 * Created by RetailAdmin on 06-11-2017.
 */

public class ExperienceDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    JSONObject response;
    JSONArray dataList;

    ValueCallback valueCallback;

    private final int VIEW_COVER = 0;
    private final int VIEW_DES = 1;
    private final int VIEW_LIST1 = 2;
    private final int VIEW_LIST2 = 3;
    private final int VIEW_IMG = 4;
    private final int VIEW_VIDEO = 5;
    private final int VIEW_URL = 6;

    CallbackFragOpen callbackFragOpen;

    public ExperienceDetailsAdapter (Context context, JSONObject response, ValueCallback valueCallback){
        this.context = context;
        this.response = response;
        this.valueCallback = valueCallback;
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
            Log.d("viewType", "VIEW_COVER");
            return coverCard;
        }else if (viewType == VIEW_DES){
            view = layoutInflater.inflate(R.layout.expe_description_card, parent, false);
            descriptionCard = new DescriptionCard(view);
            Log.d("viewType", "VIEW_DES");
            return descriptionCard;
        }else if (viewType == VIEW_LIST1){
            view = layoutInflater.inflate(R.layout.expe_list1_card, parent, false);
            list1Card = new List1Card(view);
            Log.d("viewType", "VIEW_LIST1");
            return list1Card;
        }else if (viewType == VIEW_LIST2){
            view = layoutInflater.inflate(R.layout.expe_list2_card, parent, false);
            list2Card = new List2Card(view);
            Log.d("viewType", "VIEW_LIST2");
            return  list2Card;
        }else if (viewType == VIEW_IMG){
            view = layoutInflater.inflate(R.layout.expe_img_card, parent, false);
            imageCard = new ImageCard(view);
            Log.d("viewType", "VIEW_IMG");
            return imageCard;
        }else if (viewType == VIEW_VIDEO){
            view = layoutInflater.inflate(R.layout.expe_video_card, parent, false);
            videoCard = new VideoCard(view);
            Log.d("viewType", "VIEW_VIDEO");
            return videoCard;
        }else if (viewType == VIEW_URL){
            view = layoutInflater.inflate(R.layout.expe_url_card, parent, false);
            urlCard = new UrlCard(view);
            Log.d("viewType", "VIEW_URL");
            return urlCard;
        }else {
            view = layoutInflater.inflate(R.layout.ignore_card, parent, false);
            IgnoreCard ignoreCard = new IgnoreCard(view);
            return ignoreCard;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CoverCard){
                CoverCard coverCard = (CoverCard) holder;

                try {
                    coverCard.tvTitle.setText(response.getJSONObject("result").getString("title"));
                    coverCard.tvAddress.setText(response.getJSONObject("result").getString("address"));

                    //String date = DateFunction.convertFormat(response.getJSONObject("result").getString("start_time"), "yyyy-MM-dd HH:mm:ss", "MMM d 'at' h:mm a");
                    //String date1 = DateFunction.convertFormat(response.getJSONObject("result").getString("end_time"), "yyyy-MM-dd HH:mm:ss", "h:mm a");
                    //coverCard.tvTime.setText(date+" - "+date1);
                    //coverCard.tvTime.setText(response.getJSONObject("result").getString("display_time"));

                    String[] time = response.getJSONObject("result").getString("display_time").split("\n");
                    if (time.length == 1){
                        coverCard.tvTime.setText(time[0]);
                    }else if (time.length == 2){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            coverCard.tvTime.setText(Html.fromHtml(time[0]+"<br/><small><font color='#8c8c8c'>"+time[1]+"</font></small>", Html.FROM_HTML_MODE_COMPACT));
                        }else {
                            coverCard.tvTime.setText(Html.fromHtml(time[0]+"<br/><small><font color='#8c8c8c'>"+time[1]+"</font></small>"));
                        }
                        // expeCard.tvTime.setText(time[1]);
                    }else if (time.length == 0){
                        coverCard.tvTime.setText("");
                    }
                    //coverCard.tvAddress1.setText(response.getJSONObject("result").getString(""));

                    Picasso.with(context)
                            .load(response.getJSONObject("result").getString("cover_image"))
                            //.fit()
                            .placeholder(R.drawable.ic_placeholder)
                            //.fit().centerCrop()
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
                String videoId = uri.getQueryParameter("v");  //will return "V-Maths-Addition"

                videoCard.videoId1 = videoId;
                Log.e("videoCard","videoId: "+ videoId);

                String summary = "<html><body><iframe width=\"100%\" height=\"200\" src=\"https://www.youtube.com/embed/"+videoId+"?rel=0\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                videoCard.webView.loadData(summary, "text/html", null);
            }
            if (holder instanceof List2Card){


                List2Card list2Card = (List2Card) holder;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);


                list2Card.recyclerView.setLayoutManager(layoutManager);
                list2Card.recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //view.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
                try {
                    list2Card.tvTitle.setText(dataList.getJSONObject(position-1).getString("title"));
                    List2Adapter list2Adapter = new List2Adapter(context, dataList.getJSONObject(position-1).getJSONArray("content"));
                    list2Card.recyclerView.setAdapter(list2Adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (holder instanceof ImageCard){
                ImageCard imageCard = (ImageCard) holder;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                imageCard.recyclerView.setLayoutManager(layoutManager);
                try {
                    ExpeImageAdapter expeImageAdapter = new ExpeImageAdapter(context,dataList.getJSONObject(position-1).getJSONArray("content"), valueCallback);
                    imageCard.recyclerView.setAdapter(expeImageAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    return 45;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return 45;
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
        RecyclerView recyclerView;
        TextView tvTitle;
        public List2Card(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        }
    }
    private class ImageCard extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;

        public ImageCard(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
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

    private class IgnoreCard extends RecyclerView.ViewHolder{

        public IgnoreCard(View itemView) {
            super(itemView);
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
