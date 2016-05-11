package com.ye.player.common.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ye.player.R;
import com.ye.player.common.utils.Utils;

public abstract class AbsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener, RecyclerArrayAdapter.OnItemClickListener {
    protected EasyRecyclerView recyclerView;
    protected RecyclerArrayAdapter adapter;
    private boolean alreadyToLastPage = false;
    private int page;
    private boolean isRequesting = false;
    protected View contentView;
    protected View headerView;
    protected View footerView;
    protected View toTopView;
    protected int scrollY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = getRecyclerViewAdapter();
      //  adapter.setNoMore(R.layout.view_nomore);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        adapter.setMore(R.layout.view_more, this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.abs_fragment_list, null);
            recyclerView = (EasyRecyclerView) contentView.findViewById(R.id.rv_products);
            recyclerView.setEmptyView(R.layout.view_nocontent);
           // recyclerView.setRefreshListener(this);
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapterWithProgress(adapter);
            if (getItemDecoration() != null) {
                recyclerView.addItemDecoration(getItemDecoration());
            }

            headerView = getHeaderView(recyclerView);
            if (headerView != null) {
                adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                    @Override
                    public View onCreateView(ViewGroup parent) {
                        if (extraViewsListener != null) {
                            extraViewsListener.onHeaderAdded(headerView);
                        }
                        onHeaderAdded(headerView);
                        return headerView;
                    }

                    @Override
                    public void onBindView(View headerView) {

                    }
                });
            }

            footerView = getFooterView(recyclerView);
            if (footerView != null) {
                adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
                    @Override
                    public View onCreateView(ViewGroup parent) {
                        if (extraViewsListener != null) {
                            extraViewsListener.onFooterAdded(footerView);
                        }
                        onFooterAdded(footerView);
                        return footerView;
                    }

                    @Override
                    public void onBindView(View headerView) {

                    }
                });
            }

            if (recyclerView.getErrorView() != null) {
                recyclerView.getErrorView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.clear();
                        recyclerView.setAdapterWithProgress(adapter);
                        onCLickErrorView(view);
                        loadData();
                    }
                });
            }
            adapter.setOnItemClickListener(this);

            toTopView = contentView.findViewById(R.id.fab_to_top);
            toTopView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerView != null) {
                        scrollY = 0;
                        recyclerView.scrollToPosition(0);
                    }
                }
            });

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    scrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    scrolled(recyclerView, dx, dy);

                }
            });
        }
        setUserVisibleHint(getUserVisibleHint());
        return contentView;
    }

    protected void onHeaderAdded(View headerView) {

    }

    protected void onFooterAdded(View footerView) {

    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    protected abstract RecyclerArrayAdapter getRecyclerViewAdapter();

    protected abstract void loadData();

    protected void onCLickErrorView(View view) {

    }

    public View getHeaderView(View parent) {
        return null;
    }

    public View getFooterView(View parent) {
        return null;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (recyclerView != null && isVisibleToUser) {
                loadData();
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        alreadyToLastPage = false;
        loadData();
        if (getActivity() instanceof SwipeRefreshLayout.OnRefreshListener) {
            ((SwipeRefreshLayout.OnRefreshListener) getActivity()).onRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    protected void scrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    protected void scrolled(RecyclerView recyclerView, int dx, int dy) {
        scrollY += dy;
        if (scrollY > 2 * Utils.getDeviceHeight(getActivity())) {
            setToTopViewVisibility(View.VISIBLE);
        } else {
            setToTopViewVisibility(View.GONE);
        }
    }

    protected void setToTopViewVisibility(int visibility) {
        if (visibility != toTopView.getVisibility()) {
            toTopView.setVisibility(visibility);
        }
    }

    protected boolean isRefreshing() {
        return recyclerView == null ? false : recyclerView.isRefreshing();
    }

    public void setRefreshing(boolean isRefreshing) {
        if (recyclerView != null) {
            recyclerView.setRefreshing(isRefreshing);
        }
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isAlreadyToLastPage() {
        return alreadyToLastPage;
    }

    public void setAlreadyToLastPage(boolean alreadyToLastPage) {
        this.alreadyToLastPage = alreadyToLastPage;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return 16;
    }

    public void initScrollY() {
        scrollY = 0;
    }

    public EasyRecyclerView getRecyclerView() {
        return recyclerView;
    }

    private ExtraViewsListener extraViewsListener;

    public void setExtraViewsListener(ExtraViewsListener extraViewsListener) {
        this.extraViewsListener = extraViewsListener;
    }

    public interface ExtraViewsListener {
        public void onHeaderAdded(View headerView);

        public void onFooterAdded(View footerView);
    }
}