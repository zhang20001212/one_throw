package com.onetoall.yjt.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.LoginActivity;
import com.onetoall.yjt.controller.profile.PersonalInfoActivity;
import com.onetoall.yjt.controller.profile.PersonnelManagementActivity;
import com.onetoall.yjt.core.BaseFragment;
import com.onetoall.yjt.domain.User;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.model.impl.UserModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PermissionHelper;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.QToolbar;
import com.onetoall.yjt.widget.row.BaseRowDescriptor;
import com.onetoall.yjt.widget.row.ContainerDescriptor;
import com.onetoall.yjt.widget.row.ContainerView;
import com.onetoall.yjt.widget.row.GroupDescriptor;
import com.onetoall.yjt.widget.row.OnRowClickListener;
import com.onetoall.yjt.widget.row.expand.IOSRowDescriptor;
import com.onetoall.yjt.widget.row.tool.RowActionEnum;
import com.onetoall.yjt.widget.tab.ITabFragment;

import java.util.ArrayList;

/**
 * Created by qinwei on 2016/10/18 13:40
 * email:qinwei_it@163.com
 */

public class ProfileFragment extends BaseFragment implements ITabFragment, OnRowClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private QToolbar toolbar;
    private TextView mStoreNameLabel;
    private TextView mUserNameLabel;
    private TextView mUserJobNameLabel;
    private LinearLayout mProInfoLayout;
    private ContainerView mContainerView;
    private Button mProfileLogoutBtn;
    private UserModel userModel;
    private User user;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View v) {
        toolbar = (QToolbar) v.findViewById(R.id.toolbar);
        mContainerView = (ContainerView) v.findViewById(R.id.mContainerView);
        mProfileLogoutBtn = (Button) v.findViewById(R.id.mProfileLogoutBtn);
        mProfileLogoutBtn.setOnClickListener(this);
        mStoreNameLabel = (TextView) v.findViewById(R.id.mStoreNameLabel);
        mUserNameLabel = (TextView) v.findViewById(R.id.mUserNameLabel);
        mUserJobNameLabel = (TextView) v.findViewById(R.id.mUserJobNameLabel);
        mProInfoLayout = (LinearLayout) v.findViewById(R.id.mProInfoLayout);
        mProInfoLayout.setOnClickListener(this);
        mSwipeRefreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.mSwipeRefreshLayout));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        boolean isPushBind = PrefsAccessor.getInstance(MyApplication.getInstance()).getBoolean(Constants.KEY_PUSH);
        if (isPushBind) {
            v.findViewById(R.id.line).setVisibility(View.GONE);
        }
    }


    @Override
    protected void initData() {
        super.initData();
        toolbar.setTitle(R.string.app_home_profile_label);
        userModel = new UserModel((OnBaseModelListener) getActivity());
        ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();
        ArrayList<BaseRowDescriptor> rowDescriptors = new ArrayList<BaseRowDescriptor>();
        rowDescriptors.add(new IOSRowDescriptor(R.drawable.ic_phone, "联系客服", RowActionEnum.ACTION_PROFILE_CALL_PHONE));
        if (MyApplication.getInstance().getStore().isShopkeeper()) {
//        TODO 店长显示，店员隐藏
            rowDescriptors.add(new IOSRowDescriptor(R.drawable.ic_people, "人员管理", RowActionEnum.ACTION_PROFILE_PERSON_MANAGER));
        }
        GroupDescriptor groupDescriptor = new GroupDescriptor("", rowDescriptors);
        groupDescriptors.add(groupDescriptor);
        ContainerDescriptor containerDescriptor = new ContainerDescriptor(groupDescriptors);
        mContainerView.initializeData(containerDescriptor, this);
        mContainerView.notifyDataChanged();

//        TODO init logout btn
        if (MyApplication.getInstance().isLogin()) {
            mProfileLogoutBtn.setVisibility(View.VISIBLE);
        } else {
            mProfileLogoutBtn.setVisibility(View.GONE);
        }
        setUserInfo(MyApplication.getInstance().getUser());
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        userModel.loadUserInfo(MyApplication.getInstance().getUser().getMobile(), new Callback<User>() {
            @Override
            public void onSuccess(User data) {
                data.setUser_lev(MyApplication.getInstance().getUser().getUser_lev());
                setUserInfo(data);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int code, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setUserInfo(User user) {
        this.user = user;
        mUserNameLabel.setText("姓名: " + user.getNickname());
//        TODO set store name
        mStoreNameLabel.setText("门店: " + MyApplication.getInstance().getStore().getStore_name());
        mUserJobNameLabel.setText(MyApplication.getInstance().getStore().getJobName());
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onMenuItemClick(int id) {

    }

    @Override
    public void onRowClick(View rowView, RowActionEnum action) {
        switch (action) {
            case ACTION_PROFILE_PERSON_MANAGER:
                UMEventUtil.onEvent(getContext(), UMEvent.setctrlperson);
                startActivity(new Intent(getActivity(), PersonnelManagementActivity.class));
                break;
            case ACTION_PROFILE_CALL_PHONE:
                UMEventUtil.onEvent(getContext(), UMEvent.setlinkservice);
                new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.call_phone)).setNegativeButton(R.string.Sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UMEventUtil.onEvent(getContext(), UMEvent.setlinkserviceok);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String[] permissions = new String[]{PermissionHelper.Permission.CALL_PHONE};
                            int checkPermission = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
                            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{PermissionHelper.Permission.CALL_PHONE}, PermissionHelper.ASK_CALL_PHONE);
                            } else {
                                callPhone();
                            }
                        } else {
                            callPhone();
                        }
                    }
                }).setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UMEventUtil.onEvent(getContext(), UMEvent.setlinkserviceback);
                    }
                }).setMessage("客服电话: " + MyApplication.getInstance().getStore().getClient()).setCancelable(false).show();
                break;
            default:
                break;
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + MyApplication.getInstance().getStore().getClient()));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mProfileLogoutBtn:
                UMEventUtil.onEvent(getContext(), UMEvent.setctrlloginexit);
                showDialog();
                break;
            case R.id.mProInfoLayout:
                UMEventUtil.onEvent(getContext(), UMEvent.setpersoninfo);
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.doLogout)).setNegativeButton(R.string.Sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UMEventUtil.onEvent(getContext(), UMEvent.setctrlloginok);
                doLogout();
            }
        }).setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UMEventUtil.onEvent(getContext(), UMEvent.setctrlloginback);
            }
        }).setMessage("确定退出登录吗? ").setCancelable(false).show();
    }

    private void doLogout() {
        MyApplication.getInstance().loginOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mUserNameLabel.setText("姓名: " + MyApplication.getInstance().getUser().getNickname());
        setUserInfo(MyApplication.getInstance().getUser());
    }

    @Override
    public void onRefresh() {
        loadDataFromServer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionHelper.ASK_CALL_PHONE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MyApplication.getInstance(), "获取拨打电话权限已被禁止,无法拨打电话", Toast.LENGTH_SHORT).show();
            } else {
                callPhone();
            }
        }
    }
}
