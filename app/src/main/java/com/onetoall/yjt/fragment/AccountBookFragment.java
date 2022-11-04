package com.onetoall.yjt.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.order.ChannelRatioActivity;
import com.onetoall.yjt.controller.order.PayOrderDetailActivity;
import com.onetoall.yjt.core.BaseListFragment;
import com.onetoall.yjt.domain.AccountBookIndex;
import com.onetoall.yjt.domain.PayOrder;
import com.onetoall.yjt.domain.PayTotal;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.model.impl.AccountBookModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.LoadingView;
import com.onetoall.yjt.widget.QToolbar;
import com.onetoall.yjt.widget.tab.ITabFragment;
import com.qw.framework.utils.TextUtil;
import com.qw.framework.utils.TimeHelper;
import com.qw.framework.widget.pull.BaseViewHolder;
import com.qw.framework.widget.pull.IFooterState;
import com.qw.framework.widget.pull.PullRecyclerView;

/**
 * Created by qinwei on 2016/10/18 13:40
 * email:qinwei_it@163.com
 */

public class AccountBookFragment extends BaseListFragment<PayOrder> implements ITabFragment, LoadingView.OnRetryListener {
    private QToolbar toolbar;
    private AccountBookModel mAccountBookModel;
    private LoadingView mLoadingView;
    private boolean isFirstLoad = true;
    private long timestamp;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_account_book;
    }

    @Override
    protected void initView(View v) {
        super.initView(v);
        toolbar = (QToolbar) v.findViewById(R.id.toolbar);
        mLoadingView = (LoadingView) v.findViewById(R.id.mLoadingView);
        mPullRecyclerView.setEnablePullToEnd(true);
        mLoadingView.setOnRetryListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        adapter.isHeaderShow = true;
        toolbar.setTitle(R.string.app_home_account_book_label);
        mAccountBookModel = new AccountBookModel((OnBaseModelListener) getActivity());
        loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
    }

    public void loadDataFromServer(final int mode) {
        mAccountBookModel.loadAccountBookIndex(MyApplication.getInstance().getStore().getStore_id() + "", timestamp + "", new Callback<AccountBookIndex>() {
            @Override
            public void onSuccess(AccountBookIndex data) {
                if (isFirstLoad) {
                    mLoadingView.notifyDataChanged(LoadingView.State.done);
                    isFirstLoad = false;
                }
                if (timestamp == 0) {
                    //初始化统计数据
                    setHeader(data.getStatistics());
                }
                if (data.getList().size() < 15) {
                    mPullRecyclerView.onRefreshCompleted(mode, IFooterState.LOAD_MORE_STATE_NO_DATA);
                } else {
                    mPullRecyclerView.onRefreshCompleted(mode, IFooterState.LOAD_MORE_STATE_IDLE);
                }
                if (mode == PullRecyclerView.MODE_PULL_TO_START) modules.clear();
                modules.addAll(data.getList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code, String msg) {
                if (isFirstLoad) {
                    mLoadingView.notifyDataChanged(LoadingView.State.error);
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
            timestamp = 0;
        } else {
            if(TextUtil.isValidate(modules)){
                timestamp = modules.get(modules.size() - 1).getData_long();
            }
        }
        loadDataFromServer(mode);
    }


    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(getActivity()).inflate(R.layout.layout_account_book_item, parent, false));
    }

    @Override
    public void onRetry() {
        mLoadingView.notifyDataChanged(LoadingView.State.ing);
        loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
    }

    class Holder extends BaseViewHolder implements View.OnClickListener {
        private  View mLine;
        private TextView mGroupTitleLabel;
        private TextView mAccountBookItemAmountLabel;
        private ImageView mAccountBookItemIconImg;
        private TextView mAccountBookItemStateLabel;
        private TextView mAccountBookItemCreateTimeLabel;
        private TextView mAccountBookItemNickLabel;
        private PayOrder module;

        public Holder(View v) {
            super(v);
            mLine=v.findViewById(R.id.mLine);
            mAccountBookItemIconImg = (ImageView) v.findViewById(R.id.mAccountBookItemIconImg);
            mAccountBookItemStateLabel = (TextView) v.findViewById(R.id.mAccountBookItemStateLabel);
            mAccountBookItemCreateTimeLabel = (TextView) v.findViewById(R.id.mAccountBookItemCreateTimeLabel);
            mAccountBookItemNickLabel = (TextView) v.findViewById(R.id.mAccountBookItemNickLabel);
            mAccountBookItemAmountLabel = (TextView) v.findViewById(R.id.mAccountBookItemAmountLabel);
            mGroupTitleLabel = (TextView) v.findViewById(R.id.mGroupTitleLabel);
            v.setOnClickListener(this);
        }


        @Override
        public void bindData(int i) {
            module = modules.get(i);
            if (module.getPayment_type() == PayOrder.ALIPAY) {
                mAccountBookItemIconImg.setImageResource(R.drawable.ic_collect_money_type_alipay);
                if (module.getStatus() == 1) {
                    mAccountBookItemStateLabel.setText("支付宝收款成功");
                } else {
                    mAccountBookItemStateLabel.setText("支付宝收款失败");
                }
            } else if (module.getPayment_type() == PayOrder.WEIXIN) {
                mAccountBookItemIconImg.setImageResource(R.drawable.ic_collect_money_type_winxin);
                if (module.getStatus() == 1) {
                    mAccountBookItemStateLabel.setText("微信收款成功");
                } else {
                    mAccountBookItemStateLabel.setText("微信收款失败");
                }
            } else {
                mAccountBookItemIconImg.setImageResource(R.drawable.ic_collect_money_type_money);
                if (module.getStatus() == 1) {
                    mAccountBookItemStateLabel.setText("现金收款成功");
                } else {
                    mAccountBookItemStateLabel.setText("现金收款失败");
                }
            }
            mAccountBookItemCreateTimeLabel.setText(module.getCreate_time());
            mAccountBookItemNickLabel.setText(module.getCashier());
            mAccountBookItemAmountLabel.setText(module.getGood_amount());

            if (i == 0 || modules.get(i - 1).getDate_int() != module.getDate_int()) {
                mGroupTitleLabel.setVisibility(View.VISIBLE);
                mGroupTitleLabel.setText(TimeHelper.updateMilliSecToFormatDateStr(module.getData_long(),"yyyy-MM-dd"));
            } else {
                mLine.setVisibility(View.GONE);
                mGroupTitleLabel.setVisibility(View.GONE);
            }

            if (module.getStatus() == 1) {
                mAccountBookItemAmountLabel.setTextColor(getResources().getColor(R.color.primary_text));
            } else {
                mAccountBookItemAmountLabel.setTextColor(getResources().getColor(R.color.red));
            }

        }

        @Override
        public void onClick(View v) {
            UMEventUtil.onEvent(getActivity(),UMEvent.acpaydetail);
            Intent intent = new Intent(getActivity(), PayOrderDetailActivity.class);
            intent.putExtra(Constants.ARG_PAY_ORDER_ID, module.getId() + "");
            startActivity(intent);
        }
    }

    private View header;

    @Override
    protected View onCreateHeaderView(ViewGroup parent) {
        header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_account_book_header, parent, false);
        return header;
    }

    public void setHeader(PayTotal a) {
        View dat_1 = header.findViewById(R.id.day_1);
        View dat_7 = header.findViewById(R.id.day_7);
        View dat_30 = header.findViewById(R.id.day_30);
        bingView(dat_1, 0, a.getY_amount(), a.getY_counts(), a.getToday_counts(), a.getIncrease_rate(), a.getToday_amount(), a.getToday_counts(), a.getIncrease_counts_rate());
        bingView(dat_7, 1, a.getY_amount_7(), a.getY_counts_7(), a.getToday_counts_7(), a.getIncrease_rate_7(), a.getToday_amount_7(), a.getToday_counts_7(), a.getIncrease_counts_rate_7());
        bingView(dat_30, 2, a.getY_amount_30(), a.getY_counts_30(), a.getToday_counts_30(), a.getIncrease_rate_30(), a.getToday_amount_30(), a.getToday_counts_30(), a.getIncrease_counts_rate_30());
    }

    void bingView(View view, int aa, String a, String bb, int b, String c, String d, double e, String f) {
        TextView price_tv = (TextView) view.findViewById(R.id.price_tv);
        TextView bishu_tv = (TextView) view.findViewById(R.id.bishu_tv);
        TextView textView5 = (TextView) view.findViewById(R.id.textView5);
        TextView textView6 = (TextView) view.findViewById(R.id.textView6);
        TextView textView10 = (TextView) view.findViewById(R.id.textView10);
        TextView textView8 = (TextView) view.findViewById(R.id.textView8);
        TextView textView7 = (TextView) view.findViewById(R.id.textView7);
        final TextView jinri = (TextView) view.findViewById(R.id.jinri);

        ImageView image10 = (ImageView) view.findViewById(R.id.imageView10);
        ImageView image7 = (ImageView) view.findViewById(R.id.imageView7);

        if (aa == 0) {
            textView5.setText("昨日");
            textView8.setText("昨日");
        } else if (aa == 1) {
            jinri.setText("7天销售额");
        } else {
            jinri.setText("30天销售额");
        }
        price_tv.setText(d + "");
        bishu_tv.setText(b + "笔");
        textView6.setText("" + a + "");
        switch (a) {
            case "0":
                textView7.setTextColor(getResources().getColor(R.color.red));
                textView7.setText("暂无数据");
                image7.setVisibility(View.INVISIBLE);
                break;
            default:
                textView7.setText(f.replace("-", "") + "%");
                if(f!=null&&f.indexOf("-")>-1){
                    textView7.setTextColor(Color.GREEN);
                    image7.setImageResource(R.drawable.ic_account_book_state_down);
                    image7.setVisibility(View.VISIBLE);
                }else{
                    textView7.setTextColor(getResources().getColor(R.color.red));
                    image7.setImageResource(R.drawable.ic_account_book_state_up);
                    image7.setVisibility(View.VISIBLE);
                }
                break;
        }

        switch (bb) {
            case "0":
                textView10.setText("暂无数据");
                image10.setVisibility(View.INVISIBLE);
                textView10.setTextColor(getResources().getColor(R.color.red));
                break;
            default:
                if(c!=null&&c.indexOf("-")>-1){
                    textView10.setText(c.replace("-", "") + "%");
                    image10.setImageResource(R.drawable.ic_account_book_state_down);
                    image10.setVisibility(View.VISIBLE);
                    textView10.setTextColor(Color.GREEN);
                }else{
                    textView10.setText(c.replace("-", "") + "%");
                    textView10.setTextColor(getResources().getColor(R.color.red));
                    image10.setVisibility(View.VISIBLE);
                    image10.setImageResource(R.drawable.ic_account_book_state_up);
                }
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChannelRatioActivity.class);
                if ("今日销售额".equals(jinri.getText().toString())){
                    UMEventUtil.onEvent(AccountBookFragment.this.getActivity(), UMEvent.actotaltoday);
                    intent.putExtra(Constants.ACCOUNT_UM_KEY,Constants.ACCOUNT_UM_VALUE_1);
                }
                if ("7天销售额".equals(jinri.getText().toString())){
                    UMEventUtil.onEvent(AccountBookFragment.this.getActivity(), UMEvent.actotal7day);
                    intent.putExtra(Constants.ACCOUNT_UM_KEY,Constants.ACCOUNT_UM_VALUE_7);
                }
                if ("30天销售额".equals(jinri.getText().toString())){
                    UMEventUtil.onEvent(AccountBookFragment.this.getActivity(), UMEvent.actotal30day);
                    intent.putExtra(Constants.ACCOUNT_UM_KEY,Constants.ACCOUNT_UM_VALUE_30);
                }
                startActivity(intent);

            }
        });
    }

    @Override
    public void onMenuItemClick(int id) {

    }

    private long refreshTimestamp=0;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(System.currentTimeMillis()-refreshTimestamp>5000){
                timestamp = 0;
                loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
            }
            refreshTimestamp=System.currentTimeMillis();
        }
    }
}
