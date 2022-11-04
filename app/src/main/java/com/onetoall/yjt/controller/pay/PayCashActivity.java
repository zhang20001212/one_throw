package com.onetoall.yjt.controller.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.PayOrder;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PayRequestParameter;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.PayCashModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.qw.http.L;

import java.math.BigDecimal;

/**
 * 现金支付页面
 * Created by user on 2016/11/1.
 */

public class PayCashActivity extends BaseActivity {
    private static final String COLLECT_PRICE_TOTAL = "COLLECT_PRICE_TOTAL";//收钱
    private double totalPrice;//收钱总额
    private LinearLayout mCashLayout;
    private TextView mCashChargeLabel;//找零
    private TextView mCashHaveGetLabel;//实收
    private ImageView mCashDeleBtn;//回删
    private TextView mCash100Label;//100
    private TextView mCash50Label;//50
    private TextView mCash20Label;
    private TextView mCash10Label;
    private GridView mCashCashGv;//收款键盘
    private Button mCashSureBtn;//确定
    private static String types[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "删除"};
    private String endNumber = "";
    private PayCashModel mPayCashModel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pay_cash, true);
    }

    @Override
    protected void initView() {
        assignViews();
        MyAdapter adapter = new MyAdapter();
        mCashCashGv.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        totalPrice = getIntent().getDoubleExtra(COLLECT_PRICE_TOTAL, 0.0d);
        setTitle("应收￥" + totalPrice);
        mPayCashModel = new PayCashModel(this);
        initLinstener();
    }

    private void assignViews() {
        mCashChargeLabel = (TextView) findViewById(R.id.charge_tv);
        mCashHaveGetLabel = (TextView) findViewById(R.id.have_get_tv);
        mCashDeleBtn = (ImageView) findViewById(R.id.dele_btn);
        mCash100Label = (TextView) findViewById(R.id.price_100_tv);
        mCash50Label = (TextView) findViewById(R.id.price_50_tv);
        mCash20Label = (TextView) findViewById(R.id.price_20_tv);
        mCash10Label = (TextView) findViewById(R.id.price_10_tv);
        mCashCashGv = (GridView) findViewById(R.id.payCashGv);
        mCashSureBtn = (Button) findViewById(R.id.sure_btn);
        mCashLayout = (LinearLayout) findViewById(R.id.payCashLinear);
    }

    private void initLinstener() {
        mCashCashGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 11) {
                    endNumber = appendShowNumber(types[i], endNumber);
                    if (parseStringToDouble(endNumber) > 999999.99) {
                        showToast("超过最大值");
                        endNumber = subStringValues(endNumber);
                    } else if (parseStringToDouble(endNumber) > totalPrice) {
                        mCashChargeLabel.setText("找零￥" + subValue(parseStringToDouble(endNumber), totalPrice));
                    }
                    mCashHaveGetLabel.setText("￥" + endNumber);
                } else {
                    UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cashdelete);
                    endNumber = "";
                    mCashHaveGetLabel.setText("￥" + 0.00);
                    mCashChargeLabel.setText("找零￥0.00");
                }
            }
        });
        mCash100Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cash100);
                mCashHaveGetLabel.setText("￥100");
                endNumber = "100";
                if (parseStringToDouble(endNumber) > totalPrice) {
                    mCashChargeLabel.setText("找零￥" + subValue(parseStringToDouble(endNumber), totalPrice));

                }
            }
        });
        mCash50Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cash50);
                mCashHaveGetLabel.setText("￥50");
                endNumber = "50";
                if (parseStringToDouble(endNumber) > totalPrice) {
                    mCashChargeLabel.setText("找零￥" + subValue(parseStringToDouble(endNumber), totalPrice));

                }
            }
        });
        mCash20Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cash20);
                mCashHaveGetLabel.setText("￥20");
                endNumber = "20";
                if (parseStringToDouble(endNumber) > totalPrice) {
                    mCashChargeLabel.setText("找零￥" + subValue(parseStringToDouble(endNumber), totalPrice));

                }
            }
        });
        mCash10Label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cash10);
                mCashHaveGetLabel.setText("￥10");
                endNumber = "10";
                if (parseStringToDouble(endNumber) > totalPrice) {
                    mCashChargeLabel.setText("找零￥" + subValue(parseStringToDouble(endNumber), totalPrice));

                }
            }
        });
        mCashSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cashok);
                double endPrice = gainEndPrice(mCashHaveGetLabel.getText().toString());
                L.d(endPrice + "===");
                if (endPrice < totalPrice) {
                    showToast("收款金额低于应收金额");
                } else {
//                    mCashChargeLabel.setText("找零￥"+subValue(endPrice,totalPrice));
                    doPayCash();
                }
            }
        });
        mCashDeleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endNumber = subStringValues(endNumber);
                mCashHaveGetLabel.setText("￥" + endNumber);
                if (parseStringToDouble(endNumber) > totalPrice) {
                    mCashChargeLabel.setText("找零￥" + subValue(parseStringToDouble(endNumber), totalPrice));
                } else {
                    mCashChargeLabel.setText("找零￥0.00");
                }
            }
        });

    }

    private double subValue(double bigNum, double littleNum) {
        BigDecimal b1 = new BigDecimal(Double.toString(bigNum));
        BigDecimal b2 = new BigDecimal(Double.toString(littleNum));
        return b1.subtract(b2).doubleValue();

    }

    /**
     * 判断是否为金额
     *
     * @return true 是，false 否
     */
    private boolean isReallyMoney(String money) {

        return money.matches("^(([1-9]\\d{0,5})|0)(\\.\\d{0,2})?$");
    }

    /**
     * 字符串转为double
     *
     * @param number
     * @return
     */
    private double parseStringToDouble(String number) {
        return TextUtils.isEmpty(number) ? 0.0d : Double.valueOf(number);
    }

    /**
     * 拼接输入的数字作为展示值
     *
     * @param inputChar 输入的数值
     * @param endNumber 初始窜
     * @return
     */
    private String appendShowNumber(String inputChar, String endNumber) {

        if ("".equals(endNumber) && ".".equals(inputChar)) {//处理直接按小数点情况
            return "0.";
        } else if ("0".equals(endNumber)) {//处理按0后，按其他值正则验证false的情况，如01
            endNumber = "";
            return appendShowNumber(inputChar, endNumber);
        } else if (endNumber.contains(".0") && "0".equals(inputChar)) {//处理按到全部为0后最后一位值不变动的情况
            return endNumber;
        } else {//处理'+'的情况
            return isReallyMoney(endNumber + inputChar) ? endNumber + inputChar : endNumber;
        }
    }

    /**
     * 切割字符串获取最终价格
     *
     * @param price
     */
    private double gainEndPrice(String price) {
        String[] prices = price.split("￥");
        return parseStringToDouble(prices[1]);
    }

    /**
     * 切割字符串最后一个字符
     *
     * @param subValues
     * @return
     */
    private String subStringValues(String subValues) {
        if (subValues.length() == 0) {
            return "0";
        }
        return subValues.length() == 1 ? "0" : subValues.substring(0, subValues.length() - 1);
    }

    private void doPayCash() {
        showProgress();
        PayRequestParameter parameter = new PayRequestParameter();
        parameter.price = totalPrice + "";
        parameter.username = MyApplication.getInstance().getUser().getUser_name();
        parameter.type = PayOrder.MONEY + "";
        parameter.trans_account = PayRequestParameter.TRANS_ACCOUNT_TYPE_MONEY;
        parameter.mobile = MyApplication.getInstance().getUser().getMobile();
        parameter.store_id = MyApplication.getInstance().getStore().getStore_id() + "";
        parameter.merchant_id = MyApplication.getInstance().getStore().getMerchant_id() + "";
        mPayCashModel.collectMoneyByScanner(parameter, new Callback<PayOrderDetail>() {
            @Override
            public void onSuccess(PayOrderDetail data) {
                closeProgress();
                goPaySuccessActivity(data);
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    private void goPaySuccessActivity(PayOrderDetail data) {
        Intent intent = new Intent(this, PaySuccessActivity.class);
        intent.putExtra(Constants.ARG_PAY_ORDER_DETAIL_ENTITY, data);
        startActivity(intent);
        finish();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return types.length;
        }

        @Override
        public Object getItem(int i) {
            return types[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(PayCashActivity.this).inflate(R.layout.gv_paycash_calcalar_item, null);
            TextView mTypeLabel = (TextView) view.findViewById(R.id.calacularTypeLabel);
            mTypeLabel.setHeight(mCashLayout.getHeight() / 4);
            mTypeLabel.setText(types[i]);
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(PayCashActivity.this, UMEvent.cashback);
    }
}
