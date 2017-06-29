package in.foodtalk.privilege.fragment.OfferDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.foodtalk.privilege.R;
import in.foodtalk.privilege.comm.ValueCallback;
import in.foodtalk.privilege.models.ImagesObj;

/**
 * Created by RetailAdmin on 17-05-2017.
 */

public class BigImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ImagesObj> imagesList;
    Context context;
    LayoutInflater layoutInflater;
    ValueCallback valueCallback;

    public BigImagesAdapter(Context context, List<ImagesObj> imagesList, ValueCallback valueCallback){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.imagesList = imagesList;

        this.valueCallback = valueCallback;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.big_image_card, parent, false);
        ImageCard imageCard = new ImageCard(view);
        return imageCard;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageCard imageCard = (ImageCard) holder;
        ImagesObj imagesObj = imagesList.get(position);
        Picasso.with(context)
                .load(imagesObj.url)
                .fit().centerCrop()
                //.fit()
                .placeholder(R.drawable.ic_placeholder)
                .into(imageCard.imgView);



        //imageCard.imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    private class ImageCard extends RecyclerView.ViewHolder implements View.OnTouchListener{

        ImageView imgView, btnClose;

        public ImageCard(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_view);


            //imgView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            return false;
        }
    }
}
