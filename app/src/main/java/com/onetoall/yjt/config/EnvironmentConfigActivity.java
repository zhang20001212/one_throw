package com.onetoall.yjt.config;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseListActivity;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.qw.framework.utils.TextUtil;
import com.qw.framework.widget.pull.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by qinwei on 2016/10/21 10:04
 * email:qinwei_it@163.com
 */

public class EnvironmentConfigActivity extends BaseListActivity<Environment.EnvironmentBean> {

    public int currentIndex;

    @Override
    protected void setContentView() {
        setContentView(R.layout.environment_activity_list, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("环境切换");
        mPullRecyclerView.setEnablePullToStart(false);
        ArrayList<Environment.EnvironmentBean> beans = Environment.getInstance().getEnvironmentBeans();
        String tag = Environment.getInstance().getEnvironmentName();
        if (TextUtil.isValidate(tag)) {
            for (int i = 0; i < beans.size(); i++) {
                if (beans.get(i).name.equals(tag)) {
                    currentIndex = i;
                    break;
                }
            }
        }
        modules.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(this).inflate(R.layout.environment_layout_list_item, parent, false));
    }

    class Holder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mEnvironmentTitleLabel;
        private TextView mEnvironmentUrlLabel;
        private RadioButton mEnvironmentCheckStateRb;
        private Environment.EnvironmentBean bean;

        public Holder(View v) {
            super(v);
            mEnvironmentTitleLabel = (TextView) v.findViewById(R.id.mEnvironmentTitleLabel);
            mEnvironmentUrlLabel = (TextView) v.findViewById(R.id.mEnvironmentUrlLabel);
            mEnvironmentCheckStateRb = (RadioButton) v.findViewById(R.id.mEnvironmentCheckStateLabel);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void bindData(int i) {
            bean = modules.get(i);
            mEnvironmentTitleLabel.setText(bean.name);
            if (bean.name.equals(Environment.TAG_DEV)) {
                String domain = PrefsAccessor.getInstance(EnvironmentConfigActivity.this).getString(Environment.KEY_ENVIRONMENT_CUSTOM_DOMAIN);
                if (TextUtils.isEmpty(domain)) {
                    mEnvironmentUrlLabel.setText(bean.domain);
                } else {
                    mEnvironmentUrlLabel.setText(domain);
                }
            } else {
                mEnvironmentUrlLabel.setText(bean.domain);
            }
            if (i == currentIndex) {
                mEnvironmentCheckStateRb.setChecked(true);
            } else {
                mEnvironmentCheckStateRb.setChecked(false);
            }
        }

        @Override
        public void onClick(View v) {
            int clickIndex = modules.indexOf(bean);
            if (clickIndex == currentIndex) {
                return;
            }
            currentIndex = clickIndex;
//            TODO 缓存当前选择的环境
            PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString(Environment.KEY_ENVIRONMENT_TAG, bean.name);
            adapter.notifyDataSetChanged();
            API.setDomain(Environment.getInstance().getEnvironmentDomain());
            Toast.makeText(EnvironmentConfigActivity.this, bean.name + ",切换成功" + Environment.getInstance().getEnvironmentDomain(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            final int clickIndex = modules.indexOf(bean);
            if (clickIndex == 0) {
                View domainView = LayoutInflater.from(EnvironmentConfigActivity.this).inflate(R.layout.layout_domain_settings, null);
                final EditText mDomainIpEdt = (EditText) domainView.findViewById(R.id.mDomainIpEdt);
                final EditText mDomainDKEdt = (EditText) domainView.findViewById(R.id.mDomainDKEdt);
                mDomainIpEdt.setText(PrefsAccessor.getInstance(EnvironmentConfigActivity.this).getString("ip"));
                mDomainDKEdt.setText(PrefsAccessor.getInstance(EnvironmentConfigActivity.this).getString("dk"));
                new AlertDialog.Builder(EnvironmentConfigActivity.this).setTitle("自定义domain设置")
                        .setView(domainView)
                        .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ip = mDomainIpEdt.getText().toString();
                                String dk = mDomainDKEdt.getText().toString();
                                String domain = "http://" + ip + ":" + dk;
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString("ip", ip);
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString("dk", dk);
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString(Environment.KEY_ENVIRONMENT_TAG, bean.name);
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString(Environment.KEY_ENVIRONMENT_CUSTOM_DOMAIN, domain);
                                API.setDomain(Environment.getInstance().getEnvironmentDomain());
                                Toast.makeText(EnvironmentConfigActivity.this, bean.name + ",切换成功" + Environment.getInstance().getEnvironmentDomain(), Toast.LENGTH_SHORT).show();
                                currentIndex = clickIndex;
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString("ip", "");
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString("dk", "");
                                PrefsAccessor.getInstance(EnvironmentConfigActivity.this).saveString(Environment.KEY_ENVIRONMENT_CUSTOM_DOMAIN, bean.domain);
                                Toast.makeText(EnvironmentConfigActivity.this, bean.name + ",自定义domain 清除成功", Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            }
                        }).show();
            }
            return false;
        }
    }

}
