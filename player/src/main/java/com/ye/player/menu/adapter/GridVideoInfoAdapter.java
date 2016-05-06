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
import com.ye.player.R;
import com.ye.player.common.bean.VideoInfo;

import java.util.List;

public class GridVideoInfoAdapter extends RecyclerArrayAdapter<VideoInfo> {
    public GridVideoInfoAdapter(Context context) {
        super(context);
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
        TextView titleTextView;

        public ProductViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            titleTextView = (TextView) itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void setData(final VideoInfo videoInfo) {
            if (videoInfo == null) {
                return;
            }

            // DataCollectUtils.saveItemAction(DataCollectConstants.ItemType.MALL_PRODUCT_LIST_ITEM,
            // DataCollectConstants.ItemActionType.ITEM_EXPOSURE,
            // merchandiseSummaryRecord.getId(), null);

            titleTextView.setText(videoInfo.getTitle());

        }
    }
}
