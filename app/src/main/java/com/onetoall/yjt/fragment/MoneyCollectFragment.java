package com.onetoall.yjt.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.pay.CollectMoneyTypeActivity;
import com.onetoall.yjt.controller.profile.MessageActivity;
import com.onetoall.yjt.core.BaseFragment;
import com.onetoall.yjt.domain.PushMessage;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayResultCallBack;
import com.onetoall.yjt.push.PushManager;
import com.onetoall.yjt.push.PushWatcher;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.QToolbar;
import com.onetoall.yjt.widget.tab.ITabFragment;
import com.qw.framework.utils.Trace;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

import java.math.BigDecimal;

import static com.onetoall.yjt.R.id.moneyCollectBack;

/**
 * 收钱
 * =======
 * /**
 * 收钱
 * Created by qinwei on 2016/10/18 13:35
 * email:qinwei_it@163.com
 */

public class MoneyCollectFragment extends BaseFragment implements ITabFragment {
    private QToolbar toolbar;
    private Button mMoneyCollectAmountBtn;//去收款
    private TextView mMoneyCollectHava;//当前选择了几个商品
    private TextView mMoneyCollectInputLabel;//输入的金额
    private ImageView mMoneyCollectBackBtn;//回删
    private GridView mCashCalacutarGv;//按键
    private static final String COLLECT_PRICE_TOTAL = "COLLECT_PRICE_TOTAL";//收钱

    private static final char[] mTypes = {'7', '8', '9', '4', '5', '6', '1', '2', '3', '0', '.', '+'};//键盘显示的内容
    private String endNumber = "";//显示的结果
    private int addFlag = 0;//商品累加数量

