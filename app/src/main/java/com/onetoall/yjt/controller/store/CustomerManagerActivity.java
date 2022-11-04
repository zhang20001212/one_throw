package com.onetoall.yjt.controller.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseListActivity;
import com.onetoall.yjt.domain.CollectionMoneyBean;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.CollectionMoneyModel;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.LoadingView;
import com.qw.framework.widget.pull.BaseListAdapter;
import com.qw.framework.widget.pull.BaseViewHolder;
import com.qw.framework.widget.pull.IFooterState;
import com.qw.framework.widget.pull.PullRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户列表
 * Created by shaomengjie on 2016/11/20.
 */

public class CustomerManagerActivity extends BaseListActivity<CollectionMoneyBean.ListBean>{
    private CollectionMoneyModel collectionMoneyModel;
    private int page = 1;
    private Boolean isFirstLoad = true;
    private String time = "";
    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("客户列表");
        collectionMoneyModel = new CollectionMoneyModel(this);
        loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
    }

    private void loadDataFromServer(final int mode) {
        collectionMoneyModel.loadCollectionMoney(MyApplication.getInstance().getStore().getStore_id() + "", page+"", new Callback<CollectionMoneyBean>() {
            @Override
            public void onSuccess(CollectionMoneyBean data) {
                if (data!=null){
                    if (mode == PullRecyclerView.MODE_PULL_TO_START) modules.clear();
                    initBeab(data);
                    if (data.getList().size() < 10) {
                        mPullRecyclerView.onRefreshCompleted(mode, IFooterState.LOAD_MORE_STATE_NO_DATA);
                    } else {
                        mPullRecyclerView.onRefreshCompleted(mode, IFooterState.LOAD_MORE_STATE_IDLE);
                    }

                }
                modules.addAll(data.getList());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int code, String msg) {
                if (isFirstLoad) {

                } else {
                    mPullRecyclerView.onRefreshCompleted(mode);
                }
            }
        });

    }

    @Override
    public void onRefresh(int mode) {
        super.onRefresh(mode);
        if (mode == PullRecyclerView.MODE_PULL_TO_START) {
            page = 1;
            //isFirstLoad = true;
        } else {
            page++;
        }
        loadDataFromServer(mode);
    }

    /**
     * 对数据进行处理，屏蔽当日时间重复显示
     * @param data
     */
    private void initBeab(CollectionMoneyBean data) {
        List<CollectionMoneyBean.ListBean> list = data.getList();
        for (CollectionMoneyBean.ListBean li : list) {
            if (time.equals(li.getDate_time())) {
                li.setDate_time("");
            } else {
                time = li.getDate_time();
            }
        }
        time = "";
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_collect_moey,true);
    }

    @Override
    protected void initView() {
        super.initView();
        mPullRecyclerView.setEnablePullToEnd(true);
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_collect_money_item, parent, false));
    }

    class Holder extends BaseViewHolder{

        private TextView timeTv;
        private ImageView payImg;
        private TextView payTv;
        private TextView payPeople;
        private TextView payPrice;
        private RelativeLayout aliPayBtn;

        public Holder(View itemView) {
            super(itemView);
            timeTv = (TextView) itemView.findViewById(R.id.time_tv);
            payImg = (ImageView) itemView.findViewById(R.id.pay_img);
            payTv = (TextView) itemView.findViewById(R.id.pay_tv);
            payPeople = (TextView) itemView.findViewById(R.id.pay_people);
            payPrice = (TextView) itemView.findViewById(R.id.pay_price);
        }

        @Override
        public void bindData(int i) {
            CollectionMoneyBean.ListBean listBean = modules.get(i);
            if (listBean.getDate_time().equals("")) {
                timeTv.setVisibility(View.GONE);
                timeTv.setText(listBean.getDate_time() + "");
            } else {
                timeTv.setVisibility(View.VISIBLE);
                timeTv.setText(listBean.getDate_time() + "");
            }
            String pay_type = "";
            switch (listBean.getPayment_type()) {
                case "1":
                    payImg.setImageResource(R.drawable.ic_collect_money_type_alipay);
                    pay_type = "支付宝收款";
                    break;
                case "2":
                    payImg.setImageResource(R.drawable.ic_collect_money_type_winxin);
                    pay_type = "微信收款";
                    break;
                case "3":
                    payImg.setImageResource(R.drawable.ic_collect_money_type_money);
                    pay_type = "什码付收款";
                    break;
                case "4":
                    payImg.setImageResource(R.drawable.ic_collect_money_type_money);
                    pay_type = "现金收款";
                    break;
            }
            switch (listBean.getPayment_type()) {
                case "1":
                    payTv.setText("支付宝用户");
                    break;
                case "2":
                    payTv.setText("微信用户");
                    break;
            }
            payPeople.setText("消费次数: " + listBean.getCounts());
            payPrice.setText("¥ " + listBean.getSum());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this, UMEvent.shopcustomerctrlback);
    }
}
