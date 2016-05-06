package com.ye.player.menu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.divider.DividerGridItemDecoration;
import com.ye.player.R;
import com.ye.player.common.bean.VideoInfo;
import com.ye.player.common.service.VideoInfoService;
import com.ye.player.common.ui.fragment.AbsListFragment;
import com.ye.player.menu.adapter.GridVideoInfoAdapter;

import java.util.List;

public class MenuFragment extends AbsListFragment{
    private VideoInfoService videoInfoService;
    private List<VideoInfo> list;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        videoInfoService = new VideoInfoService(getContext());
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));
        return gridLayoutManager;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerGridItemDecoration(getContext());
    }

    @Override
    protected RecyclerArrayAdapter getRecyclerViewAdapter() {
        return new GridVideoInfoAdapter(getContext());
    }

    @Override
    protected void loadData() {
        list = videoInfoService.getVideoInfo();
        adapter.addAll(list);
        adapter.stopMore();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();;
    }

    @Override
    public boolean hasNavigationBar() {
        return false;
    }

    @Override
    public boolean hasLeftBarButton() {
        return false;
    }

}