    private double totalPrice = 0.0d;//减法运算统计总额
    private double proPrice = 0.0d;//前一个金额
    private double nowPrice = 0.0d;//相加后的金额
    private boolean isAdd;//判断是不是点击+
    private AlertDialog mClearDialog;
    private ImageView mNotifyMessageTipsImg;
    private ViewGroup mNotifyMessageLayout;
    private PushWatcher watcher = new PushWatcher() {

        @Override
        protected void receiverMessage(PushMessage pushMessage) {
            Trace.e("push message："+pushMessage.toString());
            mNotifyMessageTipsImg.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_collect_money;
    }

    @Override
    protected void initView(View v) {
        toolbar = (QToolbar) v.findViewById(R.id.toolbar);
        mNotifyMessageLayout = (ViewGroup) v.findViewById(R.id.mNotifyMessageLayout);
        mNotifyMessageTipsImg = (ImageView) v.findViewById(R.id.mNotifyMessageTipsImg);
        mNotifyMessageTipsImg.setVisibility(View.GONE);//根据当前是否有已读消息进行显示显示隐藏

        mMoneyCollectAmountBtn = (Button) v.findViewById(R.id.moneyCollectAmountBtn);
        mMoneyCollectHava = (TextView) v.findViewById(R.id.moneyCollectHava);
        mMoneyCollectInputLabel = (TextView) v.findViewById(R.id.item_price_btn);
        mMoneyCollectBackBtn = (ImageView) v.findViewById(moneyCollectBack);
        mCashCalacutarGv = (GridView) v.findViewById(R.id.cashCalacutarGv);
        mNotifyMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
        MyAdapter mCalcalarAdapter = new MyAdapter();
        mCashCalacutarGv.setAdapter(mCalcalarAdapter);
        showClearDialog();
    }

    @Override
    protected void initData() {
        super.initData();
        toolbar.setTitle(R.string.app_home_collect_money_label);
        isViewClearButton();
        initLinstener();
    }

    @Override
    public void onResume() {
        super.onResume();
        PushManager.getInstance().addObserver(watcher);
        loadDataFromServer();
    }

    @Override
    public void onPause() {
        super.onPause();
        PushManager.getInstance().deleteObserver(watcher);
    }

    private void loadDataFromServer() {
//        FIXME load message read number
        HttpRequest request = new HttpRequest(API.loadMessageReadStatus(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("username", MyApplication.getInstance().getUser().getMobile());
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result s) {
                if (s.getMsg() != null && s.getMsg().equals("0")) {
                    mNotifyMessageTipsImg.setVisibility(View.GONE);
                } else {
                    mNotifyMessageTipsImg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(AppException e) {
                e.printStackTrace();
            }
        });
        RequestManager.getInstance().execute("", request);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onMenuItemClick(int id) {

    }


    private void initLinstener() {

        toolbar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMEventUtil.onEvent(getActivity(), UMEvent.paytruncate);
                mClearDialog.show();
            }
        });
        mMoneyCollectAmountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMEventUtil.onEvent(MoneyCollectFragment.this.getActivity(), UMEvent.payfor);

                if (gainEndPrice(mMoneyCollectAmountBtn.getText().toString()) == 0.0d) {
                    showToast("请输入收款金额!");
                    return;
                }
                UMEventUtil.onEvent(getActivity(), UMEvent.pay);
                Intent intent = new Intent(getActivity(), CollectMoneyTypeActivity.class).putExtra(COLLECT_PRICE_TOTAL, gainEndPrice(mMoneyCollectAmountBtn.getText().toString()));
                startActivity(intent);

            }
        });
        mMoneyCollectBackBtn.setOnClickListener(new View.OnClickListener() {//回退数字
            @Override
            public void onClick(View view) {
                double tempPrice = addValue(nowPrice, parseStringToDouble(endNumber));
                totalPrice = parseStringToDouble(endNumber);
                if (Double.parseDouble(endNumber) != 0) {
                    endNumber = subStringValues(endNumber);
                    tempPrice = subValue(tempPrice, subValue(totalPrice, parseStringToDouble(endNumber)));
                    mMoneyCollectInputLabel.setText("￥" + endNumber);
                    mMoneyCollectAmountBtn.setText("去收款￥" + tempPrice);
                }
                if (Double.parseDouble(endNumber) == 0) {
                    mMoneyCollectBackBtn.setVisibility(View.INVISIBLE);
                    mMoneyCollectHava.setText("当前已选" + addFlag + "个商品");
                }
                isViewClearButton();
            }
        });

        mCashCalacutarGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {//按键
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mTypes[position] == '+' && !TextUtils.isEmpty(endNumber) && parseStringToDouble(endNumber) != 0.0d) {//判断按加号的情况，如果不是第一次点击+或者不是0值时可选加号
                    nowPrice = addValue(proPrice, nowPrice);
                    endNumber = "";
                    isAdd = true;
                    mMoneyCollectInputLabel.setText("￥" + 0.);
                    mMoneyCollectBackBtn.setVisibility(View.INVISIBLE);
                } else {
                    endNumber = appendShowNumber(mTypes[position], endNumber);
                    if (addValue(parseStringToDouble(endNumber), nowPrice) > 999999.99) {
                        endNumber = subStringValues(endNumber);
                        showToast("超过最大值");
                    } else {
                        proPrice = parseStringToDouble(endNumber);
                        mMoneyCollectInputLabel.setText("￥" + proPrice);
                        mMoneyCollectBackBtn.setVisibility(proPrice != 0 ? View.VISIBLE : View.INVISIBLE);
                        mMoneyCollectAmountBtn.setText("去收款￥" + addValue(proPrice, nowPrice));
                    }

                }
                if (parseStringToDouble(endNumber) > 0 && isAdd) {
                    addFlag++;
                    isAdd = false;
                }
                if (parseStringToDouble(endNumber) > 0) {
                    mMoneyCollectHava.setText("当前已选" + (addFlag + 1) + "个商品");
                }
                isViewClearButton();

            }
        });


    }

    /**
     * 重置
     */
    public void resetClear() {
        endNumber = "";
        addFlag = 0;
        isAdd = false;
        totalPrice = 0.0d;
        proPrice = 0.0d;
        nowPrice = 0.0d;
        mMoneyCollectInputLabel.setText("￥" + 0.);
        mMoneyCollectHava.setText("当前已选" + addFlag + "个商品");
        mMoneyCollectAmountBtn.setText("去收款￥0.0");
        mMoneyCollectBackBtn.setVisibility(View.INVISIBLE);
        isViewClearButton();
    }

    /**
     * 是否显示右上角的清空按钮
     */
    private void isViewClearButton() {
        if (gainEndPrice(mMoneyCollectAmountBtn.getText().toString()) == 0) {
            toolbar.setRightText(null);
        } else {
            toolbar.setRightText("清空");

        }
    }


    /**
     * 判断是否为金额
     *
     * @return true 是，false 否
     */
    private boolean isReallyMoney(String money) {

        return money.matches("^(([1-9]\\d{0,5})|0)(\\.\\d{0,2})?$");
    }

    private double addValue(double proPrice, double nowPrice) {
        BigDecimal b1 = new BigDecimal(Double.toString(proPrice));
        BigDecimal b2 = new BigDecimal(Double.toString(nowPrice));
        return b1.add(b2).doubleValue();
    }

    private double subValue(double bigNum, double littleNum) {
        BigDecimal b1 = new BigDecimal(Double.toString(bigNum));
        BigDecimal b2 = new BigDecimal(Double.toString(littleNum));
        return b1.subtract(b2).doubleValue();

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
     * 切割字符串最后一个字符
     *
     * @param subValues
     * @return
     */
    private String subStringValues(String subValues) {
        return subValues.length() == 1 ? "0" : parseStringToDouble(subValues.substring(0, subValues.length() - 1)) == 0 ? "0" : subValues.substring(0, subValues.length() - 1);
    }

    /**
     * 拼接输入的数字作为展示值
     *
     * @param inputChar 输入的数值
     * @param endNumber 初始窜
     * @return
     */
    private String appendShowNumber(char inputChar, String endNumber) {

        if ("".equals(endNumber) && inputChar == '.') {//处理直接按小数点情况
            return "0.";
        } else if ("0".equals(endNumber)) {//处理按0后，按其他值正则验证false的情况，如01
            endNumber = "";
            return appendShowNumber(inputChar, endNumber);
        } else if (endNumber.contains(".0") && inputChar == '0') {//处理按到全部为0后最后一位值不变动的情况
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

    private void showClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否确认清空？");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("全部清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetClear();
            }
        });
        mClearDialog = builder.create();
    }

    /**
     * 计算器适配器
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTypes.length;
        }

        @Override
        public Object getItem(int i) {
            return mTypes[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(MoneyCollectFragment.this.getActivity()).inflate(R.layout.gv_calcalar_item, null);
            TextView typingLabel = (TextView) view.findViewById(R.id.calacularTypeLabel);
            typingLabel.setText(String.valueOf(mTypes[i]));
            return view;
        }
    }
}
