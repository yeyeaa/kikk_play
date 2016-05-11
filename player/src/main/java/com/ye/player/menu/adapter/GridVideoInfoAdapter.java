package com.ye.player.menu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
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
import com.ye.player.common.utils.FileUtils;
import com.ye.player.common.utils.TimeUtils;

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
        return new VideoViewHolder(parent, R.layout.video_info_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    class VideoViewHolder extends BaseViewHolder<VideoInfo> {
        TextView titleTextView, durationTextView, sizeTextView ;
        ImageView thumbImageView;

        public VideoViewHolder(ViewGroup parent, @LayoutRes int res) {
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
            String duration = TimeUtils.longToString(videoInfo.getDuration());
            durationTextView.setText(String.valueOf(duration));
            String size = FileUtils.convertSize(videoInfo.getSize());
            sizeTextView.setText(String.valueOf(size));

          /*  Bitmap bitmap = BitmapFactory.decodeFile(videoInfo.getThumbPath());

            thumbImageView.setImageBitmap(bitmap);*/


            //PicassoImageLoader.loadImage(videoInfo.getThumbPath(), thumbImageView,R.drawable.error);
            File file;
            if (videoInfo.getThumbPath() != null){
                file = new File(videoInfo.getThumbPath());
            } else {
                file = null;
            }

           // File file = new File("/storage/emulated/0/1.jpg");
          //  File file = new File("/storage/emulated/0/DCIM/.thumbnails/1459302001691.jpg");
           /* if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/
            if (file !=null&&file.exists()){
                PicassoImageLoader.loadImage(file, thumbImageView, videoInfo.getPath());
            } else{
                //这里要改，会oom
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoInfo.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
                thumbImageView.setImageBitmap(bitmap);
            }

          //  Picasso.with(context).load(file).into(thumbImageView);

        }
    }
}
