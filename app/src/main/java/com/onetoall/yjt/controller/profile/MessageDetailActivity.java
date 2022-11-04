package com.onetoall.yjt.controller.profile;

import android.os.Bundle;
import android.widget.TextView;

import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.MessageBean;

import java.io.Serializable;

/**
 * Created by shaomengjie on 2016/12/6.
 */

public class MessageDetailActivity extends BaseActivity {
    private TextView mTitleLabel;
    private TextView mDirscLabel;
    private TextView mDetailLabel;

    private void assignViews() {
        mTitleLabel = (TextView) findViewById(R.id.textView19);
        mDirscLabel = (TextView) findViewById(R.id.textView22);
        mDetailLabel = (TextView) findViewById(R.id.textView23);
        MessageBean bean = (MessageBean) getIntent().getSerializableExtra("bean");
        mTitleLabel.setText(bean.getTitle());
        mDetailLabel.setText(bean.getContent());
        mDirscLabel.setText(bean.getCreate_time());

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message_detail, true);
    }

    @Override
    protected void initView() {
        assignViews();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("消息详情");
    }
}
