package com.onetoall.yjt.controller.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseRefreshActivity;
import com.onetoall.yjt.domain.ChannelRatio;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.ChannelRatioModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.RefreshView;


/**
 * Created by shaomengjie on 2016/11/18.
 */

public class ChannelRatioActivity extends BaseRefreshActivity {
    private String orderId;
    private ChannelRatioModel channelRatioModel;
    private View zhifubao_1;
    private View weixin_1;
    private View yimafu_1;

    private View zhifubao_7;
    private View weixin_7;
    private View yimafu_7;

    private View zhifubao_30;
    private View weixin_30;
    private View yimafu_30;

    private View xianjin_1;
    private View xianjin_7;
    private View xianjin_30;

    private LinearLayout background;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_channel_ratio,true);
    }

    @Override
    protected void initView() {
        super.initView();
        zhifubao_1 = findViewById(R.id.zhifubao_1);
        weixin_1 =  findViewById(R.id.weixin_1);
        yimafu_1 = findViewById(R.id.yimafu_1);
        xianjin_1 =  findViewById(R.id.xianjing_1);

        zhifubao_7 = findViewById(R.id.zhifubao_7);
        weixin_7 = findViewById(R.id.weixin_7);
        yimafu_7 = findViewById(R.id.yimafu_7);
        xianjin_7 = findViewById(R.id.xianjing_7);

        zhifubao_30 = findViewById(R.id.zhifubao_30);
        weixin_30 = findViewById(R.id.weixin_30);
        yimafu_30 = findViewById(R.id.yimafu_30);
        xianjin_30 = findViewById(R.id.xianjing_30);

        background = (LinearLayout) findViewById(R.id.background);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("各渠道占比");
        channelRatioModel = new ChannelRatioModel(this);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        channelRatioModel.loadChanneRatio(MyApplication.getInstance().getStore().getStore_id()+"", new Callback<ChannelRatio>() {
            @Override
            public void onSuccess(ChannelRatio data) {
                changeRefreshState(RefreshView.State.done);
                setPayOrderDetail(data);
            }
            @Override
            public void onFailure(int code, String msg) {
                changeRefreshState(RefreshView.State.error);
            }
        });

    }

    private void setPayOrderDetail(ChannelRatio data) {
        bingView(zhifubao_1, 1, data.getTotal_zfb(), data.getTotal_zfb_pro() + "");
        bingView(zhifubao_7, 1, data.getTotal_zfb_7(), data.getTotal_zfb_pro_7() + "");
        bingView(zhifubao_30, 1, data.getTotal_zfb_30(), data.getTotal_zfb_pro_30() + "");

        bingView(weixin_1, 2, data.getTotal_wx(), data.getTotal_wx_pro() + "");
        bingView(weixin_7, 2, data.getTotal_wx_7(), data.getTotal_wx_pro_7() + "");
        bingView(weixin_30, 2, data.getTotal_wx_30(), data.getTotal_wx_pro_30() + "");

        bingView(xianjin_1, 4, data.getTotal_cash(),data.getTotal_cash_pro() + "");
        bingView(xianjin_7, 4, data.getTotal_cash_7(), data.getTotal_cash_pro_7() + "");
        bingView(xianjin_30, 4, data.getTotal_cash_30(),data.getTotal_cash_pro_30() + "");
        background.setVisibility(View.VISIBLE);
    }

    void bingView(View view, int aa, String a, String b) {
        TextView price_tv = (TextView) view.findViewById(R.id.price_tv);
        TextView bishu_tv = (TextView) view.findViewById(R.id.bishu_tv);
        ImageView img = (ImageView) view.findViewById(R.id.img);

        switch (aa) {
            case 1:
                img.setImageResource(R.drawable.ic_collect_money_type_alipay);
                break;
            case 2:
                img.setImageResource(R.drawable.ic_collect_money_type_winxin);
                break;
            case 3:
                img.setImageResource(R.drawable.ic_collect_money_type_money);
                break;
            case 4:
                img.setImageResource(R.drawable.ic_collect_money_type_money);
                break;
        }

        price_tv.setText("¥" + a + "");
        bishu_tv.setText(b + "%");
    }

    @Override
    public void onRetry() {
        super.onRetry();
        changeRefreshState(RefreshView.State.ing);
        loadDataFromServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switch (getIntent().getIntExtra(Constants.ACCOUNT_UM_KEY,-1)){
            case Constants.ACCOUNT_UM_VALUE_1:
                UMEventUtil.onEvent(ChannelRatioActivity.this, UMEvent.actotaltodayback);
                break;
            case Constants.ACCOUNT_UM_VALUE_7:
                UMEventUtil.onEvent(ChannelRatioActivity.this, UMEvent.actotal7dayback);
                break;
            case Constants.ACCOUNT_UM_VALUE_30:
                UMEventUtil.onEvent(ChannelRatioActivity.this, UMEvent.actotal30dayback);
                break;
        }
    }
}
