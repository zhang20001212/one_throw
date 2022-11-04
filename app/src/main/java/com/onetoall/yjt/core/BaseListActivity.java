package com.onetoall.yjt.core;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.onetoall.yjt.R;
import com.qw.framework.widget.pull.BaseListAdapter;
import com.qw.framework.widget.pull.BaseViewHolder;
import com.qw.framework.widget.pull.FooterStateView;
import com.qw.framework.widget.pull.IFooterState;
import com.qw.framework.widget.pull.PullRecyclerView;

import java.util.ArrayList;

public abstract class BaseListActivity<T> extends BaseActivity implements PullRecyclerView.OnPullRecyclerViewListener, FooterStateView.OnFooterViewListener {
    protected PullRecyclerView mPullRecyclerView;
    protected ListAdapter adapter;
    protected ArrayList<T> modules = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.base_activity_list, true);
    }

    @Override
    protected void initView() {
        mPullRecyclerView = (PullRecyclerView) findViewById(R.id.mPullRecyclerView);
        mPullRecyclerView.setOnPullRecyclerViewListener(this);
        mPullRecyclerView.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        adapter = new ListAdapter(modules);
        mPullRecyclerView.setLayoutManager(getLayoutManager());
        mPullRecyclerView.setItemAnimator(getItemAnimator());
        mPullRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh(int mode) {

    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return new DefaultItemAnimator();
    }

    @Override
    public void onLoadMoreRetry() {

    }


    public class ListAdapter extends BaseListAdapter<T> {

        public ListAdapter(ArrayList<T> modules) {
            super(modules);
        }

        @Override
        protected View onCreateHeaderView(ViewGroup parent) {
            return BaseListActivity.this.onCreateHeaderView(parent);
        }

        /**
         * @param parent
         * @return view  this view must impl IFooterState interface @{@link IFooterState }  because of framework will call IFooterState onLoadMoreStateChanged(int state) changed load more state
         */
        @Override
        protected View onCreateFooterView(ViewGroup parent) {
            return BaseListActivity.this.onCreateFooterView(parent);
        }

        @Override
        protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
            return BaseListActivity.this.onCreateItemView(parent, viewType);
        }

        @Override
        protected int getItemViewTypeWithPosition(int position) {
            return BaseListActivity.this.getItemViewTypeWithPosition(position);
        }
    }

    protected View onCreateFooterView(ViewGroup parent) {
        FooterStateView footerView = new FooterStateView(this);
        footerView.setOnFooterViewListener(this);
        ViewGroup.LayoutParams layoutParams = null;
//        这里有个坑 设置瀑布流StaggeredGridLayoutManager布局 第一次footer不独占一行
//        debug后发现footerView的layoutParams类型不是StaggeredGridLayoutManager.LayoutParams类型
//        很奇怪加载更多后footerView的layoutParams类型又变为StaggeredGridLayoutManager.LayoutParams类型l
//        下面是一个解决方案 大家要是有更好的方案可以联系我 qq:435231045
        if (mPullRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            layoutParams = mPullRecyclerView.getLayoutManager().generateDefaultLayoutParams();
        } else {
            layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        }
        footerView.setLayoutParams(layoutParams);
        return footerView;
    }

    protected View onCreateHeaderView(ViewGroup parent) {
        return null;
    }

    protected int getItemViewTypeWithPosition(int position) {
        return 0;
    }

    protected abstract BaseViewHolder onCreateItemView(ViewGroup parent, int viewType);

}
