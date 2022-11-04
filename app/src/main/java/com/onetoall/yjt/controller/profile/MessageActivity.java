package com.onetoall.yjt.controller.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.store.CustomerManagerActivity;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.core.BaseListActivity;
import com.onetoall.yjt.domain.MessageBean;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.MessageModel;
import com.onetoall.yjt.widget.LoadingView;
import com.onetoall.yjt.widget.RefreshView;
import com.qw.framework.widget.pull.BaseViewHolder;
import com.qw.framework.widget.pull.IFooterState;
import com.qw.framework.widget.pull.PullRecyclerView;

import java.util.ArrayList;

/**
 * Created by shaomengjie on 2016/12/6.
 */

public class MessageActivity extends BaseListActivity<MessageBean> {
    private MessageModel messageModel;
    private int page = 0 ;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message,true);
    }

    @Override
    protected void initView() {
        super.initView();
        mPullRecyclerView.setEnablePullToEnd(true);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("消息");
        messageModel = new MessageModel(this);
        loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
    }

    private void loadDataFromServer(final int modePullToStart) {
        messageModel.loadMessage(page+"", MyApplication.getInstance().getTel(), new Callback<ArrayList<MessageBean>>() {

            @Override
            public void onSuccess(ArrayList<MessageBean> data) {
                if (modePullToStart == PullRecyclerView.MODE_PULL_TO_START) modules.clear();
                if (data.size() < 10) {
                    mPullRecyclerView.onRefreshCompleted(modePullToStart, IFooterState.LOAD_MORE_STATE_NO_DATA);
                } else {
                    mPullRecyclerView.onRefreshCompleted(modePullToStart, IFooterState.LOAD_MORE_STATE_IDLE);
                }
                modules.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });

    }

    @Override
    public void onRefresh(int mode) {
        super.onRefresh(mode);
        if (mode == PullRecyclerView.MODE_PULL_TO_START){
            page = 0;
        }else {
            page = 1;
        }
        loadDataFromServer(mode);
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(getBaseContext()).inflate(R.layout.message_item, parent, false));
    }

    class Holder extends BaseViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView titleTv;
        private TextView tiemTv;
        private MessageBean bean;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.pay_img);
            tiemTv = (TextView) itemView.findViewById(R.id.time);
            titleTv = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(int i) {
            bean = modules.get(i);
            titleTv.setText(bean.getTitle());
            tiemTv.setText(bean.getRelease_time());
            if(bean.getStatus() == 0){
                imageView.setImageResource(R.drawable.messagered);
            }else{
                imageView.setImageResource(R.drawable.message);
            }
        }
        @Override
        public void onClick(View view) {
            imageView.setImageResource(R.drawable.message);
            bean.setStatus(1);
            Intent intent=new Intent(MessageActivity.this,MessageDetailActivity.class);
            intent.putExtra("bean",bean);
            startActivity(intent);
            messageModel.commitUpdata(bean.getId()+"");
        }
    }
}
