package in.foodtalk.privilege.fragment.experiencesDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.ValueCallback;

/**
 * Created by RetailAdmin on 09-11-2017.
 */

public class ExpeImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    JSONArray dataList;
    ValueCallback valueCallback;
    public ExpeImageAdapter (Context context, JSONArray dataList, ValueCallback valueCallback){
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
        this.valueCallback = valueCallback;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.expe_image_card, parent, false);
        ImageCard imageCard = new ImageCard(view);
        return imageCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageCard imageCard = (ImageCard) holder;
        //ImagesObj imagesObj = imagesList.get(position);
        try {
            Picasso.with(context)
                    .load(dataList.getString(position))
                    .fit().centerCrop()
                    //.fit()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imageCard.imgView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return dataList.length();
    }
    private class ImageCard extends RecyclerView.ViewHolder{

        ImageView imgView;

        public ImageCard(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);
            //imgView.setOnTouchListener(this);

            imgView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //view.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    //Log.e("ontouch","hogya lick");
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_UP:
                            switch (view.getId()){
                                case R.id.img_view:
                                    Log.e("bigImageAdapter","img clicked p: "+ getAdapterPosition());
                                    valueCallback.setValue(dataList.toString(),Integer.toString(getAdapterPosition()));
                                    break;
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }
}
