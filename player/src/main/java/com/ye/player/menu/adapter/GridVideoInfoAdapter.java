package com.ye.player.menu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.picasso.Picasso;
import com.ye.player.R;
import com.ye.player.common.PicassoImageLoader;
import com.ye.player.common.bean.VideoInfo;

import java.io.File;
import java.util.List;

public class GridVideoInfoAdapter extends RecyclerArrayAdapter<VideoInfo> {
    private Context context;
    public GridVideoInfoAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(parent, R.layout.video_info_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    class ProductViewHolder extends BaseViewHolder<VideoInfo> {
        TextView titleTextView, durationTextView, sizeTextView ;
        ImageView thumbImageView;

        public ProductViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            durationTextView = (TextView) itemView.findViewById(R.id.duration);
            sizeTextView = (TextView) itemView.findViewById(R.id.size);
            thumbImageView = (ImageView) itemView.findViewById(R.id.thumb);
        }

        @Override
        public void setData(final VideoInfo videoInfo) {
            if (videoInfo == null) {
                return;
            }

            if (videoInfo.getTitle()!=null){
                titleTextView.setText(videoInfo.getTitle());
            } else {
                titleTextView.setText("");
            }
            long duration = videoInfo.getDuration();
            durationTextView.setText(String.valueOf(duration));
            long size = videoInfo.getSize();
            sizeTextView.setText(String.valueOf(size));
          //  PicassoImageLoader.loadImage(videoInfo.getThumbPath(), thumbImageView);
            Picasso.with(context).load(new File(videoInfo.getThumbPath())).into(thumbImageView);

        }
    }
}
